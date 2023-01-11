package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.model.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

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
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        BaseFont arial = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(arial, 12);

        fromDetails(document, font);
        toDetails(document, font);

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

    private void fromDetails(Document document, Font font) {
        newPdfParagraph(document, font, "Dane nadawcy: ", Paragraph.ALIGN_LEFT, 14);
        newPdfParagraph(document, font, "\n");
        newPdfParagraph(document, font, "iSłodycze");
        newPdfParagraph(document, font, "ul. Kupiecka");
        newPdfParagraph(document, font, "12-345");
        newPdfParagraph(document, font, "Zielona Góra:");
        newPdfParagraph(document, font, "Polska");
        newPdfParagraph(document, font, "\n");
    }

    private void toDetails(Document document, Font font) {
        newPdfParagraph(document, font, "Dane odbiorcy: ", Paragraph.ALIGN_LEFT, 14);
        newPdfParagraph(document, font, "\n");
        newPdfParagraph(document, font, delivery.getName() + " " + delivery.getSurname());
        newPdfParagraph(document, font, delivery.getAddress());
        newPdfParagraph(document, font, delivery.getPostalCode());
        newPdfParagraph(document, font, delivery.getCity());
        newPdfParagraph(document, font, delivery.getCountry());
    }
}
