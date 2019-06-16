package it.polito.tdp.artsmia.model;

public class Adiacenza {
	
	ArtObject o1;
	ArtObject o2;
	double peso;
	public Adiacenza(ArtObject o1, ArtObject o2, double peso) {
		super();
		this.o1 = o1;
		this.o2 = o2;
		this.peso = peso;
	}
	public ArtObject getO1() {
		return o1;
	}
	public void setO1(ArtObject o1) {
		this.o1 = o1;
	}
	public ArtObject getO2() {
		return o2;
	}
	public void setO2(ArtObject o2) {
		this.o2 = o2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	

}
