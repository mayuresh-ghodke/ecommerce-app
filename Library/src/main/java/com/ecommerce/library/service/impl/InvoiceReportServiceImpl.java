//package com.ecommerce.library.service.impl;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
//import org.springframework.stereotype.Service;
//
//import com.ecommerce.library.model.Order;
//import com.ecommerce.library.service.InvoiceReportService;
//
//import lombok.RequiredArgsConstructor;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import java.awt.Color;
//
//@Service
//@RequiredArgsConstructor
//public class InvoiceReportServiceImpl implements InvoiceReportService{
//
//    public static final PDType1Font HELVETICA_BOLD = new PDType1Font(Standard14Fonts.FontName.valueOf("Helvetica-Bold"));
//    @Override
//    public void generateInvoicePDF(List<Order> orders) {
//
//        try (PDDocument document = new PDDocument()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.setFont(HELVETICA_BOLD, 12);
//
//                contentStream.setNonStrokingColor(Color.BLACK);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(100, 700);
//                contentStream.showText("Invoice Report");
//                contentStream.newLineAtOffset(0, -20);
//
//                int y = 650; // Starting y-coordinate for the table
//                int rowHeight = 20; // Height of each row
//                int columnWidth = 100; // Width of each column
//
//                // Draw table headers
//                contentStream.setFont(HELVETICA_BOLD, 10);
//                contentStream.setNonStrokingColor(Color.BLACK);
//                contentStream.newLineAtOffset(100, y);
//                contentStream.showText("ORD-ID");
//                contentStream.newLineAtOffset(columnWidth, 0);
//                contentStream.showText("CUSTOMER");
//                contentStream.newLineAtOffset(columnWidth, 0);
//                contentStream.showText("ORDER PLACED DATE");
//                // Add more headers as needed
//
//                // Draw table content
//                contentStream.setFont(HELVETICA_BOLD, 12);
//                for (Order order : orders) {
//                    y -= rowHeight; // Move to next row
//                    contentStream.newLineAtOffset(-2 * columnWidth, -rowHeight); // Move to the start of next row
//                    contentStream.showText(String.valueOf(order.getId()));
//                    contentStream.newLineAtOffset(columnWidth, 0);
//                    contentStream.showText(order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName());
//                    contentStream.newLineAtOffset(columnWidth, 0);
//                    contentStream.showText(order.getOrderDate().toString());
//                    // Add more fields as needed
//                }
//
//                contentStream.endText();
//            }
//
//            document.save("Invoice_Report.pdf");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}