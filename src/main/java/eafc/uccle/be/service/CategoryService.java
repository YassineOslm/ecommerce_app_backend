package eafc.uccle.be.service;

import eafc.uccle.be.dao.CategoryRepository;
import eafc.uccle.be.dao.ImageRepository;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.entity.Category;
import eafc.uccle.be.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;


    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategory(id);
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        productRepository.findProductsByCategoryId(id).forEach((product) -> {
            imageRepository.deleteByProductId(product.getId());
        });
        productRepository.deleteProductsByCategoryId(id);
        categoryRepository.deleteCategoryById(id);
    }

}
