package com.candyshop.islodycze.pdf;

import com.candyshop.islodycze.delivery.DeliveryEntity;
import com.candyshop.islodycze.delivery.DeliveryRepository;
import com.candyshop.islodycze.model.Order;
import com.candyshop.islodycze.order.OrderRepository;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class GetPdfController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @GetMapping("/pdfFile")
    public String getPdf() {
        return "getPdfFile";
    }

    @GetMapping("/confirmationPdfExport/{orderId}")
    public void confirmationExportToPDF(HttpServletResponse response, @PathVariable("orderId") final Long orderId) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Potwierdzenie_zamówienia_nr_" + orderId + ".pdf";
        response.setHeader(headerKey, headerValue);

        Order order = orderRepository.getReferenceById(orderId);
        DeliveryEntity delivery = deliveryRepository.findByOrderOrderId(orderId);
        PdfConfirmationExporter exporter = new PdfConfirmationExporter(order, delivery);
        exporter.export(response);
    }

    @GetMapping("/transportLabelPdfExport/{orderId}")
    public void transportExportToPDF(HttpServletResponse response, @PathVariable("orderId") final Long orderId) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Etykieta_transportowa_zamówienia_nr_" + orderId + ".pdf";
        response.setHeader(headerKey, headerValue);

        Order order = orderRepository.getReferenceById(orderId);
        DeliveryEntity delivery = deliveryRepository.findByOrderOrderId(orderId);
        PdfTransportLabelExporter exporter = new PdfTransportLabelExporter(order, delivery);
        exporter.export(response);
    }
}
