package eafc.uccle.be.controller;

import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.service.CheckoutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<byte[]> placeOrder(@RequestBody Purchase purchase) {
        Map<String, Object> result = checkoutService.placeOrder(purchase);

        byte[] pdfBytes = (byte[]) result.get("pdfBytes");
        String orderId = (String) result.get("orderId");

        log.info("new order with id : {}", orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "receipt_" + orderId + ".pdf");
        headers.add("Order-ID", orderId);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
