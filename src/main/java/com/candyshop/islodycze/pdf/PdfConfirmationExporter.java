package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PdfConfirmationExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfConfirmationExporter(Order order, DeliveryEntity delivery) {
        this.order = order;
        this.delivery = delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        BaseFont arial = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(arial, 12);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = order.getOrderDate().format(customFormat);
        newPdfParagraph(document, font, formattedDate, Paragraph.ALIGN_RIGHT, 12);

        usersDetails(document, font);

        newPdfParagraph(document, font, "Rodzaj przesyłki: " + delivery.getMode());
        newPdfParagraph(document, font, "\n");

        PdfPTable ordersTable = new PdfPTable(4);
        ordersTable.setWidthPercentage(100);

        writeTableHeader(ordersTable, font);
        writeTableData(ordersTable, font);

        document.add(ordersTable);

        document.close();
    }

    private void newPdfParagraph(Document document, Font font, String text) {
        newPdfParagraph(document, font, text, Paragraph.ALIGN_LEFT, 12);
    }

    private void newPdfParagraph(Document document, Font font, String text, int alignment, float size) {
        Paragraph paragraph = new Paragraph(text, font);

        paragraph.getFont().setSize(size);
        paragraph.setAlignment(alignment);

        document.add(paragraph);
    }

    private void usersDetails(Document document, Font font) {
        newPdfParagraph(document, font, "Faktura za zakupy w iSłodycze", Paragraph.ALIGN_CENTER, 32);
        newPdfParagraph(document, font, "\n");
        newPdfParagraph(document, font, "Numer zamówienia: " + order.getOrderId(), Paragraph.ALIGN_LEFT, 14);
        newPdfParagraph(document, font, "\n");
        newPdfParagraph(document, font, "Imię: " + delivery.getName());
        newPdfParagraph(document, font, "Nazwisko: " + delivery.getSurname());
        newPdfParagraph(document, font, "E-mail: " + order.getUserIdFk().getEmail());
        newPdfParagraph(document, font, "Adres: " + delivery.getAddress());
        newPdfParagraph(document, font, "Kod pocztowy: " + delivery.getPostalCode());
        newPdfParagraph(document, font, "Miasto: " + delivery.getCity());
        newPdfParagraph(document, font, "Państwo:  " + delivery.getCountry());
        newPdfParagraph(document, font, "Numer telefonu: " + delivery.getPhoneArea() + " " + delivery.getPhoneNumber());
    }

    private void writeTableHeader(PdfPTable table, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(4);
        cell.setPhrase(new Phrase("", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nazwa produktu", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cena produktu", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Ilość", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, Font font) {
        int i = 0;
        for (ProductOrder productOrder : order.getProductOrder()) {
            table.addCell(String.valueOf(++i));
            table.addCell(String.valueOf(productOrder.getProductFk().getProductName()));
            table.addCell(String.valueOf(productOrder.getProductFk().getPrice()));
            table.addCell(String.valueOf(productOrder.getAmount()));
        }
        PdfPCell cell = new PdfPCell();

        cell.setBorder(0);
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Phrase("Razem:", font));
        table.addCell(new Phrase(order.getTotalCost() + " zł", font));
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Phrase("Dostawa:", font));
        table.addCell(new Phrase(delivery.getDeliveryCost() + " zł", font));
        table.addCell(cell);
        table.addCell(cell);
        table.addCell(new Phrase("Suma:", font));
        table.addCell(new Phrase(order.getTotalCost().add(delivery.getDeliveryCost()) + " zł", font));
    }

}
