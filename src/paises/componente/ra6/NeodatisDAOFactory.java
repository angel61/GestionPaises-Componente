/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paises.componente.ra6;

import neodatis.*;
import dao.*;
import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;



public class NeodatisDAOFactory extends DAOFactory {

    static ODB odb = null;

    public NeodatisDAOFactory() {
    }

    public static ODB crearConexion() {
        if (odb == null) {
		odb = ODBFactory.openClient("localhost", 8000, "base1");
        }
        return odb;
    }

    @Override
    public MunicipioDAO getMunicipioDAO() {
        return new NeodatisMunicipioImpl();
    }

    @Override
    public PaisDAO getPaisDAO() {
        return new NeodatisPaisImpl();
    }

    @Override
    public PersonaDAO getPersonaDAO() {
        return new NeodatisPersonaImpl();
    }

    @Override
    public ViviendaDAO getViviendaDAO() {
        return new NeodatisViviendaImpl();
    }

}
