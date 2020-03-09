package dao;

import java.util.ArrayList;

import datos.Municipio;
import datos.Pais;

public interface MunicipioDAO {

    public int InsertaMunicipio(Municipio muni);
    public int EliminarMunicipio(String nombre);
    public int ModificarMunicipio(String nombre, Municipio muninuevo);
    //recibe el nombre de municipio a modificar se cambiarn los datos del municipio por muninuevo (menos el nombre).
   
    public Municipio ConsultarMunicipio(String nombre);
    

    public void ActualizarMunicipio();//sumaimpuestos y numerohabitantes

    public ArrayList <Municipio> TodosMunicipios();
    
    public ArrayList <Municipio> MunicipiosdeunPais(int codpais);    
    public ArrayList <Municipio> MunicipiosdeunPais(String nombrepais);

}
