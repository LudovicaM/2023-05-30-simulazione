package it.polito.tdp.gosales.model;

import java.time.LocalDate;
import java.util.PriorityQueue;

import it.polito.tdp.gosales.dao.GOsalesDAO;
import it.polito.tdp.gosales.model.Event.EventType;

public class Simulatore {
	
	//parametri di ingresso
	private int anno;
	private int N;
	
	
	//parametri
	private int avgD;
	private int avgQ;
	private GOsalesDAO dao;
	private double costoUnitario;
	private double prezzoUnitario;
	private double threshold;
	
	//variabili di uscita
	private int clientiTot;
	private int clientiSoddisfatti;
	private double costo;
	private double ricavo;
	
	//stato del mondo
	private int Q;
	
	//coda degli eventi
	PriorityQueue<Event> queue;
	
	public Simulatore(Retailers r, int anno, Products p, int n, int q, int nConnessi) {
		
		this.dao = new GOsalesDAO();
		this.anno = anno;
		N = n;
		Q = q;
		
		avgD = this.dao.getAvgD(r, p, anno);
		avgQ = this.dao.getAvgQ(r, p, anno);
		this.costoUnitario = p.isUnit_cost();
		this.prezzoUnitario = p.isUnit_price();
		
		this.threshold = Math.min(0.2 +0.1*nConnessi, 0.5);		
	}
	
	//popolo la coda degli eventi
	public void popolaCoda() {
		this.queue = new PriorityQueue<Event>();
		
		//eventi rifornimento
		for (int i=1; i<=12; i++) {
			this.queue.add(new Event(EventType.RIFORNIMENTO, LocalDate.of(anno, i, 1)));
		}
		
		//eventi vendita
		LocalDate data = LocalDate.of(anno, 1, 15);
		while (data.isBefore(LocalDate.of(anno, 12, 31))) {
			this.queue.add(new Event(EventType.VENDITA, data));
			data = data.plusDays(avgD);
		}
	}
	
	public void run() {
		this.clientiSoddisfatti = 0;
		this.clientiTot = 0;
		this.costo = 0;
		this.ricavo = 0;
		
		while (!queue.isEmpty()) {
			Event e = queue.poll();
			
			switch(e.getType()) {
			case RIFORNIMENTO:
				double prob = Math.random();
				if (prob <= this.threshold) {
					Q += 0.8*N;
					this.costo = this.costoUnitario*0.8*N;
				}else {
					Q += N;
					this.costo = this.costoUnitario*N;
				}
				break;
			case VENDITA:
				this.clientiTot++;
				if (Q >= avgQ) {
					this.clientiSoddisfatti++;
					this.ricavo += this.prezzoUnitario*avgQ;
					Q -= avgQ;
				}else if (Q >= 0.9*avgQ) {				
					this.clientiSoddisfatti++;
					this.ricavo += this.prezzoUnitario*Q;
					Q = 0;
				}else {
					this.ricavo += this.prezzoUnitario*Q;
					Q = 0;
				}
				break;
			default:
				break;
			}
		}
	}
	
	public SimulationResult getSimulationResult() {
		return new SimulationResult(((double)this.clientiSoddisfatti/(double)this.clientiTot)*100, this.costo,
								this.ricavo, this.ricavo-this.costo);
		
	}

}
