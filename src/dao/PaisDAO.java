package dao;

import java.util.ArrayList;

import datos.Pais;

public interface PaisDAO {

	public int InsertarPais(Pais pais);

	public int EliminarPais(int codigo);

	public int ModificarPais(int codigo, String nuevonombre);
    //recibe el c�digo de pas a modificar, se le cambiarel nombre actual por nuevonombre.

	public Pais ConsultarPais(int codigo);
        public Pais ConsultarPais(String nombre);

	public ArrayList <Pais> TodosPaises();

}
