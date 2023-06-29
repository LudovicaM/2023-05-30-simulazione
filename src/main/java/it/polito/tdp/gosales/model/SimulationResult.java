package it.polito.tdp.gosales.model;

public class SimulationResult {
	
	//attributi dell'output
	
	private double soddisfazione;
	private double costo;
	private double ricavo;
	private double profitto;
	
	public SimulationResult(double soddisfazione, double costo, double ricavo, double profitto) {
		super();
		this.soddisfazione = soddisfazione;
		this.costo = costo;
		this.ricavo = ricavo;
		this.profitto = profitto;
	}

	public double getSoddisfazione() {
		return soddisfazione;
	}

	public void setSoddisfazione(double soddisfazione) {
		this.soddisfazione = soddisfazione;
	}

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public double getRicavo() {
		return ricavo;
	}

	public void setRicavo(double ricavo) {
		this.ricavo = ricavo;
	}

	public double getProfitto() {
		return profitto;
	}

	public void setProfitto(double profitto) {
		this.profitto = profitto;
	}

	@Override
	public String toString() {
		return "SimulationResult [soddisfazione=" + soddisfazione + ", costo=" + costo + ", ricavo=" + ricavo
				+ ", profitto=" + profitto + "]";
	}
	
	

}
