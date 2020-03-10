/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import dao.PaisDAO;
import datos.Pais;
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
public class SqlDbPaisImpl implements PaisDAO {

    Connection conexion;

    public SqlDbPaisImpl() {
        conexion = SqlDbDAOFactory.crearConexion();
    }

    @Override
    public int InsertarPais(Pais pais) {
        int valor = 1;
        
        if(ConsultarPais(pais.getCodigo())==null&&ConsultarPais(pais.getNombrepais())==null){
        String sql = "INSERT INTO pais VALUES(?, ?)";
        PreparedStatement sentencia;
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, pais.getCodigo());
            sentencia.setString(2, pais.getNombrepais().toUpperCase());
            int filas = sentencia.executeUpdate();
            //System.out.printf("Filas insertadas: %d%n", filas);
            if (filas > 0) {
                valor = 0;
                 System.out.printf("Pais %d insertado%n", pais.getCodigo());
            }
            sentencia.close();

        } catch (SQLException e) {
            valor=-1;
        }
        }else{
        System.out.printf("Pais: %d Ya existe %n", pais.getCodigo());
}
        return valor;
    }

    @Override
    public int EliminarPais(int codigo) {
        int valor = 1;
        

        String sqlAux = "SELECT * FROM Municipio where codpais=?";
        PreparedStatement sentenciaAux;   
        ResultSet rsAux=null;
        try {
            sentenciaAux = conexion.prepareStatement(sqlAux);
            sentenciaAux.setInt(1, codigo);
            rsAux = sentenciaAux.executeQuery();
        }catch(Exception ex){}
            
        
        try {
        if (!rsAux.next()){
        String sql = "DELETE FROM pais WHERE codigo = ? ";
        PreparedStatement sentencia;
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, codigo);
            int filas = sentencia.executeUpdate();
            if (filas > 0) {
                valor = 0;
                System.out.printf("Pais %d eliminado%n", codigo);
            }
            sentencia.close();}
        } catch (SQLException e) {  
            valor=-1;
        }
        return valor;
    }

    @Override
    public int ModificarPais(int codigo, String nuevonombre) {
        int valor = 1;
        if(ConsultarPais(nuevonombre)==null){
        String sql = "UPDATE pais SET nombrepais = ? WHERE codigo = ? ";
        PreparedStatement sentencia;
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(2, codigo);
            sentencia.setString(1, nuevonombre);
            int filas = sentencia.executeUpdate();
            if (filas > 0) {
                valor = 0;
                System.out.printf("Pais %d modificado%n", codigo);
            }
            sentencia.close();
        } catch (SQLException e) {
            valor=-1;
        }}
        return valor;
    }

    @Override
    public Pais ConsultarPais(int codigo) {
        String sql = "SELECT codigo, nombrepais FROM pais WHERE codigo =  ?";
        PreparedStatement sentencia;
        Pais pais=new Pais();       
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, codigo);
            ResultSet rs = sentencia.executeQuery();          
            if (rs.next()) {
                pais.setCodigo(rs.getInt("codigo"));
                pais.setNombrepais(rs.getString("nombrepais"));
            }
            else{
                System.out.printf("Pais: %d No existe%n",codigo);
                pais=null;
            }
            rs.close();// liberar recursos
            sentencia.close();
         
        } catch (SQLException e) {
            pais=null;
        }
        return pais;
    }

    @Override
    public Pais ConsultarPais(String nombre) {
        String sql = "SELECT codigo, nombrepais FROM pais WHERE nombrepais =  ?";
        PreparedStatement sentencia;
        Pais pais=new Pais();       
        try {
            sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            ResultSet rs = sentencia.executeQuery();          
            if (rs.next()) {
                pais.setCodigo(rs.getInt("codigo"));
                pais.setNombrepais(rs.getString("nombrepais"));
            }
            else{
                System.out.printf("Pais: %s No existe%n",nombre);
                pais=null;
            }
            rs.close();// liberar recursos
            sentencia.close();
         
        } catch (SQLException e) {
            pais=null;
        }
        return pais;
    }

    @Override
    public ArrayList<Pais> TodosPaises() {
        ArrayList<Pais> ret=new ArrayList();
        String sql = "SELECT codigo,nombrepais FROM pais";
        PreparedStatement sentencia;
        Pais pais = new Pais();        
        try {
            sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();       
            while (rs.next()) {
                pais.setCodigo(rs.getInt("codigo"));
                pais.setNombrepais(rs.getString("nombrepais"));
                ret.add(pais);
                pais = new Pais();   
            }
            
            rs.close();// liberar recursos
            sentencia.close();
         
        } catch (SQLException e) {           
        }
        return ret;
    }
    
}
