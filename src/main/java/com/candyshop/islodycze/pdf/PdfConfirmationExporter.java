package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class PdfConfirmationExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfConfirmationExporter(Order order, DeliveryEntity delivery) {
        this.order = order;
        this.delivery = delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont("C:/Windows/Fonts/arial.TTF");
        document.setFont(font);

        usersDetails(document);
        PdfService.newPdfParagraph(document, "Rodzaj przesyłki: " + delivery.getMode());
        PdfService.newPdfParagraph(document, "\n");

        addProductsTable(document);

        document.close();
    }

    private void usersDetails(Document document) {
        PdfService.newPdfParagraph(document, "Faktura za zakupy w iSłodycze", TextAlignment.LEFT, 32);
        PdfService.newPdfParagraph(document, "\n");
        PdfService.newPdfParagraph(document, "Numer zamówienia: " + order.getOrderId(), TextAlignment.LEFT, 14);
        PdfService.newPdfParagraph(document, "\n");
        PdfService.newPdfParagraph(document, "Imię: " + delivery.getName());
        PdfService.newPdfParagraph(document, "Nazwisko: " + delivery.getSurname());
        PdfService.newPdfParagraph(document, "E-mail: " + order.getUserIdFk().getEmail());
        PdfService.newPdfParagraph(document, "Adres: " + delivery.getAddress());
        PdfService.newPdfParagraph(document, "Kod pocztowy: " + delivery.getPostalCode());
        PdfService.newPdfParagraph(document, "Miasto: " + delivery.getCity());
        PdfService.newPdfParagraph(document, "Państwo:  " + delivery.getCountry());
        PdfService.newPdfParagraph(document, "Numer telefonu: " + delivery.getPhoneArea() + " " + delivery.getPhoneNumber());
    }

    private void addProductsTable(Document document) {
        Table ordersTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        writeTableHeader(ordersTable);
        writeTableData(ordersTable);
        writeTableSumUp(ordersTable);
        document.add(ordersTable);

    }

    private void writeTableHeader(Table table) {
        Cell cell = new Cell();
        cell.setPadding(4);
        table.addCell(new Paragraph(" "));
        table.addCell(new Paragraph("Nazwa produktu"));
        table.addCell(new Paragraph("Cena produktu"));
        table.addCell(new Paragraph("Ilość"));
    }

    private void writeTableData(Table table) {
        int i = 0;
        for (ProductOrder productOrder : order.getProductOrder()) {
            table.addCell(String.valueOf(++i));
            table.addCell(String.valueOf(productOrder.getProductFk().getProductName()));
            table.addCell(String.valueOf(productOrder.getProductFk().getPrice() + " zł"));
            table.addCell(String.valueOf(productOrder.getAmount()));
        }
    }

    private void writeTableSumUp(Table table) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Paragraph("Razem:"));
        table.addCell(new Paragraph(order.getTotalCost() + " zł"));
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Paragraph("Dostawa:"));
        table.addCell(new Paragraph(delivery.getDeliveryCost() + " zł"));
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Paragraph("Suma:"));
        table.addCell(new Paragraph(order.getTotalCost().add(delivery.getDeliveryCost()) + " zł"));
    }
}
