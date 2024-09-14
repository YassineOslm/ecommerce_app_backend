package eafc.uccle.be.controller;

import eafc.uccle.be.dto.ProductDto;
import eafc.uccle.be.dto.UserDto;
import eafc.uccle.be.entity.Category;
import eafc.uccle.be.entity.Product;
import eafc.uccle.be.entity.User;
import eafc.uccle.be.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/categories")
@Slf4j
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

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Category category) {
        Category newCategory = categoryService.addCategory(category);
        Map<String, Object> response = Map.of(
                "newCategoryId", newCategory.getId()
        );
        log.info("new category created with id : {}", newCategory.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
