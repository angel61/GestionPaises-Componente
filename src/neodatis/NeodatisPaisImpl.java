/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neodatis;

import dao.PaisDAO;
import datos.Municipio;
import datos.Pais;
import datos.Pais;
import java.util.ArrayList;
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
public class NeodatisPaisImpl implements PaisDAO {

    static ODB bd;

    public NeodatisPaisImpl() {
        bd = NeodatisDAOFactory.crearConexion();
    }

    @Override
    public int InsertarPais(Pais pais) {
        String nombre=pais.getNombrepais().toUpperCase();
        pais.setNombrepais(nombre);
        
        if(ConsultarPais(pais.getCodigo())==null&&ConsultarPais(pais.getNombrepais())==null){
        bd.store(pais);
        bd.commit();
        System.out.printf("Pais: %d Insertado %n", pais.getCodigo());
        return 0;
        }else{
        System.out.printf("Pais: %d Ya existe %n", pais.getCodigo());
        return 1;
        }
    }

    @Override
    public int EliminarPais(int codigo) {
        int valor = 1;
        
        
        IQuery queryAux = new CriteriaQuery(Municipio.class, Where.equal("codpais", codigo));
        Objects<Municipio> objetosAux = bd.getObjects(queryAux);
        if(!objetosAux.hasNext()){
        
        IQuery query = new CriteriaQuery(Pais.class, Where.equal("codigo", codigo));
        Objects<Pais> objetos = bd.getObjects(query);
        try {
            Pais pais = (Pais) objetos.getFirst();
            bd.delete(pais);
            bd.commit();
            valor = 0;
            System.out.printf("Pais: %d eliminado %n", codigo);
        } catch (IndexOutOfBoundsException i) {
            valor=-1;
            System.out.printf("Pais a eliminar: %d No existe%n", codigo);
        }
        }
        return valor;
    }

    @Override
    public int ModificarPais(int codigo, String nuevonombre) {
        int valor = 1;
        IQuery query = new CriteriaQuery(Pais.class, Where.equal("codigo", codigo));
        Objects<Pais> objetos = bd.getObjects(query);
        try {
            Pais pais = (Pais) objetos.getFirst();
            pais.setNombrepais(nuevonombre.toUpperCase());
            bd.store(pais); // actualiza el objeto 
            valor = 0;
            bd.commit();
        } catch (IndexOutOfBoundsException i) {
            valor=-1;
            System.out.printf("Pais: %d No existe%n", nuevonombre);
        }

        return valor;
    }

    @Override
    public Pais ConsultarPais(int codigo) {
        IQuery query = new CriteriaQuery(Pais.class, Where.equal("codigo", codigo));
        Objects<Pais> objetos = bd.getObjects(query);
        Pais pais = null;
        if (objetos.size()>0) {
            try {
                pais = (Pais) objetos.getFirst();
            } catch (Exception i) {
                System.out.printf("Pais: %d No existe%n", codigo);
                pais=null;
            }
        }
        return pais;
    }

    @Override
    public Pais ConsultarPais(String nombre) {
        IQuery query = new CriteriaQuery(Pais.class, Where.equal("nombrepais", nombre));
        Objects<Pais> objetos = bd.getObjects(query);
        Pais pais = null;
        if (objetos.size()>0) {
            try {
                pais = (Pais) objetos.getFirst();
            } catch (Exception i) {
                System.out.printf("Pais: %s No existe%n", nombre);
                pais=null;
            }
        }
        return pais;
    }

    @Override
    public ArrayList<Pais> TodosPaises() {
        ArrayList<Pais> ret=new ArrayList();
        IQuery query = new CriteriaQuery(Pais.class);
        Objects<Pais> objetos = bd.getObjects(query);
        Pais pais = new Pais();
        while(objetos.hasNext()){
            pais=objetos.next();
            ret.add(pais);
        }
        return ret;
    }
    
}
