package br.ufrj.cos.bri.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class QualisReader {

	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(new FileInputStream(
					"input/Periodicos_Qualis_02.xls"));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			for (Iterator<HSSFRow> rit = (Iterator<HSSFRow>)sheet.rowIterator(); rit.hasNext(); ) {
				HSSFRow row = rit.next();
				for (Iterator<HSSFCell> cit = (Iterator<HSSFCell>)row.cellIterator(); cit.hasNext(); ) {
					HSSFCell cell = cit.next();
					System.out.println(cell.toString());
				}
			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
