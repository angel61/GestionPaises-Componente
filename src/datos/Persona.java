package datos;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Persona implements Serializable {
	private String nombre;
	private int edad;
	private int codvivienda; 

	
	public Persona() {
		super();		
	}

	public Persona(String nombre, int edad, int codvivienda) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.codvivienda = codvivienda;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public int getCodvivienda() {
		return codvivienda;
	}

	public void setCodvivienda(int codvivienda) {
		this.codvivienda = codvivienda;
	}

	@Override
	public String toString() {
		return "Persona [nombre=" + nombre + ", edad=" + edad + ", codvivienda=" + codvivienda + "]";
		
		
	}

	
}
