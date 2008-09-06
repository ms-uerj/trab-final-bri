package br.ufrj.cos.bri.citeseer;

import java.util.Vector;

import br.ufrj.cos.bri.citeseer.model.RegistroCiteSeer;
import br.ufrj.cos.bri.model.InteressadoRegistro;
import br.ufrj.cos.bri.model.Registro;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;
import br.ufrj.cos.bri.util.xml.CiteSeerXMLReader;

public class CiteSeerLoader extends Thread implements InteressadoRegistro{
	private Vector<String> xml_docs = null;
	private MysqlConnector db = null;
		
	public CiteSeerLoader(int fileRangeStart, int fileRangeEnd) {
		System.setProperty("entityExpansionLimit", "10000");
		xml_docs = new Vector<String>();
		
		for (int i = fileRangeStart; i <= fileRangeEnd; i++) {
			xml_docs.add("input/oai_citeseer/oai_citeseer"+i+".dump");
		}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public void populaBase() {
		for(int i=0;i < xml_docs.size();i++) {
			CiteSeerXMLReader leitor = new CiteSeerXMLReader(xml_docs.elementAt(i));
			leitor.cadastrarInteressado(this);
			leitor.executarLeitura();
		}
	}
	
	public void receberRegistro(Registro registro) {
		gravaRegistro((RegistroCiteSeer)registro);
	}
	
	public void terminoLeitura() {}
	
	private void gravaRegistro(RegistroCiteSeer registro) {
		
		gravaRegistroArticle(registro);
			
	}
	
	
	private void gravaRegistroArticle(RegistroCiteSeer registro) {
		
		String query = new String("INSERT INTO PAPER (id, title) VALUES ("+
				"\'"+registro.getId()+"\',\'"+registro.getTitle()+"\');");
		
		db.exec(query);

		gravaRegistroArticleReferences(registro.getId(), registro.getReferences());
		gravaRegistroArticleReferencedBy(registro.getId(), registro.getReferencedBy());
		
	}
	
	
	private void gravaRegistroArticleReferences(String articleId, Vector<String> referencesId) {
		for (String referenceId : referencesId) {
			String query = new String("INSERT INTO PAPER_REFERENCES_TEMP (id_article1,id_article2) VALUES ("+
					"\'"+articleId+"\'"+",\'"+referenceId+"');");
			
			db.exec(query);
		}
	}
	
	private void gravaRegistroArticleReferencedBy(String articleId, Vector<String> referencesId) {
		for (String referenceId : referencesId) {
			String query = new String("INSERT INTO PAPER_IS_REFERENCED_BY_TEMP (id_article1,id_article2) VALUES ("+
					"\'"+articleId+"\'"+",\'"+referenceId+"');");
			
			db.exec(query);
		}
	}

	public void run() {
		long startTime = System.currentTimeMillis();
		
		populaBase();
		
		long finishTime = System.currentTimeMillis();
		
		System.out.println("Tempo decorrido: "+(finishTime-startTime)/1000/60+" minuto(s)");
	}
	
}

