package br.ufrj.cos.bri.util.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.ufrj.cos.bri.citeseer.model.RegistroCiteSeer;
import br.ufrj.cos.bri.model.InteressadoRegistro;

public class CiteSeerXMLReader extends DefaultHandler {
	private String arquivo = null;
	private InteressadoRegistro interessado=null;
	
	/* Parametros auxialiares para o parser */
	private String localTitle = new String();
	private String localIdentifier = new String();
	private String localReferenceIdentifier = new String();
	
	/** PARSER DBLP */
    private boolean inRecord=false;
    private boolean inRelation=false;
    private boolean inURI=false;
    private String relationType=null;
    private boolean inIdentifier=false;
    private boolean inTitle=false;
    private RegistroCiteSeer registroCiteSeer=null;
	
	public CiteSeerXMLReader(String arquivo) {
		super();
		this.arquivo = arquivo;
		registroCiteSeer = new RegistroCiteSeer();		
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
		
//		String simpleName = qName;
//		if(qName.contains(":"))
//			simpleName = qName.substring(qName.lastIndexOf(":")+1);
		
		if(qName.equals("record")) {
			inRecord = true;
		}
		else
		if(qName.equals("oai_citeseer:relation") && inRecord) {
			inRelation=true;
			relationType = atts.getValue("type");
		}
		else
		if(qName.equals("oai_citeseer:uri") && inRelation) {
			inURI=true;
		}
		else if(qName.equals("dc:title") && inRecord) {
			inTitle = true;
		}
		else if(qName.equals("identifier") && inRecord) {
			inIdentifier = true;
		}
	}
	
	public void endElement (String uri, String localName, String qName) throws SAXException {
		
//		String simpleName = qName;
//		if(qName.contains(":"))
//			simpleName = qName.substring(qName.lastIndexOf(":")+1);
		
		if(qName.equals("record") && inRecord) {
			inRecord = false;
			interessado.receberRegistro(registroCiteSeer);
			registroCiteSeer.cleanRegister();
		}
		else if(qName.equals("dc:title") && inTitle) {
			inTitle = false;
			localTitle="";
		}
		else if(qName.equals("identifier") && inIdentifier) {
			inIdentifier = false;
			registroCiteSeer.setId(localIdentifier.substring(localIdentifier.lastIndexOf(":")+1));
			localIdentifier="";
		}
		else if(qName.equals("oai_citeseer:uri") && inURI) {
			inURI = false;
			inRelation = false;
			
			if(relationType.equals("References"))
				registroCiteSeer.addReference(localReferenceIdentifier.substring(localReferenceIdentifier.lastIndexOf(":")+1));
			else if(relationType.equals("Is Referenced By"))
				registroCiteSeer.addReferencedBy(localReferenceIdentifier.substring(localReferenceIdentifier.lastIndexOf(":")+1));
			
			localReferenceIdentifier="";
		}
//		else if(qName.equals("oai_citeseer:relation") && inRelation) {
//			inRelation = false;
//		}
	}
	
	public void characters (char[] ch, int start, int length) throws SAXException {
		
		if(inTitle) {
			localTitle += new String(ch,start,length);
			registroCiteSeer.setTitle(localTitle);
		}
		else if(inIdentifier) {
			localIdentifier += new String(ch,start,length);
		}
		else if(inURI) {
			localReferenceIdentifier += new String(ch,start,length);
		}
		
	}
	
	public void endDocument() {
		interessado.terminoLeitura();
	}
	
	
}
