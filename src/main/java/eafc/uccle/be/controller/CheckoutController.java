package eafc.uccle.be.controller;

import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Purchase purchase) {
        String newShoppingOrderId = checkoutService.placeOrder(purchase);
        Map<String, Object> response = Map.of(
                "msg", "purchase received",
                "purchase_uuid", newShoppingOrderId
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
