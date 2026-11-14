package com.mentorbridge.utils;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utility to export JTable data or custom records to CSV.
 */
public class CsvExporter {

    public static boolean exportTableToCsv(JTable table, File targetFile) {
        if (table == null || targetFile == null) return false;
        try (PrintWriter writer = new PrintWriter(new FileWriter(targetFile))) {
            TableModel model = table.getModel();
            int colCount = model.getColumnCount();
            int rowCount = model.getRowCount();

            // Header
            for (int col = 0; col < colCount; col++) {
                writer.print(escapeCsv(model.getColumnName(col)));
                if (col < colCount - 1) writer.print(",");
            }
            writer.println();

            // Rows
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    Object val = model.getValueAt(row, col);
                    writer.print(escapeCsv(val != null ? val.toString() : ""));
                    if (col < colCount - 1) writer.print(",");
                }
                writer.println();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String escapeCsv(String data) {
        if (data == null) return "";
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }
}
