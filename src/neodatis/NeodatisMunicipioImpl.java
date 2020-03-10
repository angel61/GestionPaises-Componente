/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neodatis;

import dao.MunicipioDAO;
import datos.*;
import java.util.ArrayList;
import static neodatis.NeodatisMunicipioImpl.bd;
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
public class NeodatisMunicipioImpl implements MunicipioDAO {

    static ODB bd = null;

    public NeodatisMunicipioImpl() {
        bd = NeodatisDAOFactory.crearConexion();
    }

    @Override
    public int InsertaMunicipio(Municipio muni) {
        String str = muni.getNombre();
        String nombre = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        muni.setNombre(nombre);
        
        NeodatisPaisImpl paisDAO=new NeodatisPaisImpl();
        if (ConsultarMunicipio(muni.getNombre()) == null&&paisDAO.ConsultarPais(muni.getCodpais())!=null) {
            bd.store(muni);
            bd.commit();
            System.out.printf("Municipio: %s Insertado %n", muni.getNombre());
            return 0;
        } else {
            System.out.printf("Municipio: %s Ya existe %n", muni.getNombre());
            return 1;
        }
    }

    @Override
    public int EliminarMunicipio(String nombre) {
        int valor = 1;

        IQuery queryAux = new CriteriaQuery(Vivienda.class, Where.equal("nombremunicipio", nombre));
        Objects<Vivienda> objetosAux = bd.getObjects(queryAux);
        if (!objetosAux.hasNext()) {

            IQuery query = new CriteriaQuery(Municipio.class, Where.equal("nombre", nombre));
            Objects<Municipio> objetos = bd.getObjects(query);
            try {
                Municipio mun = (Municipio) objetos.getFirst();
                bd.delete(mun);
                bd.commit();
                valor = 0;
                System.out.printf("Municipio: %s eliminado %n", nombre);
            } catch (IndexOutOfBoundsException i) {
                valor = -1;
                System.out.printf("Municipio a eliminar: %s No existe%n", nombre);
            }
        }
        return valor;
    }

    @Override
    public int ModificarMunicipio(String nombre, Municipio muninuevo) {
        int valor = 1;
        IQuery query = new CriteriaQuery(Municipio.class, Where.equal("nombre", nombre));
        Objects<Municipio> objetos = bd.getObjects(query);
        try {
            Municipio mun = (Municipio) objetos.getFirst();
            mun.setCodpais(muninuevo.getCodpais());
            mun.setNumerohabitantes(muninuevo.getNumerohabitantes());
            mun.setSumaimpuestos(muninuevo.getSumaimpuestos());
            mun.setTasapago(muninuevo.getTasapago());
            bd.store(mun); // actualiza el objeto 
            valor = 0;
            bd.commit();
        } catch (IndexOutOfBoundsException i) {
            valor = -1;
            System.out.printf("Municipio: %s No existe%n", nombre);
        }

        return valor;
    }

    @Override
    public Municipio ConsultarMunicipio(String nombre) {
        IQuery query = new CriteriaQuery(Municipio.class, Where.equal("nombre", nombre));
        Objects<Municipio> objetos = bd.getObjects(query);
        Municipio mun = null;
        if (objetos.size()>0) {
            try {
                mun = (Municipio) objetos.getFirst();
            } catch (Exception i) {
                System.out.printf("Municipio: %d No existe%n", nombre);
                mun = null;
            }
        }
        return mun;
    }

    @Override
    public void ActualizarMunicipio() {
        NeodatisViviendaImpl v=new NeodatisViviendaImpl();
        ArrayList<Municipio>munis=TodosMunicipios();
        Vivienda vivienda=new Vivienda();
        Municipio mun= new Municipio();
        double imp;
        int hab;
        for(int i=0;i<munis.size();i++){
            imp=0;
            hab=0;
            mun=munis.get(i);
            ArrayList<Vivienda> viviens=v.ViviendasDeunMunicipio(mun.getNombre());
            for(int u=0;u<viviens.size();u++){
                vivienda=viviens.get(u);
                imp+=vivienda.getImpuesto();
                hab+=vivienda.getNumeropersonas();
            }
            vivienda=new Vivienda();
            
            mun.setNumerohabitantes(hab);
            mun.setSumaimpuestos(imp);
            ModificarMunicipio(mun.getNombre(),mun);
        }
    }

    @Override
    public ArrayList<Municipio> TodosMunicipios() {
        ArrayList<Municipio> ret = new ArrayList();
        Objects<Municipio> objetos = bd.getObjects(Municipio.class);
        Municipio mun = new Municipio();
        while (objetos.hasNext()) {
            mun = objetos.next();
            ret.add(mun);
        }
        return ret;
    }

    @Override
    public ArrayList<Municipio> MunicipiosdeunPais(int codpais) {
        ArrayList<Municipio> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Municipio.class, Where.equal("codpais", codpais));
        Objects<Municipio> objetos = bd.getObjects(query);
        Municipio mun = new Municipio();
        while (objetos.hasNext()) {
            mun = objetos.next();
            ret.add(mun);
        }
        return ret;
    }

    @Override
    public ArrayList<Municipio> MunicipiosdeunPais(String nombrepais) {
        int codpais = -1;
        ArrayList<Municipio> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Pais.class, Where.equal("nombrepais", nombrepais));
        Objects<Pais> objetos = bd.getObjects(query);
        if (objetos.size()>0) {
            try {
                codpais = ((Pais) objetos.getFirst()).getCodigo();
            } catch (IndexOutOfBoundsException i) {
            }
        query = new CriteriaQuery(Municipio.class, Where.equal("codpais", codpais));
        Objects<Municipio> objetoss = bd.getObjects(query);
        Municipio mun = new Municipio();
        while (objetoss.hasNext()) {
            mun = objetoss.next();
            ret.add(mun);
        }
        }
        return ret;
    }


}
