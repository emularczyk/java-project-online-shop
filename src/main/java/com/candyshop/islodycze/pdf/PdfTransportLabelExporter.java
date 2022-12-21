package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.lowagie.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfTransportLabelExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfTransportLabelExporter(Order order, DeliveryEntity delivery) {
        this.order = order;
        this.delivery=delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        PdfDocument pdfDoc = new PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(response.getOutputStream()));
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);
        
        fromDetails(document);
        toDetails(document);

        addBarCodeImage(document);

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

    private void fromDetails(Document document){
        newPdfParagraph(document,"Dane nadawcy: ", TextAlignment.LEFT,32);
        newPdfParagraph(document,"Islodycze");
        newPdfParagraph(document,"ul. Kupiecka");
        newPdfParagraph(document,"12-345");
        newPdfParagraph(document,"Zielona Gora:");
        newPdfParagraph(document,"Polska");
    }

    private void toDetails(Document document){
        newPdfParagraph(document,"Dane odbiorcy: ",TextAlignment.LEFT,32);
        newPdfParagraph(document,delivery.getName()+" "+delivery.getSurname());
        newPdfParagraph(document,delivery.getAddress());
        newPdfParagraph(document,delivery.getPostalCode());
        newPdfParagraph(document,delivery.getCity());
        newPdfParagraph(document,delivery.getCountry());
    }

    private void addBarCodeImage(Document document) {
        newPdfParagraph(document,"");
        newPdfParagraph(document,"Kod kreskowy do przesyłki:");
        byte[] imageInBytes = BarCodeImage.getBarCodeImage(delivery.getId().toString(), 200, 50);
        ImageData imageData = ImageDataFactory.create(imageInBytes);
        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
        document.add(image);
    }
}
