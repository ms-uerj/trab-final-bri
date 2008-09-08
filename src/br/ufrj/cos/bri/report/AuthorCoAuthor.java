package br.ufrj.cos.bri.report;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class AuthorCoAuthor {
	private MysqlConnector db = null;

	public AuthorCoAuthor() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("INF/INF.properties"));
		} catch (Exception e) {e.printStackTrace();}
		
		db = new MysqlConnector();
		db.connect();
	}
	
	public Map<String, Integer> listCoAuthors(String name) {
		String query = new String("SELECT id FROM author WHERE name="+"\'"+name+"\'");
		Map<String, Integer> coAuthors = new HashMap<String, Integer>();
		
		ResultSet set = db.query(query);
		
		int idauthor=0;
		try {
			if(set.next()) {
				idauthor = set.getInt("id");
				//busca artigos de uma autor
				query = new String("SELECT a.id_article FROM article_author a WHERE a.id_author="+"\'"+idauthor+"\'");
				ResultSet articles = db.query(query);
				
				//para cara artigo contabiliza os co-autores
				while(articles.next()) {
					int idArticle = articles.getInt("id_article");
					//busca os co-autores de um artigo 
					query = new String("SELECT a.id_article, id_author FROM article_author a WHERE a.id_article="+"\'"+idArticle+"\'");
					ResultSet idCoAuthors = db.query(query);
					
					while(idCoAuthors.next()) {
						int idCoAuthor = idCoAuthors.getInt("id_author");
						if (idCoAuthor != idauthor) {
							countCoAuthor(coAuthors, idCoAuthor);
						}
						//System.out.println(idArticle);
					}
				}
			}
			else {
				System.out.println("Author "+name+" não encontrado.");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return coAuthors;
	}
	
	public void countCoAuthor(Map<String, Integer> coAuthors, int idCoAuthor) {
		String query = new String("SELECT name FROM author WHERE id="+"\'"+idCoAuthor+"\'");
		
		ResultSet set = db.query(query);
		
		String nameCoAuthor = "";
		try {
			if(set.next()) {
				nameCoAuthor = set.getString("name");
				
				//se já contém co-author, incrementa valor
				if (coAuthors.containsKey(nameCoAuthor)){
					int valor = coAuthors.get(nameCoAuthor);
					coAuthors.put(nameCoAuthor, valor+1);
				}
				//caso contrário adiciona co author no HashMap 
				else {
					coAuthors.put(nameCoAuthor, 1);
				}
				
			}
			else {
				System.out.println("Co-Author "+idCoAuthor+" não encontrado.");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		AuthorCoAuthor teste = new AuthorCoAuthor();
		//teste.listCoAuthors("ClÃ¡udia Maria Lima Werner");
		//teste.listCoAuthors("Leonardo G. P. Murta");
		
		Map<String, Integer> coAuthors = teste.listCoAuthors("Jano Moreira de Souza");
		
		Iterator it = coAuthors.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	    }


	}
	
	
}
