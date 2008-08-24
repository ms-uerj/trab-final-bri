package br.ufrj.cos.bri.util.text;

public class Stemmer {

	// Adapted from http://tartarus.org/~martin/PorterStemmer/java.txt
	// Some points of difference and common errors are described in http://tartarus.org/~martin/PorterStemmer/

	private char[] word;
	private int i; // offset into b
	private int i_end; // offset to end of stemmed word
	private int j, k;
	private static final int INC = 50; // unit of size whereby b is increased

	/**
	 * Default constructor method.
	 */
	public Stemmer() {
		word = new char[INC];
		i = 0;
		i_end = 0;
	}

	/**
	 * Adds a character to the word being stemmed. When all the characters
	 * are added, you can call stem(void) to stem the word.
	 * @param character - the character to be added
	 */
	public void add(char character) {
		if (i == word.length) {
			char[] new_b = new char[i + INC];
			for (int c = 0; c < i; c++) {
				new_b[c] = word[c];
			}
			word = new_b;
		}
		word[i++] = character;
	}

	/**
	 * Adds wLen characters to the word being stemmed contained in a portion
	 * of a char[] array. This is like repeated calls of add(char character), but
	 * faster.
	 */
	public void add(char[] w, int wLen) {
		if (i+wLen >= word.length) {  
			char[] new_b = new char[i+wLen+INC];
			for (int c = 0; c < i; c++) {
				new_b[c] = word[c];
			}
			word = new_b;
		}
		for (int c = 0; c < wLen; c++) {
			word[i++] = w[c];
		}
	}

	/**
	 * After a word has been stemmed, it can be retrieved by toString(),
	 * or a reference to the internal buffer can be retrieved by getResultBuffer
	 * and getResultLength (which is generally more efficient).
	 * @return the word that has been stemmed
	 */
	public String toString() {
		return new String(word, 0, i_end);
	}

	/**
	 * @return the length of the word resulting from the stemming process
	 */
	public int getResultLength() {
		return i_end;
	}

	/**
	 * @return a reference to a character buffer containing the results of
	 * the stemming process. You also need to consult getResultLength()
	 * to determine the length of the result.
	 */
	public char[] getResultBuffer() {
		return word;
	}

