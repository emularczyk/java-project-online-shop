package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;
import com.lowagie.text.DocumentException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfTransportLabelExporter {

    private Order order;
    private DeliveryEntity delivery;

    public PdfTransportLabelExporter(Order order, DeliveryEntity delivery) {
        this.order = order;
        this.delivery = delivery;
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document document = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont("C:/Windows/Fonts/arial.ttf");
        document.setFont(font);

        fromDetails(document);
        toDetails(document);

        addBarCodeImage(document);

        document.close();
    }

    private void fromDetails(Document document) {
        PdfService.newPdfParagraph(document, "Dane nadawcy: ", TextAlignment.LEFT, 14);
        PdfService.newPdfParagraph(document, "\n");
        PdfService.newPdfParagraph(document, "iSłodycze");
        PdfService.newPdfParagraph(document, "ul. Kupiecka");
        PdfService.newPdfParagraph(document, "12-345");
        PdfService.newPdfParagraph(document, "Zielona Góra:");
        PdfService.newPdfParagraph(document, "Polska");
        PdfService.newPdfParagraph(document, "\n");
    }

    private void toDetails(Document document) {
        PdfService.newPdfParagraph(document, "Dane odbiorcy: ", TextAlignment.LEFT, 14);
        PdfService.newPdfParagraph(document, "\n");
        PdfService.newPdfParagraph(document, delivery.getName() + " " + delivery.getSurname());
        PdfService.newPdfParagraph(document, delivery.getAddress());
        PdfService.newPdfParagraph(document, delivery.getPostalCode());
        PdfService.newPdfParagraph(document, delivery.getCity());
        PdfService.newPdfParagraph(document, delivery.getCountry());
    }

    private void addBarCodeImage(Document document) {
        PdfService.newPdfParagraph(document, "");
        PdfService.newPdfParagraph(document, "Kod kreskowy do przesyłki:", TextAlignment.LEFT, 14);
        byte[] imageInBytes = BarCodeImage.getBarCodeImage(delivery.getId().toString(), 200, 50);
        ImageData imageData = ImageDataFactory.create(imageInBytes);
        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
        document.add(image);
    }
}
