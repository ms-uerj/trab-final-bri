package br.ufrj.cos.bri;

import br.ufrj.cos.bri.dblp.DBLPLoader;
import br.ufrj.cos.bri.dblp.Extractor;
import br.ufrj.cos.bri.excel.QualisReader;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;
import br.ufrj.cos.bri.util.text.TextPreprocessing;

import com.mysql.jdbc.ResultSet;

public class Main {
	
	public static void main(String[] args){
		
		try{
			MysqlConnector mysqlCon = new MysqlConnector("bri", "root", "");
			mysqlCon.connect();
			
			System.out.println("Inicializando extração e carga do XML DBLP...");
			DBLPLoader loader = new DBLPLoader();
			loader.populaBase();
			System.out.println("Fim da carga.");
			
			System.out.println("Inicializando extração dos pdfs dos artigos e carga no banco DBLP...");
			Extractor articleDocumentExtractor = new Extractor();
			
			ResultSet result = mysqlCon.query("SELECT id,link FROM INPROCEEDINGS");
			
			while(result.next()){
				int id = result.getInt(1);
				String link = result.getString(2);
				
				String documentText = articleDocumentExtractor.getDocument(link);
				
				if(documentText != null){
					String documentTextPreProc = TextPreprocessing.preProcessText(documentText);
					mysqlCon.exec("UPDATE INPROCEEDINGS SET content='"+documentTextPreProc+"' WHERE id="+id);
				}
				
			}
			
			result = mysqlCon.query("SELECT id,link FROM INCOLLECTION");
			
			while(result.next()){
				int id = result.getInt(1);
				String link = result.getString(2);
				
				String documentText = articleDocumentExtractor.getDocument(link);
				
				if(documentText != null){
					String documentTextPreProc = TextPreprocessing.preProcessText(documentText);
					mysqlCon.exec("UPDATE INCOLLECTION SET content='"+documentTextPreProc+"' WHERE id="+id);
				}
				
			}
			
			System.out.println("Fim da extração de pdfs");
			
			System.out.println("Realizando associação de conferências com nível qualis");
			
			result = mysqlCon.query("SELECT id,title FROM PROCEEDINGS");
			
			while(result.next()){
				int id = result.getInt(1);
				String ProceedingsName = result.getString(2);
				
				QualisReader qualisReader = new QualisReader();
				qualisReader.getQualis(ProceedingsName);
				
			}
		
		}catch(Exception e){
		}
	}
	
}
