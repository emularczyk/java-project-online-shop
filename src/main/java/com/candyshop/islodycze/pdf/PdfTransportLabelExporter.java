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

public class PdfTransportLabelExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfTransportLabelExporter(Order order, DeliveryEntity delivery) {
        this.order = order;
        this.delivery=delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        
        fromDetails(document);
        toDetails(document);

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

    private void fromDetails(Document document){
        newPdfParagraph(document,"Dane nadawcy: ",Paragraph.ALIGN_LEFT,32);
        newPdfParagraph(document,"Islodycze");
        newPdfParagraph(document,"ul. Kupiecka");
        newPdfParagraph(document,"12-345");
        newPdfParagraph(document,"Zielona Gora:");
        newPdfParagraph(document,"Polska");
    }

    private void toDetails(Document document){
        newPdfParagraph(document,"Dane odbiorcy: ",Paragraph.ALIGN_LEFT,32);
        newPdfParagraph(document,delivery.getName()+" "+delivery.getSurname());
        newPdfParagraph(document,delivery.getAddress());
        newPdfParagraph(document,delivery.getPostalCode());
        newPdfParagraph(document,delivery.getCity());
        newPdfParagraph(document,delivery.getCountry());
    }
}
