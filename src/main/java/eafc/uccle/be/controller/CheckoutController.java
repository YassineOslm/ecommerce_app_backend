package eafc.uccle.be.controller;

import eafc.uccle.be.dto.Purchase;
import eafc.uccle.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    // TODO: REPLACE with new checkout service
    private final ProductService productService;

    @Autowired
    public CheckoutController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Purchase purchase) {
        Map<String, Object> response = Map.of(
                "msg", "purchase received"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
