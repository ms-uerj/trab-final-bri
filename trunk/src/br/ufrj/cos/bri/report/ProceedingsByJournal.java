package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.ufrj.cos.bri.model.Proceedings;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class ProceedingsByJournal {
	private MysqlConnector db = null;

	public ProceedingsByJournal() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
//	public List<Proceedings> listRankedProceedingsCitedByJournal(String journal) {
//		String query = new String("SELECT a.title FROM article a WHERE a.journal=\'"+journal+"\'");
//		Map<Integer, Proceedings> proceedingsById = new HashMap<Integer, Proceedings>();
//		
//		ResultSet set = db.query(query);
//		
//		int idauthor=0;
//		try {
//			while(set.next()) {
//				String articleTitle = set.getString("title");
//				//busca na base do citeseer o artigo
//				query = new String("SELECT a.id FROM paper a WHERE a.title=\'"+articleTitle+"\'");
//				ResultSet article = db.query(query);
//				
//				if(article.next()) {
//					int idArticle = article.getInt("id");
//					query = new String("SELECT ref.id_article2, p.title FROM paper_references_temp ref, paper p WHERE p.id=ref.id_article2 and ref.id_article1="+idArticle);
//					//pode ser um periódico, inproceedings, incollection 
//					ResultSet PapersCitedByJournal = db.query(query);
//					
//					while(PapersCitedByJournal.next()){
//						String paperTitle = PapersCitedByJournal.getString("title");
//						
//						
//						query = new String("SELECT p.id, p.title FROM inproceedings inp, proceedings p WHERE inp.id_proceedings=p.id and inp.title='"+paperTitle+"'");
//						ResultSet proceedingsResult = db.query(query);
//						if(proceedingsResult.next()){
//							int proceedingsId = proceedingsResult.getInt("id");
//							String proceedingsTitle = proceedingsResult.getString("title");
//							
//							if(proceedingsById.containsKey(proceedingsId)){
//								Proceedings proceedings = proceedingsById.get(proceedingsId);
//								proceedings.incrementCcount();
//							}
//							else{
//								Proceedings proceedings = new Proceedings(proceedingsId, proceedingsTitle, "");
//								proceedingsById.put(proceedingsId, proceedings);
//							}
//						}
//					}
//					
//				}
//			}
//		} catch(SQLException e) {
//			e.printStackTrace();
//		}
//		
//		List proceedingsList = new ArrayList<Proceedings>(proceedingsById.values());
//		Collections.sort(proceedingsList);
//		return proceedingsList;
//	}

	
	public List<Proceedings> listRankedProceedingsCitedByJournal(String journal) {
		String query = new String("SELECT a.title FROM article a WHERE a.journal=\'"+journal+"\'");
		Map<Integer, Proceedings> proceedingsById = new HashMap<Integer, Proceedings>();
		
		ResultSet set = db.query(query);
		
		int idauthor=0;
		try {
			while(set.next()) {
				String articleTitle = set.getString("title");
				//busca na base do citeseer o artigo
				query = new String("SELECT a.id FROM paper a WHERE a.title=\'"+articleTitle+"\'");
				ResultSet article = db.query(query);
				
				if(article.next()) {
					int idArticle = article.getInt("id");
					query = new String("SELECT id_article2 FROM paper_references_temp WHERE id_article1="+idArticle);
					//pode ser um periódico, inproceedings, incollection 
					ResultSet PaperIdsCitedByJournal = db.query(query);
					
					while(PaperIdsCitedByJournal.next()){
						int paperId = PaperIdsCitedByJournal.getInt("id_article2");
						
						query = new String("SELECT title FROM paper WHERE id="+paperId);
						ResultSet result = db.query(query);
						result.next();
						String paperTitle = result.getString("title");
						
						query = new String("SELECT id_proceedings FROM inproceedings WHERE title='"+paperTitle+"'");
						ResultSet proceedingsResult = db.query(query);
						if(proceedingsResult.next()){
							int proceedingsId = proceedingsResult.getInt("id_proceedings");
							query = new String("SELECT title FROM proceedings WHERE id="+proceedingsId);
							result = db.query(query);
							result.next();
							
							String proceedingsTitle = result.getString("title");
							
							if(proceedingsById.containsKey(proceedingsId)){
								Proceedings proceedings = proceedingsById.get(proceedingsId);
								proceedings.incrementCcount();
							}
							else{
								Proceedings proceedings = new Proceedings(proceedingsId, proceedingsTitle, "");
								proceedingsById.put(proceedingsId, proceedings);
							}
						}
					}
					
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		List proceedingsList = new ArrayList<Proceedings>(proceedingsById.values());
		Collections.sort(proceedingsList);
		return proceedingsList;
	}

	
	
	public static void main(String[] args) {
		ProceedingsByJournal teste = new ProceedingsByJournal();
		List list = teste.listRankedProceedingsCitedByJournal("Computer Communications");
		System.out.println(list.size());


	}
	
	
}
