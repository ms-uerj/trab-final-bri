package br.ufrj.cos.bri;

import java.sql.ResultSet;

import br.ufrj.cos.bri.dblp.DBLPLoader;
import br.ufrj.cos.bri.dblp.PaperContentLoader;
import br.ufrj.cos.bri.excel.QualisReader;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class Main {
	
	public static void main(String[] args){
		
		try{
			MysqlConnector mysqlCon = new MysqlConnector();
			mysqlCon.connect();
			
			System.out.println("Inicializando extra��o e carga do XML DBLP...");
			DBLPLoader loader = new DBLPLoader();
			loader.populaBase();
			System.out.println("Fim da carga.");
			
			System.out.println("Inicializando extra��o e carga da planilha Qualis...");
			QualisReader qualisReader = new QualisReader();
			qualisReader.loadDatabase();
			System.out.println("Fim da carga da planilha Qualis.");
			
			System.out.println("Inicializando extra��o dos pdfs dos artigos e carga no banco DBLP...");
	
			PaperContentLoader paperContentLoader = new PaperContentLoader();
			paperContentLoader.loadPaperContents();
				
			System.out.println("Fim da extra��o de pdfs");
			
			System.out.println("Realizando associa��o de confer�ncias com n�vel qualis");
			
			ResultSet result = mysqlCon.query("SELECT id,title FROM PROCEEDINGS");
			
			while(result.next()){
				int id = result.getInt(1);
				String ProceedingsName = result.getString(2);
				
				String qualisLevel = qualisReader.getQualis(ProceedingsName);
				
				mysqlCon.exec("UPDATE PROCEEDINGS SET qualis='"+qualisLevel+"' WHERE id="+id);
				
			}
			
			System.out.println("Fim da associa��o de confer�ncias com n�vel qualis");
		
		}catch(Exception e){
		}
	}
	
}
