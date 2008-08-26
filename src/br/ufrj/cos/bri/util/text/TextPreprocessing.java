package br.ufrj.cos.bri.util.text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;


public class TextPreprocessing {

	private List<String> listOfStopWords;

	/**
	 * Default constructor method.
	 */
	public TextPreprocessing() {
		listOfStopWords = new Vector<String>();
	}

	/**
	 * Removes all the non-letter characters from the input string, if any.
	 * Also removes excessive whitespace occurrences (when there's one or more
	 * whitespace adjacent to another whitespace).
	 * @param source - a string
	 * @return the content of the input string with only letter and whitespace characters
	 */
	public String removeNonLetterCharacters(String source) {
		String result = source;
		// replaces all non-letter characters by a whitespace
		result = result.replaceAll("[^a-zA-Z]"," ");
		// removes excessive whitespace occurrences
		result = result.replaceAll("[ ]+"," ");
		return result;
	}

	/**
	 * Removes stop words (defined in the list of stop words),
	 * if any, from the input string.
	 * A list of stop words <i>must</i> be loaded by the method
	 * <code>loadListOfStopWords(String filePath)</code>
	 * before calling this method, otherwise there will be
	 * no stop word removed from the input string.
	 * @param source - a string
	 * @return a list of all the words from the input string,
	 * except the stop words
	 */
	public List<String> removeStopWords(String source) {
		String[] words = source.split(" ");

		List<String> listOfWords = new Vector<String>();

		for (int i = 0; i < words.length; i++) {
			String word = words[i].toUpperCase();
			// this algorithm is not considering composite terms
			if (!listOfStopWords.contains(word)) {
				listOfWords.add(word);
			}
		}
		return listOfWords;
	}

	/**
	 * Applies Porter's Stemming Algorithm to the input set of strings.
	 * @param source - the input set of words
	 * @return the content of that set of words, now stemmed
	 */
	public String applyPorterStemmer(List<String> source) {
		StringBuffer result = new StringBuffer();
		Stemmer stemmer = new Stemmer();

		for (int i = 0; i < source.size(); i++) {
			String wordString = source.get(i);
			// the stemming algorithm works with arrays of chars
			char[] word = wordString.toCharArray();
			// adding char by char to the stemmer
			for (int pos = 0; pos < word.length; pos++) {
				// the stemming algorithm works with lower-case letters
				// so it's necessary to convert letters to lower-case
				stemmer.add(Character.toLowerCase(word[pos]));
			}
			// calling the stemming algorithm
			stemmer.stem();
			// converting the stemming result (an array of char) to string
			String resultString = stemmer.toString();
			// adding to the result string, reversing the lower-case conversion
			result.append(resultString +" ");
		}
		return result.toString();
	}

	/**
     * Loads a list of stop words from the input file path
     * so that these words can be removed thereafter by the method
     * <code>removeStopWords(String source)</code>.
     * The file must have <i>only one word</i> per line.
     * @param filePath - the file path to the list
     * @return true if loaded successfully, false otherwise
     */
	public void loadListOfStopWords(String filePath) {
		try {
			// open the file using the path defined in filePath parameter
			FileInputStream fileInputStream = new FileInputStream(filePath);
			// get the object of DataInputStream
			DataInputStream in = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			String stringLine;
			// read the file line by line
			while ((stringLine = bufferedReader.readLine()) != null) {
				listOfStopWords.add(stringLine.trim());
			}
			// close the input stream
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes text preprocessing with the input string text.
	 * @param sourceText - the text to be preprocessed 
	 * @return the terms obtained by text preprocessing
	 */
	public static String preProcessText(String sourceText) {
		TextPreprocessing textProcessor = new TextPreprocessing();
		textProcessor.loadListOfStopWords("resources/stopwords/english.stopwords.txt");
		if (sourceText == null) {
			sourceText = "";
		}
		String tempText = sourceText.toUpperCase();
		tempText = textProcessor.removeNonLetterCharacters(tempText);			
		List<String> tempListofTerms = textProcessor.removeStopWords(tempText);
		String listOfTerms = textProcessor.applyPorterStemmer(tempListofTerms);
		
		return listOfTerms;
	}
	
}
