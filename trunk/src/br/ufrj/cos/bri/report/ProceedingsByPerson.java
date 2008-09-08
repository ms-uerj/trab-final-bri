package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class ProceedingsByPerson {
	private MysqlConnector db = null;

	public ProceedingsByPerson() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public Vector<String> listProceedings(String name) {
		Vector<String> procs = new Vector<String>();
		
		String query = new String("SELECT id FROM author WHERE name="+"\'"+name+"\'");
		
		ResultSet set = db.query(query);
		
		int idauthor=0;
		try {
			if(set.next()) {
				idauthor = set.getInt("id");
				query = new String("SELECT distinct a.title FROM proceedings a, inproceedings b, inproceedings_author c WHERE a.id=b.id_proceedings AND b.id=c.id_inproceedings AND c.id_author="+"\'"+idauthor+"\' ORDER BY a.title ASC");
				ResultSet proceedings = db.query(query);
				
				while(proceedings.next()) {
					String pname = proceedings.getString("title");
					procs.add(pname);
				}
			}
			else {
				System.out.println("Author "+name+" não encontrado.");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return procs;
	}
	
	public void listProceedings(Vector<String> names) {
		
		String name = new String("(\'");
		
		for(int i=0;i < names.size();i++) {
			name += names.elementAt(i);
			name += "\'";
			
			if((i+1) < names.size()) {
				name += ",";
			}
		}
		
		name += ")";
		
		String query = new String("SELECT id FROM author WHERE name IN "+name);
		
		ResultSet set = db.query(query);
		
		try {
			String idauthor = new String("(");
			
			if(set.next()) {
				idauthor += set.getInt("id");
			}
			while(set.next()) {
				idauthor += ",";
				idauthor += set.getInt("id");
			}
			idauthor += ")";
					
			query = new String("SELECT distinct a.title FROM proceedings a, inproceedings b, inproceedings_author c WHERE a.id=b.id_proceedings AND b.id=c.id_inproceedings AND c.id_author IN "+idauthor);
			ResultSet proceedings = db.query(query);
				
			while(proceedings.next()) {
				String pname = proceedings.getString("title");
				System.out.println(pname);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
