package br.ufrj.cos.bri.util.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.ufrj.cos.bri.dblp.model.RegistroDBLP;
import br.ufrj.cos.bri.model.InteressadoRegistro;
import br.ufrj.cos.bri.model.Registro;
import br.ufrj.cos.bri.model.RegistroBase;
import br.ufrj.cos.bri.model.RegistroConsulta;

public class LeitorXML extends DefaultHandler {
	private String arquivo = null;
	private InteressadoRegistro interessado=null;
	
	/* Parametros auxialiares para o parser */
	private String localTitle = new String();
	private String localBookTitle = new String();
	private String localYear = new String();
	private String localLink = new String();
	
	/** PARSER DBLP */
	private boolean inTitle=false;
    private boolean inInProceedings=false;
    private boolean inBookTitle=false;
    private boolean inYear=false;
    private boolean inLink=false;
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
			//registroDBLP.print();
		}
		else if(qName.equals("title") && inInProceedings) {
			inTitle = true;
		}
		else if(qName.equals("booktitle") && inInProceedings) {
			inBookTitle = true;
		}
		else if(qName.equals("year") && inInProceedings) {
			inYear = true;
		}
		else if(qName.equals("url") && inInProceedings) {
			inLink = true;
		}

	}
	
	public void endElement (String uri, String localName, String qName) throws SAXException {
		
		if(qName.equals("inproceedings")) {
			inInProceedings = false;
			interessado.receberRegistro(registroDBLP);
			localBookTitle="";
			localLink="";
			localTitle="";
			localYear="";
		}
		else if(qName.equals("title")) {
			inTitle = false;
		}
		else if(qName.equals("booktitle")) {
			inBookTitle = false;
		}
		else if(qName.equals("year")) {
			inYear = false;
		}
		else if(qName.equals("url")) {
			inLink = false;
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
		
	}
	
	public void endDocument() {
		interessado.terminoLeitura();
	}
	
	
}
