package br.ufrj.cos.bri.dblp;

import java.sql.ResultSet;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;
import br.ufrj.cos.bri.util.text.TextPreprocessing;

public class PaperContentLoader {
	private MysqlConnector db = null;
	private int contador=0;
	private Extractor articleDocumentExtractor;
		
	public PaperContentLoader() {
		articleDocumentExtractor = new Extractor();
		db = new MysqlConnector("bri", "root", "");
		db.connect();
	}
	
	public void loadPaperContents() throws Exception{
		
		ResultSet result = db.query("SELECT id,link FROM INPROCEEDINGS");
		
		while(result.next()){
			int id = result.getInt(1);
			String link = result.getString(2);
			
			String documentText = articleDocumentExtractor.getDocument(link);
			
			if(documentText != null){
				String documentTextPreProc = TextPreprocessing.preProcessText(documentText);
				db.exec("UPDATE INPROCEEDINGS SET content='"+documentTextPreProc+"' WHERE id="+id);
			}
			
		}
		
		result = db.query("SELECT id,link FROM INCOLLECTION");
		
		while(result.next()){
			int id = result.getInt(1);
			String link = result.getString(2);
			
			String documentText = articleDocumentExtractor.getDocument(link);
			
			if(documentText != null){
				String documentTextPreProc = TextPreprocessing.preProcessText(documentText);
				db.exec("UPDATE INCOLLECTION SET content='"+documentTextPreProc+"' WHERE id="+id);
			}
			
		}

	}
	
}

