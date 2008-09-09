package br.ufrj.cos.bri.model;

public class Journal {
	
	private int id;
	
	private String title;
	
	private int count;
	
	public Journal(int id, String title){
		this.id = id;
		this.title = title;
		count = 0;
	}
	
	public int compareTo(Object obj){
		
		return count-((Journal)obj).count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void incrementCcount(){
		count++;
	}
	
	
}
