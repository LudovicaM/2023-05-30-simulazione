package it.polito.tdp.gosales;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.gosales.model.Coppie;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizzaComponente;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;

    @FXML
    private ComboBox<Products> cmbProdotto;

    @FXML
    private ComboBox<Retailers> cmbRivenditore;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextField txtN;

    @FXML
    private TextField txtNProdotti;

    @FXML
    private TextField txtQ;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextArea txtVertici;

    @FXML
    void doAnalizzaComponente(ActionEvent event) {
    	Retailers r = cmbRivenditore.getValue();
    	
    	txtResult.appendText("La componente connessa di "+ r+ " ha dimensione "+model.calcolaConnessa(r)+"\n");
    	txtResult.appendText("Il peso totale degli archi della componente connessa è "+model.pesoConnessa(r)+"\n");
    	
    	//creo già la tendina per la simulazione
    	//cmbProdotto.disableProperty() = false;
    	int anno = cmbAnno.getValue();
    	cmbProdotto.getItems().addAll(model.getProdottixR(anno, r));
    	txtN.setDisable(false);
    	txtQ.setDisable(false);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	int anno = cmbAnno.getValue();
    	String nazione = cmbNazione.getValue();
    	String NS = txtNProdotti.getText();
    	if (NS == "") {
    		txtResult.setText("Inserire un numero minimo di prodotti\n");
    		return;
    	}
    	try {
    		int numMin = Integer.parseInt(NS);
    		model.creaGrafo(nazione, numMin, anno);
    		txtResult.setText("Grafo creato\n");
    		txtResult.appendText("Numero vertici: "+model.getNumV()+"\n");
    		txtResult.appendText("Numero archi: "+model.getNumE()+"\n");
    		
    		List<Retailers> vertici = model.getVertici();
    		for (Retailers r : vertici)
    			txtVertici.appendText(r+"\n");
    		
    		List<Coppie> archi = model.getArchi();
    		for (Coppie c : archi)
    			txtArchi.appendText(c+"\n");
    		
    		cmbRivenditore.getItems().addAll(vertici);
    		
    	}catch (NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero nel campo numero minimo di prodotti\n");
    		return;
    	}
    		
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	String nS = txtN.getText();
    	String qS = txtQ.getText();
    	if (nS== "" || qS == "") {
    		txtResult.setText("Inserire due valori in Q ed N");
    		return;
    	}
    	try {
    		int N = Integer.parseInt(nS);
    		int Q = Integer.parseInt(qS);
    		Products p = cmbProdotto.getValue();
    		if (p == null) {
    			txtResult.setText("Selezionare un prodotto dalla tendina");
    			return;
    		}
    		int anno = cmbAnno.getValue();
    		Retailers r = cmbRivenditore.getValue();
    		
    		txtResult.setText("Simulazione finita. Il risultato è:\n"+ model.eseguiSimulazione(p, Q, N, r, anno));
    		
    	}catch (NumberFormatException e) {
    		txtResult.setText("Inserire dei valori numerici per i campi Q e N");
    		return;
    	}
    }

    @FXML
    void initialize() {
        assert btnAnalizzaComponente != null : "fx:id=\"btnAnalizzaComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbRivenditore != null : "fx:id=\"cmbRivenditore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNProdotti != null : "fx:id=\"txtNProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQ != null : "fx:id=\"txtQ\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtVertici != null : "fx:id=\"txtVertici\" was not injected: check your FXML file 'Scene.fxml'.";

        for (int anno = 2015; anno<2019; anno++)
        	cmbAnno.getItems().add(anno);
        
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	cmbNazione.getItems().addAll(model.getNazioni());
    }

}
