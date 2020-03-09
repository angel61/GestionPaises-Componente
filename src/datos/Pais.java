package datos;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Pais implements Serializable {
	private int codigo;
	private String nombrepais;
	
	public Pais() {
		super();		
	}


	public Pais(int codigo, String nombrepais) {
		super();
		this.codigo = codigo;
		this.nombrepais = nombrepais;		
	}

	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getNombrepais() {
		return nombrepais;
	}


	public void setNombrepais(String nombrepais) {
		this.nombrepais = nombrepais;
	}


	@Override
	public String toString() {
		return "Pais [codigo=" + codigo + ", nombrepais=" + nombrepais + "]";
	}
	

}
