package br.ufrj.cos.bri.model;

public class Proceedings {
	
	private int id;
	
	private String title;
	
	private String year;
	
	private int count;
	
	public Proceedings(int id, String title, String year){
		this.id = id;
		this.title = title;
		this.year = year;
		count = 0;
	}
	
	public int compareTo(Object obj){
		
		return ((Proceedings)obj).count-count;
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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
