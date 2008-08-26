package br.ufrj.cos.bri.util.text.example;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import br.ufrj.cos.bri.util.text.TextPreprocessing;


public class TestTextProcessing {

	/**
	 * Tests text preprocessing.
	 * @param args
	 */
	public static void main(String[] args) {
		String string = new String("The quick brown fox jumps over the lazy dog mating ocurring");
		
		List<String> temporaryListOfTerms = new Vector<String>();
		
		TextPreprocessing textProcessor = new TextPreprocessing();
		textProcessor.loadListOfStopWords("resources/stopwords/english.stopwords.txt");
		
		temporaryListOfTerms = textProcessor.removeStopWords(string);
		
		System.out.println("After stop words removing:");
		for (Iterator<String> iterator = temporaryListOfTerms.iterator(); iterator.hasNext();) {
			String term = (String) iterator.next();
			System.out.println(term);
		}
		
		String resultingListOfTerms = textProcessor.applyPorterStemmer(temporaryListOfTerms);
		
		System.out.println("After Porter stemming:");
		for (String term: resultingListOfTerms.split(" ")) {
			System.out.println(term);
		}
	}
}
