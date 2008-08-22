package br.ufrj.cos.bri.dblp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class Extractor {

	
	public String getDocument(String URL) throws Exception{
		
		if(URL.endsWith("pdf") || URL.endsWith("PDF")){
			
		}
		else if (URL.contains("ieeexplore.ieee.org")){
			
		}
		else if (URL.contains("portal.acm.org")){
			
		}
		else if (URL.contains("www.springerlink.com")){
			String pdfLink = getPDFLinkFromSpringer(URL);
			byte[] pdf = getPDFFile(pdfLink);
		}
		
		return null;
	}
	
	public byte[] getPDFFile(String URL) throws Exception{
		
	    // Cria uma instância de HttpClient.
	    HttpClient client = new HttpClient();
		
		HttpMethod method = new GetMethod(URL);
		
		// Executa o método.
	    int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
		  throw new Exception("URL:\n"+HttpStatus.getStatusText(statusCode));
		}
		
		byte[] pdf = method.getResponseBody(); 
		
		return pdf;
		
	}
	
	public String getPDFLinkFromSpringer(String URL) throws Exception{

	    // Cria uma instância de HttpClient.
	    HttpClient client = new HttpClient();
		
		HttpMethod method = new GetMethod(URL);
		
		// Executa o método.
	    int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
		  throw new Exception("URL:\n"+HttpStatus.getStatusText(statusCode));
		}
		
		byte[] springerPage = method.getResponseBody(); 
		
		String springerHTML = new String(springerPage);
		
		int index = springerHTML.indexOf(".pdf");
		if(index<0)
			index = springerHTML.indexOf(".PDF");
		
		if(index>0){
			int startindex = index - 1 -"href=".length();
			String temp = springerHTML.substring(startindex, index);
			
			while(!(temp.contains("href=") || temp.contains("href=")) && startindex>0){
				startindex --;
				temp = springerHTML.substring(startindex, index);
			}
			
			if(startindex>0){
				int temp2 = temp.indexOf("href=");
				if(temp2<0)
					temp2 = temp.indexOf("HREF=");
				
				
				int pdfLinkStartIndex = startindex + temp2 + "href=".length() + 1;
				int pdfLinkEndIndex = index + ".pdf".length();
				
				String pdfLink = springerHTML.substring(pdfLinkStartIndex, pdfLinkEndIndex);
				if(pdfLink.startsWith("/"))
					pdfLink = "http://www.springerlink.com" + pdfLink;
				
				return pdfLink;
				
			}
			else 
				return null;
			
		}
		else
			return null;
		
	}
	
	public String getPDFLinkFromIEEE(String URL) throws Exception{
		
	    // Cria uma instância de HttpClient.
	    HttpClient client = new HttpClient();
		
		HttpMethod method = new GetMethod(URL);
		
		// Executa o método.
	    int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
		  throw new Exception("URL:\n"+HttpStatus.getStatusText(statusCode));
		}
		
		byte[] ieeePage = method.getResponseBody(); 
		
		String ieeeHTML = new String(ieeePage);
		
		int index = ieeeHTML.indexOf("Full Text:");
		if(index>0){
			String partACMHTML = ieeeHTML.substring(index);
			
			int hrefIndex = partACMHTML.indexOf("href");
			
			int pdfLinkStartIndex = hrefIndex +"href=\"".length();
			int pdfLinkEndIndex = pdfLinkStartIndex;
			
			while(partACMHTML.charAt(pdfLinkEndIndex) != '"'){
				pdfLinkEndIndex++;
			}
			
			String pdfLink = partACMHTML.substring(pdfLinkStartIndex, pdfLinkEndIndex);
			if(pdfLink.startsWith("/"))
				pdfLink = "http://ieeexplore.ieee.org" + pdfLink;
			
			return pdfLink;
		}
		else 
			return null;
		
	}
	
	public String getPDFLinkFromACM(String URL) throws Exception{
		
	    // Cria uma instância de HttpClient.
	    HttpClient client = new HttpClient();
		
		HttpMethod method = new GetMethod(URL);
		
		// Executa o método.
	    int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
		  throw new Exception("URL:\n"+HttpStatus.getStatusText(statusCode));
		}
		
		byte[] acmPage = method.getResponseBody(); 
		
		String acmHTML = new String(acmPage);
		
		int index = acmHTML.indexOf("<STRONG>Full text</STRONG>");
		if(index>0){
			String partACMHTML = acmHTML.substring(index);
			
			int hrefIndex = partACMHTML.indexOf("href");
			
			int pdfLinkStartIndex = hrefIndex +"href=\"".length();
			int pdfLinkEndIndex = pdfLinkStartIndex;
			
			while(partACMHTML.charAt(pdfLinkEndIndex) != '"'){
				pdfLinkEndIndex++;
			}
			
			String pdfLink = partACMHTML.substring(pdfLinkStartIndex, pdfLinkEndIndex);
			if(pdfLink.startsWith("/"))
				pdfLink = "http://portal.acm.org" + pdfLink;
			
			return pdfLink;
		}
		else 
			return null;
		
		
	}
	
	public static void main(String[] args){
		Extractor ex = new Extractor();
		
		try {
			System.out.println(ex.getPDFLinkFromIEEE("http://www.springerlink.com/content/qh37452kt05513u7"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
