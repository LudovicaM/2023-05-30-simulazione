package it.polito.tdp.gosales.model;

public class Coppie implements Comparable<Coppie>{
	
	private Retailers ret1;
	private Retailers ret2;
	private int peso;
	
	public Coppie(Retailers ret1, Retailers ret2, int peso) {
		super();
		this.ret1 = ret1;
		this.ret2 = ret2;
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "Arco: " + ret1.getName() + " -- " + ret2.getName() + " [" + peso + "]";
	}

	public Retailers getRet1() {
		return ret1;
	}

	public void setRet1(Retailers ret1) {
		this.ret1 = ret1;
	}

	public Retailers getRet2() {
		return ret2;
	}

	public void setRet2(Retailers ret2) {
		this.ret2 = ret2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Coppie o) {
		// TODO Auto-generated method stub
		return this.peso-o.getPeso();
	}
	
	

}
