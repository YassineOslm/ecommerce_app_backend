package eafc.uccle.be.controller;

import eafc.uccle.be.entity.Product;
import eafc.uccle.be.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/listPaginated")
    public ResponseEntity<Map<String, Object>> getProducts(Pageable pageable) {
        Map<String, Object> response = productService.getAllProducts(pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchByCatId")
    public ResponseEntity<Map<String, Object>> getProductByCategoryId(
            @RequestParam Long id,
            @RequestParam(defaultValue = "none") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            Pageable pageable
    ) {
        Page<Product> productsByCategoryId = productService.getProductByCategoryId(id, sortBy, ascending, pageable);
        Map<String, Object> response = Map.of(
                "content", productsByCategoryId.getContent(),
                "number" , productsByCategoryId.getNumber(),
                "size" , productsByCategoryId.getSize(),
                "totalElements" , productsByCategoryId.getTotalElements(),
                "totalPages" , productsByCategoryId.getTotalPages()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/searchByName")
    public ResponseEntity<Map<String, Object>> findProductsByName(
            @RequestParam("name") String name,
            @RequestParam(defaultValue = "none") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            Pageable pageable) {
        Page<Product> productsbyNameContaining = productService.findByNameContaining(name, sortBy, ascending, pageable);
        Map<String, Object> response = Map.of(
                "content", productsbyNameContaining.getContent(),
                "number" , productsbyNameContaining.getNumber(),
                "size" , productsbyNameContaining.getSize(),
                "totalElements" , productsbyNameContaining.getTotalElements(),
                "totalPages" , productsbyNameContaining.getTotalPages()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
