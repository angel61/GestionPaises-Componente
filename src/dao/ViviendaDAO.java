package dao;

import java.util.ArrayList;

import datos.Vivienda;

public interface ViviendaDAO {

    public int InsertarVivienda(Vivienda vi);
    public int EliminarVivienda(int codigo);
    public int ModificarVivienda(int codigo, Vivienda vivnueva);
    //recibe el cdigo de vivienda a modificar se cambiarn los datos de la vivienda por vivnueva (menos el codigo).


    public Vivienda ConsultarVivienda (int codigo);
    public void ActualizarVivienda(); //nmero de personas
    

    public ArrayList <Vivienda> TodasViviendas();
    public ArrayList <Vivienda> ViviendasDeunMunicipio(String nombre);

}
