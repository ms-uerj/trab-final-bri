package br.ufrj.cos.bri.dblp;

import java.util.Vector;

import br.ufrj.cos.bri.dblp.model.RegistroDBLP;
import br.ufrj.cos.bri.model.InteressadoRegistro;
import br.ufrj.cos.bri.model.Registro;
import br.ufrj.cos.bri.model.RegistroBase;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;
import br.ufrj.cos.bri.util.xml.LeitorXML;

public class DBLPLoader implements InteressadoRegistro {
	private Vector<String> xml_docs = null;
	private MysqlConnector db = null;
	private int contador=0;
		
	public DBLPLoader() {
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
		/*  REFAZER
		String query = new String("INSERT INTO repository (recordnum, title, abstract, bodyXml) VALUES ("+
				registro.pegaRecordNum()+",\'"+registro.pegaTitulo()+"\',\'"+registro.pegaAbstract()+"\',\'"+registro.pegaCorpoXML()+"\');");
		
		
		//System.out.println(query);
		
		System.out.print("Registros: "+ ++contador+"\r");
		
		db.exec(query);
		*/
	}
	
	public static void main(String[] args) {
		DBLPLoader loader = new DBLPLoader();
		loader.populaBase();
	}
	
}

