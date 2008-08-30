package br.ufrj.cos.bri.dblp.model;

import java.util.Vector;

import br.ufrj.cos.bri.model.Registro;

public class RegistroDBLP extends Registro {
	public enum DBLP_DATA {INPROCEEDINGS, ARTICLE, PROCEEDINGS};
	private DBLP_DATA current;
	
	/** COMMON */
	private String key;
	private String mdate;
	private Vector<String> author;
	private String editor;
	private String title;
	private String year;
	private String booktitle;
	private String link;
	private String journal;
	
	public void print() {
		System.out.println("[TYPE="+current.toString()+", "+
				"KEY= "+key+", "+
				"MDATE= "+mdate+
				"]");
	}
	
	public void setDataType(DBLP_DATA type) {
		current = type;
		author = new Vector<String>();
	}
	
	public DBLP_DATA getDataType() {
		return current;
	}
	
	public void setKey(String k) {
		key = k;
		key = key.replaceAll("'", "");
	}
	
	public String getKey() {
		return key;
	}
	
	public void setMdate(String m) {
		mdate = m;
		mdate = mdate.replaceAll("'", "");
	}
	
	public String getMdate() {
		return mdate;
	}
	
	public void addAuthor(String a) {
		author.add(a.replaceAll("'", ""));
	}
	
	public Vector<String> getAuthors() {
		return author;
	}
	
	public void setEditor(String e) {
		editor = e;
		editor = editor.replaceAll("'", "");
	}
	
	public String getEditor() {
		return editor;
	}
	
	public void setTitle(String t) {
		title = t;
		title = title.replaceAll("'", "");
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setBookTitle(String t) {
		booktitle = t;
		booktitle = booktitle.replaceAll("'", "");
	}
	
	public String getBookTitle() {
		return booktitle;
	}
	
	public void setYear(String t) {
		year = t;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setLink(String t) {
		link = t;
		link = link.replaceAll("'", "");
	}
	
	public String getLink() {
		return link;
	}
	
	public void setJournal(String t) {
		journal = t;
		journal = journal.replaceAll("'", "");
	}
	
	public String getJournal() {
		return journal;
	}
}
