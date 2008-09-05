package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class Person {
	private MysqlConnector db = null;
	

	public Person() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public Vector<String> getAll(String start) {
		
		Vector<String> authors = new Vector<String>();
		
		String query = new String("SELECT name FROM author WHERE name like '"+start+"' ORDER BY name ASC");
		
		ResultSet set = db.query(query);
		
		try {
			while(set.next()) {
				authors.add(set.getString("name"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return authors;
	}

}
