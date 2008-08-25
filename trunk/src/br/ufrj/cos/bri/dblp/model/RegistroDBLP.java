package br.ufrj.cos.bri.dblp.model;

import br.ufrj.cos.bri.model.Registro;

public class RegistroDBLP extends Registro {
	public enum DBLP_DATA {INPROCEEDINGS, ARTICLE};
	private DBLP_DATA current;
	
	/** COMMON */
	private String key;
	private String mdate;
	private String author;
	private String editor;
	private String title;
	
	public void print() {
		System.out.println("[TYPE="+current.toString()+", "+
				"KEY= "+key+", "+
				"MDATE= "+mdate+
				"]");
	}
	
	public void setDataType(DBLP_DATA type) {
		current = type;
	}
	
	public DBLP_DATA getDataType() {
		return current;
	}
	
	public void setKey(String k) {
		key = k;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setMdate(String m) {
		mdate = m;
	}
	
	public String getMdate() {
		return mdate;
	}
	
	public void setAuthor(String a) {
		author = a;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setEditor(String e) {
		editor = e;
	}
	
	public String getEditor() {
		return editor;
	}
	
	public void setTitle(String t) {
		title = t;
	}
	
	public String getTitle() {
		return title;
	}
	
}
