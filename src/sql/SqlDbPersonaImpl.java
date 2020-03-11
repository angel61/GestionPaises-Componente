/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import dao.PersonaDAO;
import datos.Persona;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import paises.componente.ra6.SqlDbDAOFactory;

/**
 *
 * @author angel
 */
public class SqlDbPersonaImpl implements PersonaDAO {

    Connection conexion;

    public SqlDbPersonaImpl() {
        conexion = SqlDbDAOFactory.crearConexion();
    }

    @Override
    public int InsertarPersona(Persona persona) {
        int valor = 1;
        
        String str = persona.getNombre();
        String nombre = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();

        String sqlAux = "SELECT * FROM vivienda where codigo=?";
        PreparedStatement sentenciaAux;
        ResultSet rsAux = null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setInt(1, persona.getCodvivienda());
            rsAux = sentenciaAux.executeQuery();
        } catch (Exception ex) {
        }

            try {
        if (ConsultarPersona(nombre) == null && rsAux.next()) {
            String sql = "INSERT INTO persona VALUES(?, ?, ?)";
            PreparedStatement sentencia;
                sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                sentencia.setInt(2, persona.getEdad());
                sentencia.setInt(3, persona.getCodvivienda());
                int filas = sentencia.executeUpdate();
                //System.out.printf("Filas insertadas: %d%n", filas);
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Persona %s insertado%n", persona.getNombre());
                    new SqlDbViviendaImpl().ActualizarVivienda();
                    new SqlDbMunicipioImpl().ActualizarMunicipio();
                }
                sentencia.close();

        } else {
            System.out.printf("Persona: %s Ya existe %n", persona.getNombre());
        }
            } catch (SQLException e) {
                valor = -1;
            }
        return valor;
    }

    @Override
    public int EliminarPersona(String nombre) {
        int valor = 1;
        try {
                String sql = "DELETE FROM persona WHERE nombre = ? ";
                PreparedStatement sentencia;
                sentencia = conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                int filas = sentencia.executeUpdate();
                if (filas > 0) {
                    valor = 0;
                    System.out.printf("Persona %s eliminada%n", nombre);
                    new SqlDbViviendaImpl().ActualizarVivienda();
                    new SqlDbMunicipioImpl().ActualizarMunicipio();
                }
                sentencia.close();
            
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public int ModificarPersona(String nombre, Persona nuevaper) {
        int valor = 1;
        String sql = "UPDATE persona SET edad= ?, codvivienda = ? WHERE nombre = ? ";
        PreparedStatement sentencia;
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(3, nuevaper.getNombre());
            sentencia.setInt(1, nuevaper.getEdad());
            sentencia.setInt(2, nuevaper.getCodvivienda());
            int filas = sentencia.executeUpdate();

            if (filas > 0) {
                valor = 0;
                System.out.printf("Persona %s modificada%n", nombre);
                new SqlDbViviendaImpl().ActualizarVivienda();
                new SqlDbMunicipioImpl().ActualizarMunicipio();
            }
            sentencia.close();
        } catch (SQLException e) {
            valor = -1;
        }
        return valor;
    }

    @Override
    public Persona ConsultarPersona(String nombre) {
        String sql = "SELECT edad,codvivienda FROM persona WHERE nombre =  ?";
        PreparedStatement sentencia;
        Persona per = new Persona();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                per.setNombre(nombre);
                per.setEdad(rs.getInt("edad"));
                per.setCodvivienda(rs.getInt("codvivienda"));
            } else {
                System.out.printf("Persona: %s No existe%n", nombre);
                per = null;
            }

            rs.close();
            sentencia.close();

        } catch (SQLException e) {
            per = null;
        }
        return per;
    }

    @Override
    public ArrayList<Persona> TodosPersonas() {
        ArrayList<Persona> ret = new ArrayList();
        String sql = "SELECT nombre,edad,codvivienda FROM persona";
        PreparedStatement sentencia;
        Persona per = new Persona();
        try {
            sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                per.setNombre(rs.getString("nombre"));
                per.setEdad(rs.getInt("edad"));
                per.setCodvivienda(rs.getInt("codvivienda"));

                ret.add(per);
                per = new Persona();
            }

            rs.close();
            sentencia.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> TodosPersonasVivienda(int codigovivienda) {
        ArrayList<Persona> ret = new ArrayList();
        String sql = "select nombre, edad, codvivienda from persona where codvivienda = ?";
        PreparedStatement sentencia;
        Persona per = new Persona();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1,codigovivienda);
            
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                per.setNombre(rs.getString("nombre"));
                per.setEdad(rs.getInt("edad"));
                per.setCodvivienda(rs.getInt("codvivienda"));

                ret.add(per);
                per = new Persona();
            }

            rs.close();
            sentencia.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> PersonasDeunPais(int codigopais) {
        ArrayList<Persona> ret = new ArrayList();
        String sql = "select per.nombre,per.edad,per.codvivienda from persona per,vivienda vi,municipio mun, pais pa where pa.codigo=mun.codpais and mun.nombre=vi.nombremunicipio and vi.codigo=per.codvivienda and pa.codigo=?";
        PreparedStatement sentencia;
        Persona per = new Persona();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, codigopais);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                per.setNombre(rs.getString("nombre"));
                per.setEdad(rs.getInt("edad"));
                per.setCodvivienda(rs.getInt("codvivienda"));

                ret.add(per);
                per = new Persona();
            }

            rs.close();
            sentencia.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> PersonasDeunMunicipio(String nombremunicipio) {
        ArrayList<Persona> ret = new ArrayList();
        String sql = "select per.nombre,per.edad,per.codvivienda from persona per,vivienda vi,municipio mun where mun.nombre=vi.nombremunicipio and vi.codigo=per.codvivienda and mun.nombre=?";
        PreparedStatement sentencia;
        Persona per = new Persona();
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombremunicipio);
            ResultSet rs = sentencia.executeQuery();
            while (rs.next()) {
                per.setNombre(rs.getString("nombre"));
                per.setEdad(rs.getInt("edad"));
                per.setCodvivienda(rs.getInt("codvivienda"));

                ret.add(per);
                per = new Persona();
            }

            rs.close();
            sentencia.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return ret;
    }

}
