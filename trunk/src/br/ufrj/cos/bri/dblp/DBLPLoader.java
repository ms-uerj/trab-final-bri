package br.ufrj.cos.bri.dblp;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.dblp.model.RegistroDBLP;
import br.ufrj.cos.bri.model.InteressadoRegistro;
import br.ufrj.cos.bri.model.Registro;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;
import br.ufrj.cos.bri.util.xml.LeitorXML;

public class DBLPLoader implements InteressadoRegistro {
	private Vector<String> xml_docs = null;
	private MysqlConnector db = null;
	private int contador=0;
		
	public DBLPLoader() {
		System.setProperty("entityExpansionLimit", "10000000");
		xml_docs = new Vector<String>();
		xml_docs.add("input/dblp.xml");

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public void populaBase() {
		for(int i=0;i < xml_docs.size();i++) {
			LeitorXML leitor = new LeitorXML(xml_docs.elementAt(i));
			leitor.cadastrarInteressado(this);
			leitor.executarLeitura();
		}
	}
	
	public void receberRegistro(Registro registro) {
		gravaRegistro((RegistroDBLP)registro);
	}
	
	public void terminoLeitura() {}
	
	private void gravaRegistro(RegistroDBLP registro) {
		
		Vector<Integer> idsAuthor = gravaRegistroAuthor(registro);
		
		if(registro.getDataType() == RegistroDBLP.DBLP_DATA.INPROCEEDINGS) {
			int id = gravaRegistroInProceedings(registro);
			gravaRegistroInProceedingsAuthor(id, idsAuthor);
		}
		
		//System.out.println(query);
		
		System.out.print("Registros: "+ ++contador+"\r");
		
		
		
	}
	
	private int gravaRegistroInProceedings(RegistroDBLP registro) {
		
		String query = new String("SELECT id FROM proceedings WHERE title="+"\'"+registro.getTitle()+"\'");
		
		ResultSet set = db.query(query);
		
		int idproceedings=0;
		try {
			if(set.next()) {
				idproceedings = set.getInt("id");
			}
			else {
				query = new String("INSERT INTO proceedings (title, year) VALUES ("+
						"\'"+registro.getTitle()+"\',\'"+registro.getYear()+"\');");
				
				db.exec(query);
				
				query = new String("SELECT LAST_INSERT_ID() as id");
				
				ResultSet newset = db.query(query);
				newset.next();
				idproceedings = newset.getInt("id");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		query = new String("INSERT INTO inproceedings (id_proceedings, title, booktitle, year, link) VALUES ("+
				"\'"+idproceedings+"\'"+",\'"+registro.getTitle()+"\',\'"+registro.getBookTitle()+"\',\'"+registro.getYear()+"\',\'"+registro.getLink()+"\');");
		
		db.exec(query);
		
		query = new String("SELECT LAST_INSERT_ID() as id");
		
		ResultSet setInProceedingsID = db.query(query);
		
		int idinproceedings=0;
		try {
			setInProceedingsID.next();
			idinproceedings = setInProceedingsID.getInt("id");
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return idinproceedings;
	}
	
	private Vector<Integer> gravaRegistroAuthor(RegistroDBLP registro) {
		Vector<Integer> ids = new Vector<Integer>();
		Vector<String> authors = registro.getAuthors();
		
		for(String author : authors) {
			String query = new String("SELECT id FROM author WHERE name="+"\'"+author+"\'");
			
			ResultSet set = db.query(query);
			
			int idauthor=0;
			try {
				if(set.next()) {
					idauthor = set.getInt("id");
					ids.add(Integer.valueOf(idauthor));
				}
				else {
					query = new String("INSERT INTO author (name) VALUES ("+
							"\'"+author+"\');");
					
					db.exec(query);
					
					query = new String("SELECT LAST_INSERT_ID() as id");
					
					ResultSet newset = db.query(query);
					newset.next();
					idauthor = newset.getInt("id");
					ids.add(Integer.valueOf(idauthor));
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return ids;
	}
	
	private void gravaRegistroInProceedingsAuthor(int idinproceedings, Vector<Integer> idsauthor) {
		for(int i=0;i < idsauthor.size();i++) {
			String query = new String("INSERT INTO inproceedings_author (id_inproceedings,id_author,position_author) VALUES ("+
					"\'"+idinproceedings+"\'"+",\'"+idsauthor.elementAt(i).toString()+"\',\'"+(i+1)+"\');");
			
			db.exec(query);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Inicializando extração e carga ...");
		DBLPLoader loader = new DBLPLoader();
		loader.populaBase();
		System.out.println("Fim da carga.");
	}
	
}

