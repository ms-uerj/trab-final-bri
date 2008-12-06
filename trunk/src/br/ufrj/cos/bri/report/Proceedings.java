package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class Proceedings {
	private MysqlConnector db = null;
	

	public Proceedings() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public Vector<String> getAll(String start) {
		
		Vector<String> proceedings = new Vector<String>();
		
		String query = new String("SELECT title FROM PROCEEDINGS WHERE title like '"+start+"' ORDER BY title ASC");
		
		ResultSet set = db.query(query);
		
		try {
			while(set.next()) {
				proceedings.add(set.getString("title"));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return proceedings;
	}

}
