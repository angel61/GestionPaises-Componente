/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import dao.*;
import datos.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import paises.componente.ra6.SqlDbDAOFactory;

/**
 *
 * @author angel
 */
public class SqlDbViviendaImpl implements ViviendaDAO {

    Connection conexion;

    public SqlDbViviendaImpl() {
        conexion = SqlDbDAOFactory.crearConexion();
    }

    @Override
    public int InsertarVivienda(Vivienda vi) {
        int valor = 1;

        String sqlAux = "SELECT * FROM municipio where nombre=?";
        PreparedStatement sentenciaAux;
        ResultSet rsAux = null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setString(1, vi.getNombremunicipio());
            rsAux = sentenciaAux.executeQuery();
        } catch (Exception ex) {
        }

        try {
            if (ConsultarVivienda(vi.getCodigo()) == null && rsAux.next()) {
                String sql = "INSERT INTO Vivienda VALUES(?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement sentencia;
                sentencia = conexion.prepareStatement(sql);
                sentencia.setInt(1, vi.getCodigo());
                sentencia.setString(2, vi.getNombremunicipio());
                sentencia.setString(3, vi.getDireccion());
                sentencia.setDouble(4, vi.getMetroscuadrados());
                sentencia.setInt(5, vi.getAnyo());
                sentencia.setDouble(6, vi.getImpuesto());
                sentencia.setInt(7, vi.getNumeropersonas());
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Vivienda %d insertado%n", vi.getCodigo());
                    new SqlDbMunicipioImpl().ActualizarMunicipio();
                }
                sentencia.close();

            } else {
                System.out.printf("Vivienda: %d Ya existe %n", vi.getCodigo());
            }
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public int EliminarVivienda(int codigo) {
        int valor = 1;

        String sqlAux = "SELECT * FROM persona where codvivienda=?";
        PreparedStatement sentenciaAux;
        ResultSet rsAux = null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setInt(1, codigo);
            rsAux = sentenciaAux.executeQuery();
        } catch (Exception ex) {
        }

        try {
            if (!rsAux.next() && ConsultarVivienda(codigo) != null) {
                String sql = "DELETE FROM vivienda WHERE codigo = ? ";
                PreparedStatement sentencia;
                sentencia = conexion.prepareStatement(sql);
                sentencia.setInt(1, codigo);
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Vivienda %d eliminado%n", codigo);
                    new SqlDbMunicipioImpl().ActualizarMunicipio();
                }
                sentencia.close();
            }
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public int ModificarVivienda(int codigo, Vivienda vivnueva) {
        int valor = 1;
        if ((new SqlDbMunicipioImpl()).ConsultarMunicipio(vivnueva.getNombremunicipio()) != null) {
            String sql = "UPDATE vivienda SET nombremunicipio=?,direccion=?,metroscuadrados=?,anyo=?,impuesto=?,numeropersonas=? WHERE codigo = ? ";
            PreparedStatement sentencia;
            try {
                sentencia = conexion.prepareStatement(sql);
                sentencia.setInt(7, codigo);
                sentencia.setString(1, vivnueva.getNombremunicipio());
                sentencia.setString(2, vivnueva.getDireccion());
                sentencia.setDouble(3, vivnueva.getMetroscuadrados());
                sentencia.setInt(4, vivnueva.getAnyo());
                sentencia.setDouble(5, vivnueva.getImpuesto());
                sentencia.setInt(6, vivnueva.getNumeropersonas());
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Vivienda %d modificado%n", codigo);
                    new SqlDbMunicipioImpl().ActualizarMunicipio();
                }
                sentencia.close();
            } catch (SQLException e) {
                valor = -1;
            }
        }
        return valor;
    }

    @Override
    public Vivienda ConsultarVivienda(int codigo) {
        String sql = "SELECT nombremunicipio, direccion , metroscuadrados, anyo, impuesto, numeropersonas  FROM vivienda WHERE codigo =  ?";
        PreparedStatement sentencia;
        Vivienda vi = new Vivienda();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, codigo);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                vi.setCodigo(codigo);
                vi.setNombremunicipio(rs.getString("nombremunicipio"));
                vi.setDireccion(rs.getString("direccion"));
                vi.setMetroscuadrados(rs.getDouble("metroscuadrados"));
                vi.setAnyo(rs.getInt("anyo"));
                vi.setImpuesto(rs.getDouble("impuesto"));
                vi.setNumeropersonas(rs.getInt("numeropersonas"));
            } else {
                System.out.printf("Vivienda: %d No existe%n", codigo);
                vi = null;
            }

            rs.close();
            sentencia.close();

        } catch (SQLException e) {
            vi = null;
        }
        return vi;
    }

    @Override
    public void ActualizarVivienda() {
        SqlDbPersonaImpl p = new SqlDbPersonaImpl();
        ArrayList<Vivienda> vivi = TodasViviendas();
        Persona pers = new Persona();
        Vivienda vi = new Vivienda();
        double imp;
        int hab;
        for (int i = 0; i < vivi.size(); i++) {
            imp = 0;
            hab = 0;
            vi = vivi.get(i);
            ArrayList<Persona> personas = p.TodosPersonasVivienda(vivi.get(i).getCodigo());

            hab = personas.size();
            imp = vivi.get(i).getImpuesto();
            vi.setNumeropersonas(hab);
            vi.setImpuesto(imp);
            ModificarVivienda(vi.getCodigo(), vi);
        }
    }

    @Override
    public ArrayList<Vivienda> TodasViviendas() {
        ArrayList<Vivienda> ret = new ArrayList();
        String sql = "SELECT codigo, nombremunicipio, direccion , metroscuadrados, anyo, impuesto, numeropersonas  FROM vivienda";
        PreparedStatement sentencia;
        Vivienda vi = new Vivienda();
        try {
            sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                vi.setCodigo(rs.getInt("codigo"));
                vi.setNombremunicipio(rs.getString("nombremunicipio"));
                vi.setDireccion(rs.getString("direccion"));
                vi.setMetroscuadrados(rs.getDouble("metroscuadrados"));
                vi.setAnyo(rs.getInt("anyo"));
                vi.setImpuesto(rs.getDouble("impuesto"));
                vi.setNumeropersonas(rs.getInt("numeropersonas"));
                ret.add(vi);
                vi = new Vivienda();
            }

            rs.close();// liberar recursos
            sentencia.close();

        } catch (SQLException e) {
        }
        return ret;
    }

    @Override
    public ArrayList<Vivienda> ViviendasDeunMunicipio(String nombre) {
        ArrayList<Vivienda> ret = new ArrayList();
        String sql = "SELECT codigo, nombremunicipio, direccion , metroscuadrados, anyo, impuesto, numeropersonas  FROM vivienda where nombremunicipio=?";
        PreparedStatement sentencia;
        Vivienda vi = new Vivienda();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                vi.setCodigo(rs.getInt("codigo"));
                vi.setNombremunicipio(rs.getString("nombremunicipio"));
                vi.setDireccion(rs.getString("direccion"));
                vi.setMetroscuadrados(rs.getDouble("metroscuadrados"));
                vi.setAnyo(rs.getInt("anyo"));
                vi.setImpuesto(rs.getDouble("impuesto"));
                vi.setNumeropersonas(rs.getInt("numeropersonas"));
                ret.add(vi);
                vi = new Vivienda();
            }

            rs.close();// liberar recursos
            sentencia.close();

        } catch (SQLException e) {
        }
        return ret;
    }

}
