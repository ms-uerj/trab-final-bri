package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.ufrj.cos.bri.model.Journal;
import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class JournalByProceedings {
	private static MysqlConnector db = null;

	public JournalByProceedings() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public List<Journal> listRankedJournalCitedByProceedings(int proceedingsId) {
		String query;
		Map<String, Journal> journalById = new HashMap<String, Journal>();
		ResultSet result;
		
		try {
				
			query = new String("SELECT LOWER(left(title, length(title)-1)) as title FROM inproceedings_ordered WHERE id_proceedings="+proceedingsId);

			ResultSet set = db.query(query);

			while(set.next()) {
				String inproceedingsTitle = set.getString("title");
				//System.out.println("Checking paper "+inproceedingsTitle+" ...");
				//busca na base do citeseer o artigo
				query = new String("SELECT a.id FROM paper a WHERE LOWER(a.title)=\'"+inproceedingsTitle+"\'");
				ResultSet paper = db.query(query);

				if(paper.next()) {
					//System.out.println("Paper found in Citeseer.");
					int idPaper = paper.getInt("id");
					query = new String("SELECT id_article2 FROM paper_references_temp WHERE id_article1="+idPaper);
					//pode ser um periódico, inproceedings, incollection
					//System.out.println("Searching references ...");
					ResultSet JournalIdsCitedByInproceedings = db.query(query);

					while(JournalIdsCitedByInproceedings.next()){
						int journalId = JournalIdsCitedByInproceedings.getInt("id_article2");

						query = new String("SELECT LOWER(title) as title FROM paper WHERE id="+journalId);
						result = db.query(query);
						result.next();
						String paperTitle = result.getString("title");

						//System.out.println("Found reference: "+paperTitle);

						query = new String("SELECT journal FROM article WHERE LOWER(left(title, length(title)-1))='"+paperTitle+"'");
						ResultSet proceedingsResult = db.query(query);
						if(proceedingsResult.next()){
							String journalTitle = proceedingsResult.getString("journal");
							//System.out.println("Reference published in journal "+journalTitle);
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
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		List JournalsList = new ArrayList<Journal>(journalById.values());
		//Collections.sort(JournalsList);
		return JournalsList;
	}

	
	
	public static void main(String[] args) {
		JournalByProceedings teste = new JournalByProceedings();
		
		String query = new String("SELECT id, title FROM proceedings");
		
		ResultSet result = db.query(query);
		
		try {
			while(result.next()) {
				int proceedingsId = result.getInt("id");
				String proceedingsName = result.getString("title");
				
				System.out.println("Getting journals cited by "+proceedingsName+" ...");
				
				List list = teste.listRankedJournalCitedByProceedings(proceedingsId);
				
				Iterator<Journal> journals = list.iterator();
				
				while(journals.hasNext()) {
					Journal journal = journals.next();
					String insert = new String("INSERT INTO JOURNALBYPROC VALUES (\'"+proceedingsName+"\', \'"+journal.getTitle()+"\',"+ journal.getCount()+")");
					
					try {
						db.exec(insert);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
