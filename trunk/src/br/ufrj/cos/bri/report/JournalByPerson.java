package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class JournalByPerson {
	private MysqlConnector db = null;

	public JournalByPerson() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public Vector<String> listJournals(String name) {
		Vector<String> journals = new Vector<String>();
		
		String query = new String("SELECT id FROM author WHERE name="+"\'"+name+"\'");
		
		ResultSet set = db.query(query);
		
		int idauthor=0;
		try {
			if(set.next()) {
				idauthor = set.getInt("id");
				query = new String("select count(*) as times, a.journal from article a, article_author b where a.id=b.id_article and b.id_author="+"\'"+idauthor+"\' group by a.journal order by a.journal asc");
				ResultSet journalsReg = db.query(query);
				
				while(journalsReg.next()) {
					String pname = "("+journalsReg.getString("times")+") "+journalsReg.getString("journal");
					journals.add(pname);
				}
			}
			else {
				System.out.println("Autor "+name+" não encontrado.");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return journals;
	}
}
