package br.ufrj.cos.bri.model;

import java.io.BufferedReader;
import java.io.FileReader;

public class RegistroBase extends Registro {
	private int recordNum;
	private String titulo;
	private String _abstract;
	private String corpoXML;
	private static String usedFile=null;
	private static BufferedReader input=null;
	private static String resto="";
	
	public RegistroBase(String arquivo) {
		super();
		
		if(usedFile==null || usedFile.equals(arquivo)==false) {
			usedFile = arquivo;
			try {
				if(input != null) {
					input.close();
				}
				
				input =  new BufferedReader(new FileReader(arquivo));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setRecordNum(int r) {
		recordNum = r;
	}
	
	public void setTitulo(String t) {
		titulo = t.replaceAll("\\n", "");
		titulo = titulo.replaceAll("'", "");
	}
	
	public void setAbstract(String a) {
		_abstract = a.replaceAll("\\n", "");
		_abstract = _abstract.replaceAll("'", "");
	}
	
	public void setCorpoXML() {
		String xml = new String(resto);
		
		try {
			String line = null;
			boolean inRecord=(resto.equals("")?false:true);
			
			while((line = input.readLine()) != null) {
				
				if(line.matches(".*</RECORD>.*")) {
					if(line.matches(".*<RECORD>.*")) {
						int index = line.indexOf("</RECORD>");
						xml += line.substring(0, index+9);
						resto = line.substring(index+9);
					} else {
						resto="";
						xml += line;
					}
					
					break;
				}
				if(line.matches(".*<RECORD>.*")) {
					inRecord=true;
				} 
				
				if(inRecord) {
					xml += line;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		xml = xml.replaceAll("\\n", "");
		xml = xml.replaceAll("'", "");
		corpoXML = xml;
	}
	
	public int pegaRecordNum() {
		return recordNum;
	}
	
	public String pegaTitulo() {
		return titulo;
	}
	
	public String pegaAbstract() {
		return _abstract;
	}
	
	public String pegaCorpoXML() {
		return corpoXML;
	}
	
}
