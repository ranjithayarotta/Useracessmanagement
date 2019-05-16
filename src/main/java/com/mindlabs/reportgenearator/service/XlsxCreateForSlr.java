package com.mindlabs.reportgenearator.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.mindlabs.reportgenearator.dao.DataBaseConnection;
import com.mindlabs.reportgenearator.dao.FetchDetailsFromAmlDb;
import com.mindlabs.reportgenearator.model.SuspeciousTransaction;

@Service
public class XlsxCreateForSlr {

	private static final String[] COLUMN_HEADER = { "State name", "accountNumber", "alertDate", "EntrySrNo", "STRCode",
			"Branch name" };

	public ByteArrayInputStream createXlsxForSlr() throws IOException, SQLException

	{
		Connection con = DataBaseConnection.getResult();
		try {
			String fileName = "SlrReport";
			return workBookCreator(fileName, con);
			// zipFile(fileName + ".xlsx");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con.close();

		}
		return null;
	}

	private ByteArrayInputStream workBookCreator(String filename, Connection con) throws IOException {
		FetchDetailsFromAmlDb profileTableDetails = new FetchDetailsFromAmlDb();
		int offSetVale = 0;
		QueryBuilder builder = new QueryBuilder();

		String queryForFoundTotalCount = builder.getQueryForTotalCountOfSuspeciousTransaction();
		int totalCount = profileTableDetails.getTotalCountOfRisk(con, queryForFoundTotalCount);
		for (int i = 0; i < 1; i++) {
			XlsxCreateForRiskProfiling ob1 = new XlsxCreateForRiskProfiling();
			Workbook workbook = new SXSSFWorkbook();
			File file = new File(filename + ".xlsx");
			OutputStream fileOut = null;
			int totalSheetCount = ob1.totalSheetCountFinder(totalCount);
			for (int j = 0; j <= totalSheetCount; j++) {
				String queryForFindElement = builder.getQueryForSelectSuspeciousTransaction();
				List<SuspeciousTransaction> listProfilingData = profileTableDetails
						.getSuspeciousCashTransactionInfo(con, queryForFindElement);
				Sheet sheet = workbook.createSheet("Slr" + offSetVale);
				workBookBeautifier(sheet, workbook);
				fileOut = xlsxWriter(listProfilingData, sheet, file);
				offSetVale = ob1.offSetValueFinder(totalCount, offSetVale);
			}
			workbook.write(fileOut);
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
			ByteArrayOutputStream bos = (ByteArrayOutputStream) fileOut;
			return new ByteArrayInputStream(bos.toByteArray());

		}
		return null;
	}

	private void workBookBeautifier(Sheet sheet, Workbook workbook) {

		Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		Row headerRow = sheet.createRow(0);
		for (int k = 0; k < COLUMN_HEADER.length; k++) {
			Cell cell = headerRow.createCell(k);
			cell.setCellValue(COLUMN_HEADER[k]);
			cell.setCellStyle(headerCellStyle);
		}

	}

	private void zipFile(String filePath) {
		try {
			File file = new File(filePath);
			String zipFileName = file.getName().concat(".zip");

			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			zos.putNextEntry(new ZipEntry(file.getName()));

			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			zos.write(bytes, 0, bytes.length);
			zos.closeEntry();
			zos.close();

		} catch (FileNotFoundException ex) {
			System.err.format("The file %s does not exist", filePath);
		} catch (IOException ex) {
			System.err.println("I/O error: " + ex);
		}
	}

	private FileOutputStream xlsxWriter(List<SuspeciousTransaction> fieldValue, Sheet sheet, File file)
			throws IOException {

		int rowNum = 1;
		for (int i = 0; i < fieldValue.size(); i++) {
			Row row = sheet.createRow(rowNum++);
			SuspeciousTransaction largeCashTransactions = fieldValue.get(i);
			row.createCell(0).setCellValue(largeCashTransactions.getState());
			row.createCell(1).setCellValue(largeCashTransactions.getAccountnumber());
			row.createCell(2).setCellValue(largeCashTransactions.getAlertDate());
			row.createCell(3).setCellValue(largeCashTransactions.getEntrySrNumber());
			row.createCell(4).setCellValue(largeCashTransactions.getStrCode());
			row.createCell(5).setCellValue(largeCashTransactions.getBrachname());

		}
		for (int i = 0; i < COLUMN_HEADER.length; i++) {
			sheet.autoSizeColumn(COLUMN_HEADER[i].length() + 20);
		}
		return new FileOutputStream(file);
	}

}
