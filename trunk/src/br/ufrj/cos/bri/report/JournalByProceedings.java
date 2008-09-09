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

import br.ufrj.cos.bri.model.Journal;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class JournalByProceedings {
	private MysqlConnector db = null;

	public JournalByProceedings() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public List<Journal> listRankedJournalCitedByProceedings(String proceedings) {
		String query = new String("SELECT id FROM proceedings WHERE title=\'"+proceedings+"\'");
		Map<String, Journal> journalById = new HashMap<String, Journal>();
		
		ResultSet result = db.query(query);
		
		try {
			if(result.next()){
				
				int proceedingsId = result.getInt("id");
				
				query = new String("SELECT title FROM inproceedings WHERE id_proceedings="+proceedingsId);
				ResultSet set = db.query(query);
				
				
				while(set.next()) {
					String inproceedingsTitle = set.getString("title");
					//busca na base do citeseer o artigo
					query = new String("SELECT a.id FROM paper a WHERE a.title=\'"+inproceedingsTitle+"\'");
					ResultSet paper = db.query(query);
					
					if(paper.next()) {
						int idPaper = paper.getInt("id");
						query = new String("SELECT id_article2 FROM paper_references_temp WHERE id_article1="+idPaper);
						//pode ser um periódico, inproceedings, incollection 
						ResultSet JournalIdsCitedByInproceedings = db.query(query);
						
						while(JournalIdsCitedByInproceedings.next()){
							int journalId = JournalIdsCitedByInproceedings.getInt("id_article2");
							
							query = new String("SELECT title FROM paper WHERE id="+journalId);
							result = db.query(query);
							result.next();
							String paperTitle = result.getString("title");
							
							query = new String("SELECT journal FROM article WHERE title='"+paperTitle+"'");
							ResultSet proceedingsResult = db.query(query);
							if(proceedingsResult.next()){
								String journalTitle = proceedingsResult.getString("journal");
								
								if(journalById.containsKey(journalTitle)){
									Journal journal = journalById.get(journalTitle);
									journal.incrementCcount();
								}
								else{
									Journal journal = new Journal(0, journalTitle);
									journalById.put(journalTitle, journal);
								}
							}
						}
						
					}
				}
			}	
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		List JournalsList = new ArrayList<Journal>(journalById.values());
		Collections.sort(JournalsList);
		return JournalsList;
	}

	
	
	public static void main(String[] args) {
		JournalByProceedings teste = new JournalByProceedings();
		List list = teste.listRankedJournalCitedByProceedings("Advanced Database Systems");
		System.out.println(list.size());


	}
	
	
}
