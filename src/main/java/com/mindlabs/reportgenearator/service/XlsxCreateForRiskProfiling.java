package com.mindlabs.reportgenearator.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.mindlabs.reportgenearator.model.RiskProfiling;
@Service
public class XlsxCreateForRiskProfiling {
	private static final String[] COLUMN_HEADER = { "ACC_NO", "ACC_CLASS", "TENURE", "TXN_VOL", "TURNOVER",
			"RISK_SCORE" };

	public static void main(String[] args) throws IOException, SQLException {
		createXlsxForLowRiskCategory();
		createXlsxForHighRiskCategory();
		createXlsxForMediumRiskCategory();

	}

	public static void createXlsxForLowRiskCategory() throws IOException, SQLException

	{
		String xlsxpath = "/home/ranjith/Documents/highrisk/";
		Connection con = DataBaseConnection.getResult();
		try {
			String fileName = xlsxpath + "lowrisk";
			workBookCreator(fileName, con, "low");
			zipFile(fileName + ".xlsx");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con.close();

		}
	}

	public static void createXlsxForHighRiskCategory() throws IOException, SQLException

	{
		String xlsxpath = "/home/ranjith/Documents/highrisk/";
		Connection con = DataBaseConnection.getResult();
		try {
			String fileName = xlsxpath + "highrisk";
			workBookCreator(fileName, con, "high");
			zipFile(fileName + ".xlsx");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con.close();

		}
	}

	public static void createXlsxForMediumRiskCategory() throws IOException, SQLException

	{
		String xlsxpath = "/home/ranjith/Documents/highrisk/";
		Connection con = DataBaseConnection.getResult();
		try {
			String fileName = xlsxpath + "mediumRisk";
			workBookCreator(fileName, con, "medium");
			zipFile(fileName + ".xlsx");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con.close();

		}
	}

	private static void workBookCreator(String filename, Connection con, String status) throws IOException {
		FetchDetailsFromAmlDb profileTableDetails = new FetchDetailsFromAmlDb();
		int offSetVale = 0;
		XlsxCreateForRiskProfiling xlsxCreate = new XlsxCreateForRiskProfiling();
		String queryForFoundTotalCount = xlsxCreate.querySwitcherFetchTotalCount(status);
		int totalCount = profileTableDetails.getTotalCountOfRisk(con, queryForFoundTotalCount);
		for (int i = 0; i < 1; i++) {
			XlsxCreateForRiskProfiling ob1 = new XlsxCreateForRiskProfiling();
			Workbook workbook = new SXSSFWorkbook();
			File file = new File(filename + ".xlsx");
			FileOutputStream fileOut = null;
			int totalSheetCount = ob1.totalSheetCountFinder(totalCount);
			for (int j = 0; j <= totalSheetCount; j++) {
				String queryForFindElement = xlsxCreate.querySwitcherFetchElement(status, offSetVale);
				List<RiskProfiling> listProfilingData = profileTableDetails.getRiskMatchWithPriorityValueInfo(con,
						queryForFindElement);
				Sheet sheet = workbook.createSheet("LowRisk" + offSetVale);
				workBookBeautifier(sheet, workbook);
				fileOut = xlsxWriter(listProfilingData, sheet, file);
				offSetVale = ob1.offSetValueFinder(totalCount, offSetVale);

			}
			workbook.write(fileOut);
			if (fileOut != null) {
				fileOut.flush();
				fileOut.close();
			}
		}
	}

	private String querySwitcherFetchElement(String priorityStatus, int offsetValue) {

		QueryBuilder builder = new QueryBuilder();
		switch (priorityStatus) {
		case "medium":
			return builder.getQueryForMediumRiskPriority(offsetValue);

		case "high":
			return builder.getQueryForHighRiskPriority(offsetValue);
		case "low":
			return builder.getQueryForLowPriority(offsetValue);
		default:
			System.out.println("failure case found");
			break;
		}
		return "";

	}

	private String querySwitcherFetchTotalCount(String priorityStatus) {

		QueryBuilder builder = new QueryBuilder();
		switch (priorityStatus) {
		case "medium":
			return builder.getQueryForTotalCountMedRisk();
		case "high":
			return builder.getQueryForTotalCountHighRisk();
		case "low":
			return builder.getQueryForTotalCountLowRisk();
		default:
			System.out.println("failure case found");
			break;
		}
		return "";

	}

	private static void workBookBeautifier(Sheet sheet, Workbook workbook) {

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

	private static void zipFile(String filePath) {
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

	int totalSheetCountFinder(int totalCount) {

		return totalCount / 10000;

	}

	int offSetValueFinder(int totalCount, int offSetValue) {

		if (offSetValue < totalCount) {
			return offSetValue + 10000;
		}
		return 0;
	}

	private static FileOutputStream xlsxWriter(List<RiskProfiling> fieldValue, Sheet sheet, File file)
			throws IOException {

		int rowNum = 1;
		for (int i = 0; i < fieldValue.size(); i++) {
			Row row = sheet.createRow(rowNum++);
			RiskProfiling profiling = fieldValue.get(i);

			row.createCell(0).setCellValue(profiling.getAcc_no());
			row.createCell(1).setCellValue(profiling.getAccClass());

			row.createCell(2).setCellValue(profiling.getRiskScore());
			row.createCell(3).setCellValue(profiling.getTenure());
			row.createCell(4).setCellValue(profiling.getTrunOver());
			row.createCell(5).setCellValue(profiling.getTxnVol());

		}
		for (int i = 0; i < COLUMN_HEADER.length; i++) {
			sheet.autoSizeColumn(COLUMN_HEADER[i].length() + 20);
		}
		return new FileOutputStream(file);
	}

}
