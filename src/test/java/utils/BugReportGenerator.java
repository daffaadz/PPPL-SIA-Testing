package utils;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class BugReportGenerator {

    public static void generatePdfReport(List<BugReportData> reportDataList, String outputPath) {
        Document document = new Document();
        try {
            File outputFile = new File(outputPath);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph header = new Paragraph("BUG REPORT", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font textFont = new Font(Font.HELVETICA, 10, Font.NORMAL);

            int count = 1;
            for (BugReportData data : reportDataList) {
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1.5f, 4.5f});
                table.setSpacingAfter(30f); // Space between tables

                // Helper method to add row
                addRow(table, "No", String.valueOf(count), labelFont, textFont);
                addRow(table, "Title", data.getTitle(), labelFont, textFont);
                
                // Description (append URL if needed, we'll just use description)
                addRow(table, "Description", "url: test.websistem.com\n" + data.getDescription(), labelFont, textFont);
                
                addRow(table, "Platform", data.getPlatform(), labelFont, textFont);

                // Steps to reproduce
                StringBuilder steps = new StringBuilder();
                if (data.getStepsToReproduce() != null) {
                    int stepNum = 1;
                    for (String step : data.getStepsToReproduce()) {
                        steps.append(stepNum).append(". ").append(step).append("\n");
                        stepNum++;
                    }
                }
                addRow(table, "Step to Reproduce", steps.toString().trim(), labelFont, textFont);
                
                // Expectation
                // The image shows expectation as a numbered list, we'll format it a bit
                String expectation = "1. " + data.getExpectation();
                addRow(table, "Expectation", expectation, labelFont, textFont);
                
                // Priority, Severity
                addRow(table, "Priority, Severity", data.getPriority() + ", " + data.getSeverity(), labelFont, textFont);

                // Attachment
                PdfPCell labelCell = new PdfPCell(new Paragraph("Attachment", labelFont));
                labelCell.setPadding(5);
                table.addCell(labelCell);

                PdfPCell imageCell = new PdfPCell();
                imageCell.setPadding(5);
                if (data.getAttachmentPath() != null) {
                    try {
                        Image img = Image.getInstance(data.getAttachmentPath());
                        // Scale to fit within the cell width
                        img.scaleToFit(300, 300);
                        imageCell.addElement(img);
                    } catch (Exception e) {
                        imageCell.addElement(new Paragraph("Screenshot unavailable: " + e.getMessage(), textFont));
                    }
                } else {
                    imageCell.addElement(new Paragraph("No screenshot available.", textFont));
                }
                table.addCell(imageCell);

                document.add(table);
                count++;
            }

            document.close();
            System.out.println("Bug report generated: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addRow(PdfPTable table, String label, String value, Font labelFont, Font textFont) {
        PdfPCell cell1 = new PdfPCell(new Paragraph(label, labelFont));
        cell1.setPadding(5);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Paragraph(value, textFont));
        cell2.setPadding(5);
        table.addCell(cell2);
    }
}
