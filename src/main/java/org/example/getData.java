package org.example;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class getData  extends  GetParams{
    public static void getdatadb()
    {


        String driver =GetParams.ReadFile().get("driver");
        String url = GetParams.ReadFile().get("url");
        String login = GetParams.ReadFile().get("login");
        String password =GetParams.ReadFile().get("password");
        String requete =GetParams.ReadFile().get("requete");
        String localDirectory = GetParams.ReadFile().get("path");


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        FileOutputStream output = null;

        try {
            // Load and register drivers
            Class.forName(driver);

            // Establish a connection
            connection = DriverManager.getConnection(url
                    , login, password);

            // Process the statement
            statement = connection.createStatement();
            resultSet = statement.executeQuery(requete);

            // Create a workbook
            XSSFWorkbook workbook = new XSSFWorkbook();

            // Create a spreadsheet inside the workbook
            XSSFSheet spreadsheet = workbook.createSheet("datasheet");

            // Get result set metadata
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create header row
            XSSFRow headerRow = spreadsheet.createRow(0);
            for (int i = 1; i <= columnCount; i++) {
                XSSFCell cell = headerRow.createCell(i - 1);
                cell.setCellValue(metaData.getColumnName(i));
            }

            // Write data rows
            int rowIndex = 1;
            while (resultSet.next()) {
                XSSFRow row = spreadsheet.createRow(rowIndex++);
                for (int i = 1; i <= columnCount; i++) {
                    XSSFCell cell = row.createCell(i - 1);
                    Object value = resultSet.getObject(i);
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // Local directory on computer
            File file = new File(localDirectory);
            if (file.exists())
            {
                file.delete();
            }

            // Retry mechanism for file output stream
            int attempts = 5;
            while (attempts > 0) {
                try {
                    output = new FileOutputStream(file);
                    break;
                } catch (java.io.FileNotFoundException e) {
                    attempts--;
                    if (attempts == 0) {
                        throw e;
                    }
                    Thread.sleep(1000); // Wait for 1 second before retrying
                }
            }

            // Write to the Excel file
            workbook.write(output);
            System.out.println("xlsx written successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
                if (output != null) output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
