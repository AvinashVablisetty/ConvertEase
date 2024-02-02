package com.convertease.xlsxtohtml.service;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@Service
public class FileConverterService {

	private byte[] htmlContent;

	public void convertAndSave(MultipartFile file) throws IOException {
		try {
			File convertedFile = convertToHtml(file);
			//FileInputStream fis = new FileInputStream(convertedFile);
	        this.htmlContent = Files.readAllBytes(convertedFile.toPath());
			//fis.close();
			System.out.println("XLS to HTML conversion complete!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File convertToHtml(MultipartFile file) throws IOException {
		String projectRoot = System.getProperty("user.dir");
	    String htmlFilePath = "\\OutputFile\\output.html";
	    String htmlFile = projectRoot + htmlFilePath;

	    File convertedFile = new File(htmlFile);
	    try (InputStream inputStream = file.getInputStream();
	         Workbook workbook = new HSSFWorkbook(inputStream);
	         FileOutputStream fos = new FileOutputStream(convertedFile);
	         PrintWriter pw = new PrintWriter(fos))  {

			// Your existing logic for converting XLS to HTML

			pw.println("<html>\n<body>");
			// ... rest of the code

			pw.println("<div id=\"sheetsContainer\"></div>");

			// JavaScript for tab functionality
			pw.println("<script>");
			pw.println("var sheetsContainer = document.getElementById('sheetsContainer');");
			pw.println("function showSheet(sheetName) {");
			pw.println("  var sheets = document.getElementsByClassName('sheet');");
			pw.println("  for (var i = 0; i < sheets.length; i++) {");
			pw.println("    sheets[i].style.display = 'none';");
			pw.println("  }");
			pw.println("  var sheet = document.getElementById(sheetName);");
			pw.println("  sheet.style.display = 'block';");
			pw.println("}");
			pw.println("document.addEventListener('DOMContentLoaded', function() {");
			pw.println("  var sheets = document.getElementsByClassName('sheet');");
			pw.println("  for (var i = 1; i < sheets.length; i++) {");
			pw.println("    sheets[i].style.display = 'none';");
			pw.println("  }");
			pw.println("});");
			pw.println("</script>");

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				pw.println("<div id=\"" + sheetName + "\" class=\"sheet\">");
				pw.println("<h2>Sheet: " + sheetName + "</h2>");
				pw.println("<table border='1'>");

				for (Row row : sheet) {
					pw.println("<tr>");

					for (Cell cell : row) {
						pw.println("<td>");

						switch (cell.getCellType()) {
						case STRING:
							pw.println(cell.getRichStringCellValue().getString());
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								pw.println(cell.getDateCellValue());
							} else {
								pw.println(cell.getNumericCellValue());
							}
							break;
						case BOOLEAN:
							pw.println(cell.getBooleanCellValue());
							break;
						case FORMULA:
							pw.println(cell.getCellFormula());
							break;
						default:
							pw.println("");
						}

						pw.println("</td>");
					}

					pw.println("</tr>");
				}

				pw.println("</table>");
				pw.println("</div>");
			}

			pw.println("<div style=\"text-align: left;\">");
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				pw.println("<button onclick=\"showSheet('" + sheetName + "')\">" + sheetName + "</button>");
			}
			pw.println("</div>");

			pw.println("</body>\n</html>");

			pw.close();
			fos.close();

			System.out.println("XLS to HTML conversion complete!");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return convertedFile;
	}

	public byte[] getHtmlContent() {
		if (this.htmlContent == null) {
			return new byte[0];
		}
		return this.htmlContent;
	}
}
