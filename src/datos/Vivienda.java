package datos;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Vivienda implements Serializable {
	private int codigo;
	private String nombremunicipio;
	private String direccion;
	private double metroscuadrados;
	private int anyo; // a�o de construccion
	private double impuesto;// impuesto a pagar por la vivienda
	private int numeropersonas;// personas que viven en la vivienda

	public Vivienda() {
		super();

	}

	
	public Vivienda(int codigo, String nombremunicipio, String direccion, double metroscuadrados, int anyo,
			double impuesto, int numeropersonas) {
		super();
		this.codigo = codigo;
		this.nombremunicipio = nombremunicipio;
		this.direccion = direccion;
		this.metroscuadrados = metroscuadrados;
		this.anyo = anyo;
		this.impuesto = impuesto;
		this.numeropersonas = numeropersonas;
	}

		
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
	public String getNombremunicipio() {
		return nombremunicipio;
	}


	public void setNombremunicipio(String nombremunicipio) {
		this.nombremunicipio = nombremunicipio;
	}


	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public double getMetroscuadrados() {
		return metroscuadrados;
	}

	public void setMetroscuadrados(double metroscuadrados) {
		this.metroscuadrados = metroscuadrados;
	}

	public int getAnyo() {
		return anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	public int getNumeropersonas() {
		return numeropersonas;
	}

	public void setNumeropersonas(int numeropersonas) {
		this.numeropersonas = numeropersonas;
	}

	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}

	/* 
	*  A DESAROLLAR POR EL ALUMNO 
	*/

	// A DESAROLLAR POR EL ALUMNO
	public double getImpuesto() {
            int impMetros=0;
		if(metroscuadrados<50){
                    impMetros=100;
                }else if(metroscuadrados<80){
                    impMetros=180;
                }else if(metroscuadrados<120){
                    impMetros=270;
                }else{
                    impMetros=300;
                }
                impuesto = impMetros + 0.1 * metroscuadrados;
		return impuesto;

	}

	@Override
	public String toString() {
		return 
				"Vivienda [codigo=" + codigo + ", municipio=" + nombremunicipio + ", "
				+ "direccion=" + direccion
				+ ", metroscuadrados=" + metroscuadrados + ", anyo=" + anyo + ", impuesto=" + impuesto
				+ ", numeropersonas=" + numeropersonas + "]";	
	
	}
	
	
	
}
