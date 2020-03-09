package dao;

import java.util.ArrayList;

import datos.Persona;

public interface PersonaDAO {

	public int InsertarPersona(Persona persona);

	public int EliminarPersona(String nombre);

	public int ModificarPersona(String nombre, Persona nuevaper);
    //recibe el nombre de persona a modificar se cambiarn los datos de la persona por nuevaper (menos el nombre).

	public Persona ConsultarPersona(String nombre);

	public ArrayList<Persona> TodosPersonas();

	public ArrayList<Persona> TodosPersonasVivienda(int codigovivienda);

	public ArrayList<Persona> PersonasDeunPais(int codigopais);

	public ArrayList<Persona> PersonasDeunMunicipio(String nombremunicipio);

}
