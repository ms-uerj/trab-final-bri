package br.ufrj.cos.bri.util.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.ufrj.cos.bri.dblp.model.RegistroDBLP;
import br.ufrj.cos.bri.model.InteressadoRegistro;

public class LeitorXML extends DefaultHandler {
	private String arquivo = null;
	private InteressadoRegistro interessado=null;
	
	/* Parametros auxialiares para o parser */
	private String localTitle = new String();
	private String localBookTitle = new String();
	private String localYear = new String();
	private String localLink = new String();
	private String localAuthor = new String();
	private String localJournal = new String();
	private String localCrossref = new String();
	
	/** PARSER DBLP */
	private boolean inTitle=false;
    private boolean inInProceedings=false;
    private boolean inBookTitle=false;
    private boolean inYear=false;
    private boolean inLink=false;
    private boolean inAuthor=false;
    private boolean inArticle=false;
    private boolean inJournal=false;
    private boolean inProceedings=false;
    private boolean inCrossref=false;
    private RegistroDBLP registroDBLP=null;
	
	public LeitorXML(String arquivo) {
		super();
		this.arquivo = arquivo;
		registroDBLP = new RegistroDBLP();		
	}
	
	public void cadastrarInteressado(InteressadoRegistro i) {
		interessado = i;
	}
	
	public void executarLeitura() {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			SAXParser xmlParser = factory.newSAXParser();
			xmlParser.parse(new File(arquivo), this);
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void startElement (String uri, String localName, String qName, Attributes atts) {
		
		if(qName.equals("inproceedings")) {
			inInProceedings = true;
			registroDBLP.setDataType(RegistroDBLP.DBLP_DATA.INPROCEEDINGS);
			String key = atts.getValue("key");
			String mdate = atts.getValue("mdate");
			registroDBLP.setKey(key);
			registroDBLP.setMdate(mdate);
		}
//		if(qName.equals("article")) {
//			inArticle=true;
//			registroDBLP.setDataType(RegistroDBLP.DBLP_DATA.ARTICLE);
//			String key = atts.getValue("key");
//			String mdate = atts.getValue("mdate");
//			registroDBLP.setKey(key);
//			registroDBLP.setMdate(mdate);
//		}
//		if(qName.equals("proceedings")) {
//			inProceedings=true;
//			registroDBLP.setDataType(RegistroDBLP.DBLP_DATA.PROCEEDINGS);
//			String key = atts.getValue("key");
//			String mdate = atts.getValue("mdate");
//			registroDBLP.setKey(key);
//			registroDBLP.setMdate(mdate);
//		}
		else if(qName.equals("title") && (inInProceedings||inArticle||inProceedings)) {
			inTitle = true;
		}
		else if(qName.equals("booktitle") && (inInProceedings||inArticle||inProceedings)) {
			inBookTitle = true;
		}
		else if(qName.equals("year") && (inInProceedings||inArticle||inProceedings)) {
			inYear = true;
		}
		else if(qName.equals("ee") && (inInProceedings||inArticle||inProceedings)) {
			inLink = true;
		}
		else if(qName.equals("author") && (inInProceedings||inArticle||inProceedings)) {
			inAuthor = true;
		}
		else if(qName.equals("journal") && (inInProceedings||inArticle||inProceedings)) {
			inJournal = true;
		}
		else if(qName.equals("crossref") && (inInProceedings||inArticle||inProceedings)) {
			inCrossref = true;
		}
	}
	
	public void endElement (String uri, String localName, String qName) throws SAXException {
		
		if(qName.equals("inproceedings") && inInProceedings) {
			inInProceedings = false;
			interessado.receberRegistro(registroDBLP);
		}
		else if(qName.equals("article") && inArticle) {
			inArticle = false;
			interessado.receberRegistro(registroDBLP);
		}
		else if(qName.equals("proceedings") && inProceedings) {
			inProceedings = false;
			interessado.receberRegistro(registroDBLP);
		}
		else if(qName.equals("title") && inTitle) {
			inTitle = false;
			localTitle="";
		}
		else if(qName.equals("booktitle") && inBookTitle) {
			inBookTitle = false;
			localBookTitle="";
		}
		else if(qName.equals("year") && inYear) {
			inYear = false;
			localYear="";
		}
		else if(qName.equals("ee") && inLink) {
			inLink = false;
			localLink="";
		}
		else if(qName.equals("author") && inAuthor) {
			inAuthor = false;
			registroDBLP.addAuthor(localAuthor);
			localAuthor="";
		}
		else if(qName.equals("journal") && inJournal) {
			inJournal = false;
			localJournal="";
		}
		else if(qName.equals("crossref") && inCrossref) {
			inCrossref = false;
			localCrossref="";
		}

	}
	
	public void characters (char[] ch, int start, int length) throws SAXException {
		if(inTitle) {
			localTitle += new String(ch,start,length);
			registroDBLP.setTitle(localTitle);
		}
		else if(inBookTitle) {
			localBookTitle += new String(ch,start,length);
			registroDBLP.setBookTitle(localBookTitle);
		}
		else if(inYear) {
			localYear += new String(ch,start,length);
			registroDBLP.setYear(localYear);
		}
		else if(inLink) {
			localLink += new String(ch,start,length);
			registroDBLP.setLink(localLink);
		}
		else if(inAuthor) {
			localAuthor += new String(ch,start,length);
			
		}
		else if(inJournal) {
			localJournal += new String(ch,start,length);
			registroDBLP.setJournal(localJournal);
		}
		else if(inCrossref) {
			localCrossref += new String(ch,start,length);
			registroDBLP.setCrossref(localCrossref);
		}
		
	}
	
	public void endDocument() {
		interessado.terminoLeitura();
	}
	
	
}
