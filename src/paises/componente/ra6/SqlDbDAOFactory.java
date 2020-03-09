/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paises.componente.ra6;

import sql.SqlDbPersonaImpl;
import sql.*;
import sql.SqlDbMunicipioImpl;
import sql.SqlDbViviendaImpl;
import dao.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlDbDAOFactory extends DAOFactory {

    static Connection conexion = null;
    static String DRIVER = "";
    static String URLDB = "";
    static String USUARIO = "paises";
    static String CLAVE = "paises";

    public SqlDbDAOFactory(int bd) {
        if(DAOFactory.MYSQL==bd){
        DRIVER = "com.mysql.jdbc.Driver";
        URLDB = "jdbc:mysql://localhost/paises";
        }else{
        DRIVER = "oracle.jdbc.driver.OracleDriver";
        //URLDB = "jdbc:oracle:thin:@localhost:1521:XE";
        URLDB = "jdbc:oracle:thin:@localhost:1521:orcl";
        }
        conexion=null;
    }

    // crear la conexion
    public static Connection crearConexion() {
        if (conexion == null) {
            try {
                Class.forName(DRIVER); // Cargar el driver
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SqlDbDAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                conexion = DriverManager.getConnection(URLDB, USUARIO, CLAVE);
            } catch (SQLException ex) {
                System.out.printf("HA OCURRIDO UNA EXCEPCIÓN:%n");
                System.out.printf("Mensaje   : %s %n", ex.getMessage());
                System.out.printf("SQL estado: %s %n", ex.getSQLState());
                System.out.printf("Cód error : %s %n", ex.getErrorCode());
            }
        }
        return conexion;
    }

    @Override
    public MunicipioDAO getMunicipioDAO() {
        return new SqlDbMunicipioImpl();
    }

    @Override
    public PaisDAO getPaisDAO() {
        return new SqlDbPaisImpl();
    }

    @Override
    public PersonaDAO getPersonaDAO() {
        return new SqlDbPersonaImpl();
    }

    @Override
    public ViviendaDAO getViviendaDAO() {
        return new SqlDbViviendaImpl();
    }
}
