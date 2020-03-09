/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import dao.MunicipioDAO;
import datos.Municipio;
import datos.Municipio;
import datos.Pais;
import datos.Vivienda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import paises.componente.ra6.SqlDbDAOFactory;

/**
 *
 * @author yuter
 */
public class SqlDbMunicipioImpl implements MunicipioDAO {

    Connection conexion;

    public SqlDbMunicipioImpl() {
        conexion = SqlDbDAOFactory.crearConexion();
    }

    @Override
    public int InsertaMunicipio(Municipio muni) {
        int valor = 1;
        
        String str = muni.getNombre();
        String nombre = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();

        String sqlAux = "SELECT * FROM pais where codigo=?";
        PreparedStatement sentenciaAux;
        ResultSet rsAux = null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setInt(1, muni.getCodpais());
            rsAux = sentenciaAux.executeQuery();
        } catch (Exception ex) {
        }

        try {
            if (ConsultarMunicipio(muni.getNombre()) == null && rsAux.next()) {
                String sql = "INSERT INTO municipio VALUES(?, ?, ?, ?, ?)";
                PreparedStatement sentencia;
                try {
                    sentencia = conexion.prepareStatement(sql);
                    sentencia.setString(1, nombre);
                    sentencia.setInt(2, muni.getCodpais());
                    sentencia.setDouble(3, muni.getTasapago());
                    sentencia.setDouble(4, muni.getSumaimpuestos());
                    sentencia.setInt(5, muni.getNumerohabitantes());
                    int filas = sentencia.executeUpdate();
                    if (filas > 0) {
                        valor = 0;
                        System.out.printf("Municipio %s insertado%n", muni.getNombre());
                    }
                    sentencia.close();

                } catch (SQLException e) {
                    valor = -1;
                }
            } else {
                System.out.printf("Municipio: %s Ya existe %n", muni.getNombre());
            }
        } catch (SQLException ex) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public int EliminarMunicipio(String nombre) {
        int valor = 1;

        String sqlAux = "SELECT * FROM vivienda where nombremunicipio=?";
        PreparedStatement sentenciaAux;
        ResultSet rsAux = null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setString(1, nombre);
            rsAux = sentenciaAux.executeQuery();
        } catch (Exception ex) {
        }

