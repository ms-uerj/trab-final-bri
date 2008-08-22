package br.ufrj.cos.bri.util;

import java.io.IOException;
import java.io.InputStream;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;


public class PDFParser{
	
	public static String getTextContent(InputStream is){
	
		try{
			PDDocument pdfDocument = PDDocument.load(is);
			
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(pdfDocument);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
} 
