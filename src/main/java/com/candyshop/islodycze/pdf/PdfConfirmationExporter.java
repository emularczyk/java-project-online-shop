package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.itextpdf.io.font.constants.StandardFonts;
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

import javax.servlet.http.HttpServletResponse;
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

        usersDetails(document);
        newPdfParagraph(document, "Rodzaj przesyłki: " + delivery.getMode());
        newPdfParagraph(document, "\n");

        addProductsTable(document);

        document.close();
    }

    private void newPdfParagraph(Document document, String text) {
        newPdfParagraph(document, text, TextAlignment.LEFT, 12);
    }

    private void newPdfParagraph(Document document, String text, TextAlignment alignment, float size) {
        Paragraph paragraph = new Paragraph(text);
        try {
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);
            paragraph.setFont(font);
        } catch (IOException e) {
        }
        paragraph.setTextAlignment(TextAlignment.LEFT);
        document.add(paragraph);
    }

    private void usersDetails(Document document) {
        newPdfParagraph(document, "Faktura za zakupy w Isłodycze", TextAlignment.CENTER, 32);
        newPdfParagraph(document, "Numer zamowienia: " + order.getOrderId());
        newPdfParagraph(document, "");
        newPdfParagraph(document, "Dane odbiorcy: ", TextAlignment.LEFT, 32);
        newPdfParagraph(document, "Imie: " + delivery.getName());
        newPdfParagraph(document, "Nazwisko: " + delivery.getSurname());
        newPdfParagraph(document, "Email odbiorcy: " + order.getUserIdFk().getEmail());
        newPdfParagraph(document, "Address: " + delivery.getAddress());
        newPdfParagraph(document, "Kod pocztowy: " + delivery.getPostalCode());
        newPdfParagraph(document, "Miasto: " + delivery.getCity());
        newPdfParagraph(document, ":  " + delivery.getCountry());
        newPdfParagraph(document, "Telefon: " + delivery.getPhoneArea() + " " + delivery.getPhoneNumber());
    }

    private void addProductsTable (Document document){
        Table ordersTable = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();
        writeTableHeader(ordersTable);
        writeTableData(ordersTable);
        document.add(ordersTable);
    }

    private void writeTableHeader(Table table) {
        Cell cell = new Cell();
        cell.setPadding(4);
        cell.add(new Paragraph(" "));
        table.addCell(cell);
        cell.add(new Paragraph("Nazwa produktu"));
        table.addCell(cell);
        cell.add(new Paragraph("Cena produktu"));
        table.addCell(cell);
        cell.add(new Paragraph("Ilosc"));
        table.addCell(cell);
    }

    private void writeTableData(Table table) {
        int i = 0;
        for (ProductOrder productOrder : order.getProductOrder()) {
            table.addCell(String.valueOf(i++));
            table.addCell(String.valueOf(productOrder.getProductFk().getProductName()));
            table.addCell(String.valueOf(productOrder.getProductFk().getPrice()));
            table.addCell(String.valueOf(productOrder.getAmount()));
        }
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        table.addCell(cell);
        table.addCell("Razem:");
        table.addCell(order.getTotalCost() + " zl");
    }

}
