package com.fa.ims.service;

import com.fa.ims.entity.Offer;
import com.fa.ims.entity.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ExportExcelOfferService {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<Offer> listOffer;

    public ExportExcelOfferService(List<Offer> listOffer) {
        this.listOffer = listOffer;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Offers");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Candidate name", style);
        createCell(row, 1, "E-mail", style);
        createCell(row, 2, "Approver", style);
        createCell(row, 3, "Department", style);
        createCell(row, 4, "Notes", style);
        createCell(row, 5, "Status", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Offer offer : listOffer) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, offer.getCandidate().getFullName(), style);
            createCell(row, columnCount++, offer.getCandidate().getEmail(), style);
            createCell(row, columnCount++, offer.getApprovedBy().getUsername(), style);
            createCell(row, columnCount++, offer.getDepartment().getDisplayValue(), style);
            createCell(row, columnCount++, offer.getNote(), style);
            createCell(row, columnCount++, offer.getOfferStatus().getDisplayValue(), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
