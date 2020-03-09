package paises.componente.ra6;

import dao.*;

public abstract class DAOFactory {
  // Bases de datos soportadas
  public static final int MYSQL = 1;  
  public static final int NEODATIS = 2;
  public static final int ORACLE = 3;
 
  public abstract MunicipioDAO getMunicipioDAO();
  public abstract PaisDAO getPaisDAO();
  public abstract PersonaDAO getPersonaDAO();
  public abstract ViviendaDAO getViviendaDAO();
  
  public static DAOFactory getDAOFactory(int bd) {  
    switch (bd) {
      case MYSQL:          
           return new SqlDbDAOFactory(MYSQL);     
      case NEODATIS:       
            return new NeodatisDAOFactory();
      case ORACLE:       
           return new SqlDbDAOFactory(ORACLE);     
      default           : 
          return null;
    }
  }
}
