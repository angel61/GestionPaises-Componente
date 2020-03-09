package datos;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Municipio implements Serializable{
	private String nombre;	
	private int codpais;
	private double tasapago; //tasa a pagar por las viviendas
	private double sumaimpuestos; //suma impuestos de las vivienda
	private int numerohabitantes; //numero de personas en el municipio

	
	public Municipio() {
		super();		
	}


	public Municipio(String nombre, int codpais, double tasapago, double sumaimpuestos, int numerohabitantes) {
		super();
		this.nombre = nombre;
		this.codpais = codpais;
		this.tasapago = tasapago;
		this.sumaimpuestos = sumaimpuestos;
		this.numerohabitantes = numerohabitantes;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	

	public int getCodpais() {
		return codpais;
	}


	public void setCodpais(int codpais) {
		this.codpais = codpais;
	}


	public double getTasapago() {
		return tasapago;
	}


	public void setTasapago(double tasapago) {
		this.tasapago = tasapago;
	}


	public double getSumaimpuestos() {
		return sumaimpuestos;
	}


	public void setSumaimpuestos(double sumaimpuestos) {
		this.sumaimpuestos = sumaimpuestos;
	}


	public int getNumerohabitantes() {
		return numerohabitantes;
	}


	public void setNumerohabitantes(int numerohabitantes) {
		this.numerohabitantes = numerohabitantes;
	}


	@Override
	public String toString() {
		return "Municipio [nombre=" + nombre + ", pais=" + codpais + ", tasapago=" + tasapago + ", sumaimpuestos="
				+ sumaimpuestos + ", numerohabitantes=" + numerohabitantes + "]";
	}
	
	

	
}