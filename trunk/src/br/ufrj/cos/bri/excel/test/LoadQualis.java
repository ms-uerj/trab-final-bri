package br.ufrj.cos.bri.excel.test;

import java.sql.Connection;
import java.sql.DriverManager;

import br.ufrj.cos.bri.excel.QualisReader;

public class LoadQualis {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		QualisReader qualisReader = new QualisReader();
		qualisReader.printContents();
		qualisReader.loadDatabase();
		
	}

}
