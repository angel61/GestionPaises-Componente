/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neodatis;

import dao.ViviendaDAO;
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
 * @author yuter
 */
public class NeodatisViviendaImpl implements ViviendaDAO {

    static ODB bd = null;

    public NeodatisViviendaImpl() {
        bd = NeodatisDAOFactory.crearConexion();
    }

    @Override
    public int InsertarVivienda(Vivienda vi) {
        NeodatisMunicipioImpl muniDAO=new NeodatisMunicipioImpl();
        if (ConsultarVivienda(vi.getCodigo()) == null&&muniDAO.ConsultarMunicipio(vi.getNombremunicipio())!=null) {
            bd.store(vi);
            bd.commit();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
            System.out.printf("Vivienda: %s Insertada %n", vi.getCodigo());
            return 0;
        } else {
            System.out.printf("Vivienda: %s Ya existe %n", vi.getCodigo());
            return 1;
        }
    }

    @Override
    public int EliminarVivienda(int codigo) {
        int valor = 1;

        IQuery queryAux = new CriteriaQuery(Persona.class, Where.equal("codvivienda", codigo));
        Objects<Persona> objetosAux = bd.getObjects(queryAux);
        if (!objetosAux.hasNext()) {

            IQuery query = new CriteriaQuery(Vivienda.class, Where.equal("codigo", codigo));
            Objects<Vivienda> objetos = bd.getObjects(query);
            try {
                Vivienda vi = (Vivienda) objetos.getFirst();
                bd.delete(vi);
                bd.commit();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
                valor = 0;
                System.out.printf("Vivienda: %s eliminada %n", codigo);
            } catch (IndexOutOfBoundsException i) {
                valor = -1;
                System.out.printf("Vivienda a eliminar: %s No existe%n", codigo);
            }
        }
        return valor;
    }

    @Override
    public int ModificarVivienda(int codigo, Vivienda vivnueva) {
        int valor = 1;
        IQuery query = new CriteriaQuery(Vivienda.class, Where.equal("codigo", codigo));
        Objects<Vivienda> objetos = bd.getObjects(query);
        try {
            Vivienda vi = (Vivienda) objetos.getFirst();
            vi.setDireccion(vivnueva.getDireccion());
            vi.setNombremunicipio(vivnueva.getNombremunicipio());
            vi.setAnyo(vivnueva.getAnyo());
            vi.setNumeropersonas(vivnueva.getNumeropersonas());
            vi.setMetroscuadrados(vivnueva.getMetroscuadrados());
            vi.setImpuesto(vivnueva.getImpuesto());
            bd.store(vi); 
            valor = 0;
            bd.commit();
            new NeodatisMunicipioImpl().ActualizarMunicipio();
        } catch (IndexOutOfBoundsException i) {
            valor = -1;
            System.out.printf("Vivienda: %s No existe%n", codigo);
        }

        return valor;
    }

    @Override
    public Vivienda ConsultarVivienda(int codigo) {
        IQuery query = new CriteriaQuery(Vivienda.class, Where.equal("codigo", codigo));
        Objects<Vivienda> objetos = bd.getObjects(query);
        Vivienda vi = null;
        if (objetos.size()>0) {
            try {
                vi = (Vivienda) objetos.getFirst();
            } catch (Exception i) {
                System.out.printf("Vivienda: %d No existe%n", codigo);
                vi = null;
            }
        }
        return vi;
    }

    @Override
    public void ActualizarVivienda() {
        NeodatisPersonaImpl p=new NeodatisPersonaImpl();
        ArrayList<Vivienda>vivi=TodasViviendas();
        Persona pers=new Persona();
        Vivienda vi= new Vivienda();
        double imp;
        int hab;
        for(int i=0;i<vivi.size();i++){
            imp=0;
            hab=0;
            vi=vivi.get(i);
            ArrayList<Persona> personas=p.TodosPersonasVivienda(vivi.get(i).getCodigo());
            
            hab=personas.size();
            imp=vivi.get(i).getImpuesto();
            vi.setNumeropersonas(hab);
            vi.setImpuesto(imp);
            ModificarVivienda(vi.getCodigo(), vi);
        }
    }

    @Override
    public ArrayList<Vivienda> TodasViviendas() {
        ArrayList<Vivienda> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Vivienda.class);
        Objects<Vivienda> objetos = bd.getObjects(query);
        Vivienda vi = new Vivienda();
        while (objetos.hasNext()) {
            vi = objetos.next();
            ret.add(vi);
        }
        return ret;
    }

    @Override
    public ArrayList<Vivienda> ViviendasDeunMunicipio(String nombre) {
        ArrayList<Vivienda> ret = new ArrayList();
        IQuery query = new CriteriaQuery(Vivienda.class, Where.equal("nombremunicipio", nombre));
        Objects<Vivienda> objetos = bd.getObjects(query);
        Vivienda vi = new Vivienda();
        while (objetos.hasNext()) {
            vi = objetos.next();
            ret.add(vi);
        }
        return ret;
    }
    
}