	/**
	 * @return true if, and only if, b[i] is a consonant
	 */
	private final boolean cons(int i) {
		switch (word[i]) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
			return false;
		case 'y':
			return (i==0) ? true : !cons(i-1);
		default:
			return true;
		}
	}

	/**
	 * Measures the number of consonant sequences between 0 and j. if c is
	 * a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
	 * presence,
	 * <c><v>       gives 0
	 * <c>vc<v>     gives 1
	 * <c>vcvc<v>   gives 2
	 * <c>vcvcvc<v> gives 3
	 * ...
	 */
	private final int m() {
		int n = 0;
		int i = 0;
		while(true) {
			if (i > j) {
				return n;
			}
			if (!cons(i)) {
				break;
			}
			i++;
		}
		i++;
		while(true) {
			while(true) {
				if (i > j) {
					return n;
				}
				if (cons(i)) {
					break;
				}
				i++;
			}
			i++;
			n++;
			while(true) {
				if (i > j) {
					return n;
				}
				if (!cons(i)) {
					break;
				}
				i++;
			}
			i++;
		}
	}

	/**
	 * @return true if, and only if, 0,...j contains a vowel
	 */
	private final boolean vowelinstem() {
		int i;
		for (i = 0; i <= j; i++) {
			if (!cons(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if, and only if, j,(j-1) contain a double consonant
	 */
	private final boolean doublec(int j) {
		if (j < 1) {
			return false;
		}
		if (word[j] != word[j-1]) {
			return false;
		}
		return cons(j);
	}

	/**
	 * cvc(i) is true if, and only if, i-2,i-1,i has the form
	 * consonant - vowel - consonant
	 * and also if the second c is not w, x or y.
	 * This is used when trying to restore an e
	 * at the end of a short word.
	 * e.g. cav(e), lov(e), hop(e), crim(e), but snow, box, tray.
	 */
	private final boolean cvc(int i) {
		if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) {
			return false;
		}
		{  
			int ch = word[i];
			if (ch == 'w' || ch == 'x' || ch == 'y') {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param s the substring to be compared
	 * @return true if b ends with the string s
	 */
	private final boolean ends(String s) {
		int l = s.length();
		int o = k-l+1;
		if (o < 0) {
			return false;
		}
		for (int i = 0; i < l; i++) {
			if (word[o+i] != s.charAt(i)) {
				return false;
			}
		}
		j = k-l;
		return true;
	}

	/**
	 * Sets (j+1),...k to the characters in the string s,
	 * readjusting k.
	 */
	private final void setTo(String s) {
		int l = s.length();
		int o = j+1;
		for (int i = 0; i < l; i++) {
			word[o+i] = s.charAt(i);
		}
		k = j+l;
	}

	/**
	 * r(s) replaces the portion of an array of char
	 * by the input string s.
	 */
	private final void r(String s) {
		if (m() > 0) {
			setTo(s);
		}
	}

	/**
	 * step1() gets rid of plurals and -ed or -ing.
	 * e.g.<br/>
	 * <br/>
	 * caresses  ->  caress<br/>
	 * ponies    ->  poni<br/>
	 * ties      ->  ti<br/>
	 * caress    ->  caress<br/>
	 * cats      ->  cat<br/>
	 * feed      ->  feed<br/>
	 * agreed    ->  agree<br/>
	 * disabled  ->  disable<br/>
	 * matting   ->  mat<br/>
	 * mating    ->  mate<br/>
	 * meeting   ->  meet<br/>
	 * milling   ->  mill<br/>
	 * messing   ->  mess<br/>
	 * meetings  ->  meet
	 */
	private final void step1() {
		if (word[k] == 's') {
			if (ends("sses")) {
				k -= 2;
			} else if (ends("ies")) {
				setTo("i");
			} else if (word[k-1] != 's') {
				k--;
			}
		}
		if (ends("eed")) {
			if (m() > 0) k--; 
		} else if ((ends("ed") || ends("ing")) && vowelinstem()) {  
			k = j;
			if (ends("at")) {
				setTo("ate");
			} else if (ends("bl")) {
				setTo("ble");
			} else if (ends("iz")) {
				setTo("ize");
			} else if (doublec(k)) { 
				k--;
				{
					int ch = word[k];
					if (ch == 'l' || ch == 's' || ch == 'z') {
						k++;
					}
				}
			}
			else if (m() == 1 && cvc(k)) {
				setTo("e");
			}
		}
	}

	/**
	 * step2() turns terminal y to i when
	 * there is another vowel in the stem.
	 */
	private final void step2() {
		if (ends("y") && vowelinstem()) {
			word[k] = 'i'; 
		}
	}

	/**
	 * step3() maps double suffices to single ones.
	 * So -ization ( = -ize plus -ation) maps to -ize etc.
	 * Note that the string before the suffix must give m() > 0.
	 */
	private final void step3() {
		if (k == 0) {
			return;
		}
		switch (word[k-1]) {
		case 'a':
			if (ends("ational")) {
				r("ate");
				break;
			}
			if (ends("tional")) {
				r("tion");
				break;
			}
			break;
		case 'c':
			if (ends("enci")) {
				r("ence");
				break;
			}
			if (ends("anci")) { 
				r("ance");
				break;
			}
			break;
		case 'e': 
			if (ends("izer")) {
				r("ize");
				break; 
			}
			break;
		case 'l': 
			if (ends("bli")) { 
				r("ble");
				break; 
			}
			if (ends("alli")) {
				r("al");
				break; 
			}
			if (ends("entli")) {
				r("ent");
				break; 
			}
			if (ends("eli")) {
				r("e");
				break; }
			if (ends("ousli")) {
				r("ous");
				break; 
			}
			break;
		case 'o': 
			if (ends("ization")) {
				r("ize");
				break; 
			}
			if (ends("ation")) {
				r("ate");
				break;
			}
			if (ends("ator")) {
				r("ate");
				break; 
			}
			break;
		case 's':
			if (ends("alism")) {
				r("al");
				break; 
			}
			if (ends("iveness")) {
				r("ive");
				break; 
			}
			if (ends("fulness")) {
				r("ful");
				break; 
			}
			if (ends("ousness")) {
				r("ous");
				break; 
			}
			break;
		case 't': 
			if (ends("aliti")) {
				r("al");
				break; 
			}
			if (ends("iviti")) {
				r("ive");
				break;
			}
			if (ends("biliti")) {
				r("ble");
				break;
			}
			break;
		case 'g':
			if (ends("logi")) {
				r("log"); 
				break;
			}
		} 
	}

	/**
	 * step4() deals with -ic-, -full, -ness etc.
	 * Similar strategy to step3.
	 */
	private final void step4() {
		switch (word[k]) {
		case 'e': 
			if (ends("icate")) {
				r("ic");
				break; 
			}
			if (ends("ative")) {
				r("");
				break;
			}
			if (ends("alize")) {
				r("al"); 
				break; }
			break;
		case 'i': if (ends("iciti")) {
			r("ic"); 
			break; }
		break;
		case 'l': 
			if (ends("ical")) { 
				r("ic"); break; 
			}
			if (ends("ful")) {
				r(""); break; 
			}
			break;
		case 's':
			if (ends("ness")) {
				r(""); break; 
			}
			break;
		}
	}

	/**
	 * step5() takes off -ant, -ence etc., in context <c>vcvc<v>.
	 */
	private final void step5() {
		if (k == 0) {
			return;
		}
		switch (word[k-1]) {
		case 'a':
			if (ends("al")) {
				break;
			}
			return;
		case 'c':
			if (ends("ance")) {
				break;
			}
			if (ends("ence")) {
				break;
			}
			return;
		case 'e':
			if (ends("er")) {
				break;
			}
			return;
		case 'i':
			if (ends("ic")) {
				break;
			}
			return;
		case 'l':
			if (ends("able")) {
				break;
			}
			if (ends("ible")) {
				break;
			}
			return;
		case 'n':
			if (ends("ant")) {
				break;
			}
			if (ends("ement")) {
				break;
			}
			if (ends("ment")) {
				break;
			}
			// element etc. not stripped before the m
			if (ends("ent")) {
				break;
			}
			return;
		case 'o':
			if ((ends("ion")) && (j >= 0) && ((word[j] == 's') || (word[j] == 't'))) {
				break;
			}
			if (ends("ou")) {
				break;
			}
			return;
			// takes care of -ous
		case 's':
			if (ends("ism")) {
				break;
			}
			return;
		case 't':
			if (ends("ate")) {
				break;
			}
			if (ends("iti")) {
				break;
			}
			return;
		case 'u':
			if (ends("ous")) {
				break;
			}
			return;
		case 'v':
			if (ends("ive")) {
				break;
			}
			return;
		case 'z':
			if (ends("ize")) {
				break;
			}
			return;
		default:
			return;
		}
		if (m() > 1) {
			k = j;
		}
	}

	/**
	 * step6() removes a final -e if m() > 1.
	 */
	private final void step6() {
		j = k;
		if (word[k] == 'e') {
			int a = m();
			if ((a > 1) || (a == 1) && (!cvc(k-1))) {
				k--;
			}
		}
		if ((word[k] == 'l') && (doublec(k)) && (m() > 1)) {
			k--;
		}
	}

	/**
	 * Stem the word placed into the Stemmer buffer through calls to add().
	 * Returns true if the stemming process resulted in a word different
	 * from the input. You can retrieve the result with
	 * getResultLength()/getResultBuffer() or toString().
	 */
	public void stem() {
		k = i - 1;
		if (k > 1) {
			step1();
			step2();
			step3();
			step4();
			step5();
			step6();
		}
		i_end = k+1;
		i = 0;
	}

}
