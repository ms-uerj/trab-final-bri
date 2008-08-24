package br.ufrj.cos.bri.util.xml;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.ufrj.cos.bri.dblp.model.Person;
import br.ufrj.cos.bri.dblp.model.Publication;
import br.ufrj.cos.bri.model.InteressadoRegistro;
import br.ufrj.cos.bri.model.Registro;
import br.ufrj.cos.bri.model.RegistroBase;
import br.ufrj.cos.bri.model.RegistroConsulta;

public class LeitorXML extends DefaultHandler {
	private String arquivo = null;
	private Registro registro = null;
	private InteressadoRegistro interessado=null;
	
	/* Parametros auxialiares para o parser */
	boolean inRecordNum=false;
	boolean inTitle=false;
	boolean inAbstract=false;
	boolean inQueryNumber=false;
	boolean inQueryText=false;
	boolean inResults=false;
	boolean inItemScore=false;
	String localTitle = new String();
	String localAbstract = new String();
	String localQueryText = new String();
	
	/** PARSER DBLP */
	private Locator locator;
	private final int maxAuthorsPerPaper = 200;
    private String Value;
    private String key;
    private String recordTag;
    private Person[] persons= new Person[maxAuthorsPerPaper];
    private int numberOfPersons = 0;

    private boolean insidePerson;
	
	public LeitorXML(String arquivo) {
		super();
		this.arquivo = arquivo;
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
	
	public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

	
	public void startElement (String uri, String localName, String qName, Attributes atts) {
		String k;
		
		if (qName.equals("RECORD")) {
			registro = new RegistroBase(arquivo);
		}
		else if(qName.equals("RECORDNUM")) {
			inRecordNum=true;
		}
		else if(qName.equals("TITLE")) {
			inTitle=true;
		}
		else if(qName.equals("ABSTRACT")) {
			inAbstract=true;
		}
		else if(qName.equals("QUERY")) {
			registro = new RegistroConsulta();
		}
		else if(qName.equals("QueryNumber")) {
			inQueryNumber=true;
		}
		else if(qName.equals("QueryText")) {
			inQueryText=true;
		}
		else if(qName.equals("Results")) {
			inResults=true;
		}
		else if(qName.equals("Item")) {
			inItemScore=true;
			String s = atts.getValue("score");
			((RegistroConsulta)registro).addScore(s.trim());
		}
		
		/** PARSER DBLP */
		else if (insidePerson = (qName.equals("author") || qName
                .equals("editor"))) {
            Value = "";
            return;
        }
		else if ((atts.getLength()>0) && ((k = atts.getValue("key"))!=null)) {
            key = k;
            recordTag = qName;   
        }

	}
	
	public void endElement (String uri, String localName, String qName) throws SAXException {
		if (qName.equals("RECORD")) {
			((RegistroBase)registro).setCorpoXML();
			interessado.receberRegistro(registro);
			localTitle = "";
			localAbstract = "";
		}
		else if(qName.equals("RECORDNUM")) {
			inRecordNum=false;
		}
		else if(qName.equals("TITLE")) {
			inTitle=false;
		}
		else if(qName.equals("ABSTRACT")) {
			inAbstract=false;
		}
		else if(qName.equals("QUERY")) {
			interessado.receberRegistro(registro);
			localQueryText="";
		}
		else if(qName.equals("QueryNumber")) {
			inQueryNumber=false;
		}
		else if(qName.equals("QueryText")) {
			inQueryText=false;
		}
		else if(qName.equals("Results")) {
			inResults=false;
		}
		else if(qName.equals("Item")) {
			inItemScore=false;
		}
		
		/** PARSER DBLP */
		else if (qName.equals("author") || qName.equals("editor")) {

            Person p;
            if ((p = Person.searchPerson(Value)) == null) {
                p = new Person(Value);
            }
            p.increment();
            if (numberOfPersons<maxAuthorsPerPaper)
                persons[numberOfPersons++] = p;
            return;
        }
		else if (qName.equals(recordTag)) {
            if (numberOfPersons == 0)
                return;
            Person pa[] = new Person[numberOfPersons];
            for (int i=0; i<numberOfPersons; i++) {
                pa[i] = persons[i];
                persons[i] = null;
            }
            Publication p = new Publication(key,pa);
            numberOfPersons = 0;
        }

	}
	
	public void characters (char[] ch, int start, int length) throws SAXException {
		if (inRecordNum) {
			((RegistroBase)registro).setRecordNum(Integer.parseInt(new String(ch,start,length).trim()));
		}
		else if(inTitle) {
			localTitle += new String(ch,start,length);
			((RegistroBase)registro).setTitulo(localTitle);
		}
		else if(inAbstract) {
			localAbstract += new String(ch,start,length);
			((RegistroBase)registro).setAbstract(localAbstract);
		}
		else if(inQueryNumber) {
			((RegistroConsulta)registro).setNumeroConsulta(Integer.parseInt(new String(ch,start,length).trim()));
		}
		else if(inQueryText) {
			localQueryText += new String(ch,start,length);
			((RegistroConsulta)registro).setTextoConsulta(localQueryText);
		}
		else if(inResults) {
			((RegistroConsulta)registro).setResultados(Integer.parseInt(new String(ch,start,length).trim()));
		}
		else if(inItemScore) {
			((RegistroConsulta)registro).addRegistro(new Integer(Integer.parseInt(new String(ch,start,length).trim())));
		}
		
		/** PARSER DBLP */
		else if (insidePerson) {
            Value += new String(ch, start, length);
		}			
	}
	
	public void endDocument() {
		interessado.terminoLeitura();
	}
	
	
}