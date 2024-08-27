package eafc.uccle.be.controller;

import eafc.uccle.be.entity.Category;
import eafc.uccle.be.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getCategories() {
        List<Category>  categories = categoryService.getCategories();
        Map<String, Object> response = Map.of(
                "content", categories
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
