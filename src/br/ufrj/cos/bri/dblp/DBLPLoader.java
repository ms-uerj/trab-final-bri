package br.ufrj.cos.bri.dblp;

import java.sql.ResultSet;
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
				
		db = new MysqlConnector("bri", "rcepeda", "123456");
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
		
		String query = null;
		
		Vector<String> authors = registro.getAuthors();
		
		if(registro.getDataType() == RegistroDBLP.DBLP_DATA.INPROCEEDINGS) {
			query = new String("INSERT INTO proceedings (title, year) VALUES ("+
					"\'"+registro.getTitle()+"\',\'"+registro.getYear()+"\');");
			
			db.exec(query);
			
			query = new String("SELECT LAST_INSERT_ID()");
			
			ResultSet set = db.query(query);
			
			//int id = set.getInt("id");
			
			query = new String("INSERT INTO inproceedings (id_proceedings, title, booktitle, year, link) VALUES ("+
					"\'"+registro.getKey()+"\'"+",\'"+registro.getTitle()+"\',\'"+registro.getBookTitle()+"\',\'"+registro.getYear()+"\',\'"+registro.getLink()+"\');");
			
			db.exec(query);
		}
		
		
		//System.out.println(query);
		
		System.out.print("Registros: "+ ++contador+"\r");
		
		
		
	}
	
	public static void main(String[] args) {
		System.out.println("Inicializando extração e carga ...");
		DBLPLoader loader = new DBLPLoader();
		loader.populaBase();
		System.out.println("Fim da carga.");
	}
	
}

