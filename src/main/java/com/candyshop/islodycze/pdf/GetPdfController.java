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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Controller
public class GetPdfController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    //User generates pdf order confirmation.
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

    //User generates pdf transport label.
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

    //Generating bar code image for transport label.
    @RequestMapping(value = "/barCode/{id}", method = RequestMethod.GET)
    public void generateBarcode(@PathVariable("id") String id, HttpServletResponse response) throws Exception {

        response.setContentType("image/png");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(BarCodeImage.getBarCodeImage(id, 200, 50));
        outputStream.flush();
        outputStream.close();
    }
}
