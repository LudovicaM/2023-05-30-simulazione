package it.polito.tdp.gosales.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.gosales.model.Coppie;
import it.polito.tdp.gosales.model.DailySale;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;

public class GOsalesDAO {
	
	
	/**
	 * Metodo per leggere la lista di tutti i rivenditori dal database
	 * @return
	 */

	public List<Retailers> getAllRetailers(){
		String query = "SELECT * from go_retailers";
		List<Retailers> result = new ArrayList<Retailers>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), 
						rs.getString("Country")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<Retailers> getNationRetailers(String nation){
		String query = "SELECT * "
				+ "FROM go_retailers "
				+ "WHERE Country = ?";
		
		List<Retailers> result = new ArrayList<Retailers>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, nation);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Retailers(rs.getInt("Retailer_code"), 
						rs.getString("Retailer_name"),
						rs.getString("Type"), nation));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
		
	
	
	public List<String> getNazioni(){
		String sql = "SELECT DISTINCT Country "
				+ "FROM go_retailers";
		
		List<String> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getString("Country"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	public List<Coppie> getArchi(Map<Integer, Retailers> retailersIdMap, String nazione, int numMin, int anno){
		String sql = "SELECT r1.Retailer_code AS rc1, r2.Retailer_code AS rc2, COUNT(DISTINCT g1.Product_number) AS conta "
				+ "FROM go_daily_sales g1, go_daily_sales g2, go_retailers r1, go_retailers r2 "
				+ "WHERE r1.Country = ? AND r1.Country = r2.Country AND r1.Retailer_code=g1.Retailer_code "
				+ "AND r2.Retailer_code = g2.Retailer_code AND g1.Product_number = g2.Product_number AND "
				+ "r1.Retailer_code< r2.Retailer_code "
				+ "AND YEAR(g1.Date) = ? AND YEAR(g1.Date)=YEAR(g2.Date) "
				+ "GROUP BY r1.Retailer_code, r2.Retailer_code "
				+ "HAVING conta >= ?";
		
		List<Coppie> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, nazione);
			st.setInt(2, anno);
			st.setInt(3, numMin);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Coppie(retailersIdMap.get(rs.getInt("rc1")),
						retailersIdMap.get(rs.getInt("rc2")), rs.getInt("conta")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	
	
	/**
	 * Metodo per leggere la lista di tutti i prodotti dal database
	 * @return
	 */
	public List<Products> getAllProducts(){
		String query = "SELECT * from go_products";
		List<Products> result = new ArrayList<Products>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Products(rs.getInt("Product_number"), 
						rs.getString("Product_line"), 
						rs.getString("Product_type"), 
						rs.getString("Product"), 
						rs.getString("Product_brand"), 
						rs.getString("Product_color"),
						rs.getDouble("Unit_cost"), 
						rs.getDouble("Unit_price")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	
	/**
	 * Metodo per leggere la lista di tutte le vendite nel database
	 * @return
	 */
	public List<DailySale> getAllSales(){
		String query = "SELECT * from go_daily_sales";
		List<DailySale> result = new ArrayList<DailySale>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new DailySale(rs.getInt("retailer_code"),
				rs.getInt("product_number"),
				rs.getInt("order_method_code"),
				rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
				rs.getInt("quantity"),
				rs.getDouble("unit_price"),
				rs.getDouble("unit_sale_price")  ));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public List<Products> getProductForRetailer(Map<Integer, Products> mapId, int anno, Retailers r){
		String sql = "SELECT DISTINCT Product_number "
				+ "FROM go_daily_sales "
				+ "WHERE Retailer_code = ? AND YEAR(Date) = ?";
		
		List<Products> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getCode());
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(mapId.get(rs.getInt("Product_number")));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//metodo che legge dal db il numero di giorni medio intercorso tra due vendite che il Retailer r
	//ha fatto del prodotto p in un anno
	public int getAvgD(Retailers r, Products p, int anno) {
		String sql = "SELECT Retailer_code, 12*30/COUNT(*) AS avgD "
				+ "FROM go_daily_sales "
				+ "WHERE Retailer_code = ? AND Product_number = ? AND YEAR(Date) = ?";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();
			
			if (rs.first()) {
				result = (int)(rs.getDouble("avgD"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	//quantità media di prodotto acquistato
	//ovvero venduta dal Retailer in un anno, per quanto riguarda p
	public int getAvgQ(Retailers r, Products p, int anno) {
		String sql = "SELECT Retailer_code, SUM(Quantity)/COUNT(*) AS avgQ "
				+ "FROM go_daily_sales "
				+ "WHERE Retailer_code = ? AND Product_number = ? AND YEAR(Date) = ?";
		int result = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, r.getCode());
			st.setInt(2, p.getNumber());
			st.setInt(3, anno);
			ResultSet rs = st.executeQuery();
			
			if (rs.first()) {
				result = (int)(rs.getDouble("avgQ"));
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
}
