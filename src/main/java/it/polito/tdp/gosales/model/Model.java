package it.polito.tdp.gosales.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private GOsalesDAO dao;
	private List<Retailers> allRetailers;
	private List<String> allCountries;
	private List<Retailers> nationRetailers;
	private Map<Integer, Retailers> retailersIdMap;
	private Graph<Retailers, DefaultWeightedEdge> grafo;
	private List<Coppie> archi;
	private List<Products> prodotti;
	private Map<Integer, Products> prodottiIdMap;
	private int connessi;
	
	
	
	public Model() {
		this.dao = new GOsalesDAO();
		this.allRetailers = dao.getAllRetailers();
		this.allCountries = dao.getNazioni();
		this.retailersIdMap = new TreeMap<>();
	}
	
	public List<String> getNazioni(){
		return this.allCountries;
	}
	
	public void creaGrafo(String nazione, int numMin, int anno) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.nationRetailers = dao.getNationRetailers(nazione);
		for (Retailers r: nationRetailers) 
			retailersIdMap.put(r.getCode(), r);
		Graphs.addAllVertices(this.grafo, this.nationRetailers);
		
		this.archi = dao.getArchi(retailersIdMap, nazione, numMin, anno);
		for (Coppie c : archi) {
			Graphs.addEdge(this.grafo, c.getRet1(), c.getRet2(), c.getPeso());
		}
	}
	
	public int calcolaConnessa(Retailers r) {
		ConnectivityInspector<Retailers, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		this.connessi = ci.connectedSetOf(r).size();
		return this.connessi;
	}
	
	public int pesoConnessa(Retailers r) {
		int peso = 0;
		ConnectivityInspector<Retailers, DefaultWeightedEdge> ci = new ConnectivityInspector<>(this.grafo);
		for (Retailers rr : ci.connectedSetOf(r)) {
			for (Coppie c : this.archi) {
				if (rr.equals(c.getRet1()))
					peso+=c.getPeso();
			}
		}
		return peso;
	}
	
	public int getNumV() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumE() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Retailers> getVertici(){
		List<Retailers> copia = this.nationRetailers;
		Collections.sort(copia);
		return copia;
	}
	
	public List<Coppie> getArchi(){
		List<Coppie> res = this.archi;
		Collections.sort(res);
		return res;
	}
	
	public List<Products> getProdottixR(int anno, Retailers r){
		this.prodottiIdMap = new TreeMap<>();
		this.prodotti = dao.getAllProducts();
		for (Products p : this.prodotti)
			this.prodottiIdMap.put(p.getNumber(), p);
		
		List<Products> result = dao.getProductForRetailer(prodottiIdMap, anno, r);
		for (Products p : result)
			System.out.println(p+"\n");
		return result;
	}
	
	public SimulationResult eseguiSimulazione(Products p, int q, int n, Retailers r, int anno) {
		//creo simulatore e coda degli eventi
		Simulatore sim = new Simulatore(r, anno, p, q, n, this.connessi);
		sim.popolaCoda();
		
		//eseguo la simulazione
		sim.run();
		
		//leggo e restituisco il risulato della simulazione
		return sim.getSimulationResult();
	}
	
}
