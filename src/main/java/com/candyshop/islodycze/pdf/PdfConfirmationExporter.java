package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.model.ProductOrder;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PdfConfirmationExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfConfirmationExporter(Order order,DeliveryEntity delivery) {
        this.order = order;
        this.delivery=delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = order.getOrderDate().format(customFormat);
        newPdfParagraph(document,formattedDate,Paragraph.ALIGN_RIGHT,32);

        usersDetails(document);

        newPdfParagraph(document,"Rodzaj przesyłki: " + delivery.getMode());
        newPdfParagraph(document,"\n");

        PdfPTable ordersTable = new PdfPTable(4);
        ordersTable.setWidthPercentage(100);

        writeTableHeader(ordersTable);
        writeTableData(ordersTable);

        document.add(ordersTable);

        document.close();
    }
    private void newPdfParagraph(Document document, String text){
        newPdfParagraph(document, text,Paragraph.ALIGN_LEFT,12);
    }
    private void newPdfParagraph(Document document, String text, int alignment,float size){
        Font font = FontFactory.getFont(FontFactory.COURIER,size);

        System.out.println("font: "+font.getSize());
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font);
        paragraph.setAlignment(alignment);
        document.add(paragraph);
    }

    private void usersDetails(Document document){
        newPdfParagraph(document,"Faktura za zakupy w Isłodycze",Paragraph.ALIGN_CENTER,32);
        newPdfParagraph(document,"Numer zamowienia: " + order.getOrderId());
        newPdfParagraph(document,"");
        newPdfParagraph(document,"Dane odbiorcy: ",Paragraph.ALIGN_LEFT,32);
        newPdfParagraph(document,"Imie: " + delivery.getName());
        newPdfParagraph(document,"Nazwisko: "  + delivery.getSurname());
        newPdfParagraph(document,"Email odbiorcy: " + order.getUserIdFk().getEmail());
        newPdfParagraph(document,"Address: " + delivery.getAddress());
        newPdfParagraph(document,"Kod pocztowy: " + delivery.getPostalCode());
        newPdfParagraph(document,"Miasto: " + delivery.getCity());
        newPdfParagraph(document,":  " + delivery.getCountry());
        newPdfParagraph(document,"Telefon: " + delivery.getPhoneArea()+" "+delivery.getPhoneNumber() );
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(4);
        cell.setPhrase(new Phrase(""));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nazwa produktu"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Cena produktu"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Ilosc"));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        int i=0;
        for (ProductOrder productOrder :order.getProductOrder()){
            table.addCell(String.valueOf(i++));
            table.addCell(String.valueOf(productOrder.getProductFk().getProductName()));
            table.addCell(String.valueOf(productOrder.getProductFk().getPrice()));
            table.addCell(String.valueOf(productOrder.getAmount()));
        }
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        table.addCell(cell);
        table.addCell(cell);
        table.addCell("Razem:");
        String zl=" zl";
        table.addCell(String.valueOf(order.getTotalCost())+zl);
    }

}
