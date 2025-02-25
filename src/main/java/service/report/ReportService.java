package service.report;

import view.model.SaleDTO;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import service.user.AuthenticationService;

public class ReportService {
    public void generateSalesReport(String filePath, List<SaleDTO> sales, AuthenticationService authenticationService) {
        try {
            PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Sales Report")
                    .setBold()
                    .setFontSize(18)
                    .setMarginBottom(20));

            // Group sales by user_id (seller)
            Map<String, List<SaleDTO>> salesBySeller = sales.stream()
                    .collect(Collectors.groupingBy(SaleDTO::getSeller));

            for (Map.Entry<String, List<SaleDTO>> entry : salesBySeller.entrySet()) {
                String seller = entry.getKey();
                List<SaleDTO> sellerSales = entry.getValue();

                // Add seller's header
                document.add(new Paragraph("Seller: " + authenticationService.FindByID(seller))
                        .setBold()
                        .setFontSize(14)
                        .setMarginBottom(10));

                float[] columnWidths = {100, 50, 100, 100, 100};
                Table table = new Table(columnWidths);

                // Add table headers
                table.addCell(new Paragraph("Book Title"));
                table.addCell(new Paragraph("Quantity"));
                table.addCell(new Paragraph("Total Price"));
                table.addCell(new Paragraph("Seller"));
                table.addCell(new Paragraph("Sell Date"));

                // Add sales data for the seller
                for (SaleDTO sale : sellerSales) {
                    table.addCell(sale.getBookTitle());
                    table.addCell(String.valueOf(sale.getQuantity()));
                    table.addCell(String.valueOf(sale.getTotalPrice()));
                    table.addCell(sale.getSeller());
                    table.addCell(String.valueOf(sale.getSellDate()));
                }

                // Add the table to the document
                document.add(table);

                // Add some spacing before the next seller's table
                document.add(new Paragraph("\n"));
            }

            document.close();

            System.out.println("PDF was generated at " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