        try {
            if (!rsAux.next()) {
                String sql = "DELETE FROM municipio WHERE nombre = ? ";
                PreparedStatement sentencia;
                sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Municipio %s eliminado%n", nombre);
                }
                sentencia.close();
            }
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public int ModificarMunicipio(String nombre, Municipio muninuevo) {
        int valor = 1;
        String sql = "UPDATE municipio SET codpais= ?, tasapago = ? , sumaimpuestos = ?, numerohabitantes = ? WHERE nombre = ? ";
        PreparedStatement sentencia;
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(5, nombre);
            sentencia.setInt(1, muninuevo.getCodpais());
            sentencia.setDouble(2, muninuevo.getTasapago());
            sentencia.setDouble(3, muninuevo.getSumaimpuestos());
            sentencia.setInt(4, muninuevo.getNumerohabitantes());
            int filas = sentencia.executeUpdate();
            //System.out.printf("Filas modificadas: %d%n", filas);
            if (filas > 0) {
                valor = 0;
                System.out.printf("Municipio %s modificado%n", nombre);
            }
            sentencia.close();
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public Municipio ConsultarMunicipio(String nombre) {
        String sql = "SELECT codpais, tasapago , sumaimpuestos, numerohabitantes FROM municipio WHERE nombre =  ?";
        PreparedStatement sentencia;
        Municipio mun = new Municipio();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                mun.setNombre(nombre);
                mun.setCodpais(rs.getInt("codpais"));
                mun.setNumerohabitantes(rs.getInt("numerohabitantes"));
                mun.setTasapago(rs.getDouble("tasapago"));
                mun.setSumaimpuestos(rs.getDouble("sumaimpuestos"));
            } else {
                System.out.printf("Municipio: %s No existe%n", nombre);
                mun = null;
            }

            rs.close();
            sentencia.close();

        } catch (SQLException e) {
            mun = null;
        }
        return mun;
    }

    @Override
    public void ActualizarMunicipio() {
        SqlDbViviendaImpl v = new SqlDbViviendaImpl();
        ArrayList<Municipio> munis = TodosMunicipios();
        Vivienda vivienda = new Vivienda();
        Municipio mun = new Municipio();
        double imp;
        int hab;
        for (int i = 0; i < munis.size(); i++) {
            imp = 0;
            hab = 0;
            mun = munis.get(i);
            ArrayList<Vivienda> viviens = v.ViviendasDeunMunicipio(mun.getNombre());
            for (int u = 0; u < viviens.size(); u++) {
                vivienda = viviens.get(u);
                imp += vivienda.getImpuesto();
                hab += vivienda.getNumeropersonas();
            }
            vivienda = new Vivienda();

            mun.setNumerohabitantes(hab);
            mun.setSumaimpuestos(imp);
            ModificarMunicipio(mun.getNombre(), mun);
        }
    }

    @Override
    public ArrayList<Municipio> TodosMunicipios() {
        ArrayList<Municipio> ret = new ArrayList();
        String sql = "SELECT nombre, codpais, tasapago , sumaimpuestos, numerohabitantes  FROM municipio";
        PreparedStatement sentencia;
        Municipio mun = new Municipio();
        try {
            sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                mun.setNombre(rs.getString("nombre"));
                mun.setCodpais(rs.getInt("codpais"));
                mun.setNumerohabitantes(rs.getInt("numerohabitantes"));
                mun.setTasapago(rs.getDouble("tasapago"));
                mun.setSumaimpuestos(rs.getDouble("sumaimpuestos"));
                ret.add(mun);
                mun = new Municipio();
            }

            rs.close();// liberar recursos
            sentencia.close();

        } catch (SQLException e) {
        }
        return ret;
    }

    @Override
    public ArrayList<Municipio> MunicipiosdeunPais(int codpais) {
        ArrayList<Municipio> ret = new ArrayList();
        String sql = "SELECT nombre, codpais, tasapago , sumaimpuestos, numerohabitantes  FROM municipio where codpais=?";
        PreparedStatement sentencia;
        Municipio mun = new Municipio();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, codpais);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                mun.setNombre(rs.getString("nombre"));
                mun.setCodpais(rs.getInt("codpais"));
                mun.setNumerohabitantes(rs.getInt("numerohabitantes"));
                mun.setTasapago(rs.getDouble("tasapago"));
                mun.setSumaimpuestos(rs.getDouble("sumaimpuestos"));
                ret.add(mun);
                mun = new Municipio();
            }

            rs.close();// liberar recursos
            sentencia.close();

        } catch (SQLException e) {
        }
        return ret;
    }

    @Override
    public ArrayList<Municipio> MunicipiosdeunPais(String nombrepais) {
        SqlDbPaisImpl p = new SqlDbPaisImpl();
        Pais pais = p.ConsultarPais("nombrepais");
        ArrayList<Municipio> ret = new ArrayList();
        String sql = "SELECT nombre, codpais, tasapago , sumaimpuestos, numerohabitantes  FROM municipio where codpais=?";
        PreparedStatement sentencia;
        Municipio mun = new Municipio();
        if (pais != null) {
            try {
                sentencia = conexion.prepareStatement(sql);
                sentencia.setInt(1, pais.getCodigo());
                ResultSet rs = sentencia.executeQuery();
                while (rs.next()) {
                    mun.setNombre(rs.getString("nombre"));
                    mun.setCodpais(rs.getInt("codpais"));
                    mun.setNumerohabitantes(rs.getInt("numerohabitantes"));
                    mun.setTasapago(rs.getDouble("tasapago"));
                    mun.setSumaimpuestos(rs.getDouble("sumaimpuestos"));
                    ret.add(mun);
                    mun = new Municipio();
                }

                rs.close();// liberar recursos
                sentencia.close();

            } catch (SQLException e) {
            }
        }
        return ret;
    }

}
