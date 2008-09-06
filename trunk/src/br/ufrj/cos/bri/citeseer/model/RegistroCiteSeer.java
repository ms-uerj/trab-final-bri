package br.ufrj.cos.bri.citeseer.model;

import java.util.Vector;

import br.ufrj.cos.bri.model.Registro;

public class RegistroCiteSeer extends Registro {
//	public enum DBLP_DATA {INPROCEEDINGS, ARTICLE, PROCEEDINGS};
//	private DBLP_DATA current;
	
	/** COMMON */
	private String id;
	private String title;
	private Vector<String> references;
	private Vector<String> referencedBy;
	
	public void cleanRegister(){
		references.clear();
		referencedBy.clear();
		id="";
		title="";
	}
	
	public RegistroCiteSeer(){
		references = new Vector<String>();
		referencedBy = new Vector<String>();
		id="";
		title="";
	}
	
//	public void print() {
//		System.out.println("[TYPE="+current.toString()+", "+
//				"KEY= "+key+", "+
//				"MDATE= "+mdate+
//				"]");
//	}
	
//	public void setDataType(DBLP_DATA type) {
//		current = type;
//		author = new Vector<String>();
//	}
//	
//	public DBLP_DATA getDataType() {
//		return current;
//	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}	
	
	
	public void setTitle(String t) {
		title = t;
		title = title.replaceAll("'", "");
	}
	
	public String getTitle() {
		return title;
	}
	
	public void addReference(String articleReferenceId) {
		references.add(articleReferenceId);
	}
	
	public Vector<String> getReferences() {
		return references;
	}
	
	public void addReferencedBy(String articleReferenceId) {
		referencedBy.add(articleReferenceId);
	}
	
	public Vector<String> getReferencedBy() {
		return referencedBy;
	}
}