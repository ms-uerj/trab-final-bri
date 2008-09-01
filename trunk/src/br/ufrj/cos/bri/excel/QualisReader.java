package br.ufrj.cos.bri.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import br.ufrj.cos.bri.util.db.mysql.MysqlConnector;

public class QualisReader {

	public final static String TABLE_QUALIS = "QUALIS";

	public final static String COLUMN_ISSN = "issn";
	public final static String COLUMN_TITULO = "title";
	public final static String COLUMN_NIVEL = "qualis";
	public final static String COLUMN_CIRCULACAO = "circulation";

	private String file = "input/Periodicos_Qualis_02.xls";

	public QualisReader() {

	}
	
	public String getQualis(String articleName){
		String qualisLevel = null;
		
		MysqlConnector conn = new MysqlConnector();
		conn.connect();
		ResultSet result = conn.query("SELECT "+COLUMN_NIVEL+" FROM "+TABLE_QUALIS+" WHERE "+COLUMN_TITULO+" = '"+articleName+"'");

		try {
			while(result.next()){
				qualisLevel = result.getString(COLUMN_NIVEL);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		conn.disconnect();
		return qualisLevel;
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

		MysqlConnector conn = new MysqlConnector();

		List<String> rowValues = new ArrayList<String>();

		POIFSFileSystem fs;
		String sql;

		conn.connect();

		if (conn.isConnected()) {
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

					sql = "INSERT INTO " + QualisReader.TABLE_QUALIS + "("
							+ QualisReader.COLUMN_ISSN + ", "
							+ QualisReader.COLUMN_TITULO + ", "
							+ QualisReader.COLUMN_NIVEL + ", "
							+ QualisReader.COLUMN_CIRCULACAO + ") VALUES('"
							+ rowValues.get(0) + "', '" + rowValues.get(1)
							+ "', '" + rowValues.get(2) + "', '"
							+ rowValues.get(3) + "')";

					conn.exec(sql);
					rowValues.clear();

				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		conn.disconnect();

	}
}
