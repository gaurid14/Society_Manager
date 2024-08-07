package com.example.societymanager.service;

import com.example.societymanager.controller.BillController;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ExcelService {
    private static final Logger Log=LogManager.getLogger(BillController.class);

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/society_management";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "pgadmin4";

    public void processExcelFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
             Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            int mem_id=0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            PreparedStatement preparedStatement = conn.prepareStatement("select max(sid+1) as next_mem_id from resident where sid=");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                mem_id = resultSet.getInt("next_mem_id");
            }

            String insertQuery = "insert into resident (mem_id, name, room_no, mr_ms, gender, age, contact_no, sid, isadmin) values (?, ?, ?, ?, ?, ?, ?, ?, false)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        // Skip header row
                        continue;
                    }

                    // Process each cell based on its type
                    pstmt.setInt(1, mem_id++); // Set mem_id and increment it for next resident
                    pstmt.setString(2, getCellValueAsString(row.getCell(0))); // name
                    pstmt.setInt(3, (int) row.getCell(1).getNumericCellValue()); // room_no
                    pstmt.setString(4, getCellValueAsString(row.getCell(2))); // mr_ms
                    pstmt.setString(5, getCellValueAsString(row.getCell(3))); // gender
                    pstmt.setInt(6, (int) row.getCell(4).getNumericCellValue()); // age
                    pstmt.setString(7, getCellValueAsString(row.getCell(5))); // contact_no
                    pstmt.setInt(8, (int) row.getCell(6).getNumericCellValue()); // sid

                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error processing Excel file", e);
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Handle numeric cells, converting to string
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Handle formula cells
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
