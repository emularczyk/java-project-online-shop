package com.candyshop.islodycze.pdf;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

    public static void newPdfParagraph(Document document, String text) {
        newPdfParagraph(document, text, TextAlignment.LEFT, 12);
    }

    public static  void newPdfParagraph(Document document, String text, TextAlignment alignment, int size) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setTextAlignment(alignment).setFontSize(size);
        document.add(paragraph);
    }
}
