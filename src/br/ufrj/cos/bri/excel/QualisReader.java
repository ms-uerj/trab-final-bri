package br.ufrj.cos.bri.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class QualisReader {

	public final static String TABLE_QUALIS = "QUALIS";

	public final static String COLUMN_ISSN = "issn";
	public final static String COLUMN_TITULO = "titulo";
	public final static String COLUMN_NIVEL = "nivel";
	public final static String COLUMN_CIRCULACAO = "circulacao";

	private String file = "input/Periodicos_Qualis_02.xls";

	public QualisReader() {

	}
	/**
	 * Imprime na saída do console o conteúdo da planilha
	 */
	public void printContents() {
		String out = "";

		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(new FileInputStream(file));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet
					.rowIterator(); rit.hasNext();) {
				HSSFRow row = rit.next();
				for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row
						.cellIterator(); cit.hasNext();) {
					HSSFCell cell = cit.next();
					out += cell.toString() + "\t";
				}
				System.out.println(out);
				out = "";
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Carrega para o banco de dados o conteúdo da planilha
	 */
	public void loadDatabase() {
		try {

			// TODO: Criar classe para criação da conexão, para que a mesma
			// conexão possa ser compartilhada por toda aplicação
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost/bri", "root", "");

			Statement stmt = con.createStatement();

			List<String> rowValues = new ArrayList<String>();

			POIFSFileSystem fs;
			try {
				fs = new POIFSFileSystem(new FileInputStream(file));
				HSSFWorkbook wb = new HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);

				for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>) sheet
						.rowIterator(); rit.hasNext();) {
					HSSFRow row = rit.next();
					for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>) row
							.cellIterator(); cit.hasNext();) {
						HSSFCell cell = cit.next();
						rowValues.add(cell.toString());
					}
					stmt.executeUpdate("INSERT INTO "
							+ QualisReader.TABLE_QUALIS + "("
							+ QualisReader.COLUMN_ISSN + ", "
							+ QualisReader.COLUMN_TITULO + ", "
							+ QualisReader.COLUMN_NIVEL + ", "
							+ QualisReader.COLUMN_CIRCULACAO + ") VALUES('"
							+ rowValues.get(0) + "', '" + rowValues.get(1)
							+ "', '" + rowValues.get(2) + "', '"
							+ rowValues.get(3) + "')");
					rowValues.clear();

				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
