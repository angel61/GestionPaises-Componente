/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neodatis;

import dao.PersonaDAO;
import datos.*;
import java.util.ArrayList;
import static neodatis.NeodatisMunicipioImpl.bd;
import static neodatis.NeodatisViviendaImpl.bd;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import paises.componente.ra6.NeodatisDAOFactory;

/**
 *
 * @author angel
 */
public class NeodatisPersonaImpl implements PersonaDAO {

    static ODB bd = null;

    public NeodatisPersonaImpl() {
        bd = NeodatisDAOFactory.crearConexion();
    }

    @Override
    public int InsertarPersona(Persona persona) {
        
        String str = persona.getNombre();
        String nombre = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        persona.setNombre(nombre);
        
        NeodatisViviendaImpl viviDAO=new NeodatisViviendaImpl();
        if (ConsultarPersona(persona.getNombre()) == null&&viviDAO.ConsultarVivienda(persona.getCodvivienda())!=null) {
            bd.store(persona);
            bd.commit();
            new NeodatisViviendaImpl().ActualizarVivienda();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
            System.out.printf("Persona: %s Insertada %n", persona.getNombre());
            return 0;
        } else {
            System.out.printf("Persona: %s Ya existe %n", persona.getNombre());
            return 1;
        }
    }

    @Override
    public int EliminarPersona(String nombre) {
        int valor = 1;

        IQuery query = new CriteriaQuery(Persona.class, Where.equal("nombre", nombre));
        Objects<Persona> objetos = bd.getObjects(query);
        try {
            Persona per = (Persona) objetos.getFirst();
            bd.delete(per);
            bd.commit();
            new NeodatisViviendaImpl().ActualizarVivienda();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
            valor = 0;
            System.out.printf("Persona: %s eliminada %n", nombre);
        } catch (IndexOutOfBoundsException i) {
            valor = -1;
            System.out.printf("Persona a eliminar: %s No existe%n", nombre);
        }

        return valor;
    }

    @Override
    public int ModificarPersona(String nombre, Persona nuevaper) {
        int valor = 1;
        IQuery query = new CriteriaQuery(Persona.class, Where.equal("nombre", nombre));
        Objects<Persona> objetos = bd.getObjects(query);
        try {
            Persona per = (Persona) objetos.getFirst();
            per.setCodvivienda(nuevaper.getCodvivienda());
            per.setEdad(nuevaper.getEdad());
            bd.store(per);
            valor = 0;
            bd.commit();
            new NeodatisViviendaImpl().ActualizarVivienda();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
        } catch (IndexOutOfBoundsException i) {
            valor = -1;
            System.out.printf("Persona: %s No existe%n", nombre);
        }

        return valor;
    }

    @Override
    public Persona ConsultarPersona(String nombre) {
        IQuery query = new CriteriaQuery(Persona.class, Where.equal("nombre", nombre));
        Objects<Persona> objetos = bd.getObjects(query);
        Persona per = null;
        if (objetos .size()>0) {
            try {
                per = (Persona) objetos.getFirst();
            } catch (Exception i) {
                System.out.printf("Persona: %d No existe%n", nombre);
                per = null;
            }
        }
        return per;
    }

    @Override
    public ArrayList<Persona> TodosPersonas() {
        ArrayList<Persona> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Persona.class);
        Objects<Persona> objetos = bd.getObjects(query);
        Persona per = new Persona();
        while (objetos.hasNext()) {
            per = objetos.next();
            ret.add(per);
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> TodosPersonasVivienda(int codigovivienda) {
        ArrayList<Persona> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Persona.class, Where.equal("codvivienda", codigovivienda));
        Objects<Persona> objetos = bd.getObjects(query);
        Persona per = new Persona();
        while (objetos.hasNext()) {
            per = objetos.next();
            ret.add(per);
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> PersonasDeunPais(int codigopais) {
        ArrayList<Persona> ret = new ArrayList();
        IQuery query3 = new CriteriaQuery(Municipio.class, Where.equal("codpais", codigopais));
        Objects<Municipio> objetos3 = bd.getObjects(query3);
        String nombremunicipio = "";
        while (objetos3.hasNext()) {
            nombremunicipio = objetos3.next().getNombre();

            IQuery query2 = new CriteriaQuery(Vivienda.class, Where.equal("nombremunicipio", nombremunicipio));
            Objects<Vivienda> objetos2 = bd.getObjects(query2);
            int codigovivienda = 0;
            while (objetos2.hasNext()) {
                codigovivienda = objetos2.next().getCodigo();

                IQuery query = new CriteriaQuery(Persona.class, Where.equal("codvivienda", codigovivienda));
                Objects<Persona> objetos = bd.getObjects(query);
                Persona per = new Persona();
                while (objetos.hasNext()) {
                    per = objetos.next();
                    ret.add(per);
                }

            }
        }
        return ret;
    }

    @Override
    public ArrayList<Persona> PersonasDeunMunicipio(String nombremunicipio) {
        ArrayList<Persona> ret = new ArrayList();
        IQuery query2 = new CriteriaQuery(Vivienda.class, Where.equal("nombremunicipio", nombremunicipio));
        Objects<Vivienda> objetos2 = bd.getObjects(query2);
        int codigovivienda = 0;
        while (objetos2.hasNext()) {
            codigovivienda = objetos2.next().getCodigo();

            IQuery query = new CriteriaQuery(Persona.class, Where.equal("codvivienda", codigovivienda));
            Objects<Persona> objetos = bd.getObjects(query);
            Persona per = new Persona();
            while (objetos.hasNext()) {
                per = objetos.next();
                ret.add(per);
            }

        }
        return ret;
    }

}
