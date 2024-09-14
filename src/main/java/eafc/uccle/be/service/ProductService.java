package eafc.uccle.be.service;

import eafc.uccle.be.dao.CategoryRepository;
import eafc.uccle.be.dao.ImageRepository;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.dto.ProductDto;
import eafc.uccle.be.entity.Image;
import eafc.uccle.be.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.categoryRepository = categoryRepository;
    }


    public Map<String, Object> getAllProductsPaginated(Pageable pageable) {
        Page<Product>  products = productRepository.findAll(pageable);
        return Map.of(
                "number", products.getNumber(),
                "size", products.getSize(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages(),
                "content", products.getContent()
        );
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProductByCategoryId(Long id, String sortBy, boolean ascending, Pageable pageable){
        Page<Product> products;
        if ("noFilter".equalsIgnoreCase(sortBy)) {
            products = productRepository.findByCategoryId(id, pageable);
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            products = productRepository.findByCategoryIdOrderByAverageRating(id, pageable);
        } else {
            Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            products = productRepository.findByCategoryId(id, pageable);
        }
        products.getContent().forEach(product -> product.setImages(imageRepository.findByProductId(product.getId())));
        return products;
    }

    public Page<Product> findByNameContaining(String name, String sortBy, boolean ascending, Pageable pageable) {
        Page<Product> products;
        if ("noFilter".equalsIgnoreCase(sortBy)) {
            products = productRepository.findByNameContaining(name, pageable);
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            products = productRepository.findByNameContainingOrderByAverageRating(name, pageable);
        } else {
            Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
            products = productRepository.findByNameContaining(name, pageable);
        }
        products.getContent().forEach(product -> product.setImages(imageRepository.findByProductId(product.getId())));
        return products;
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setProductDescription(updatedProduct.getProductDescription());
            existingProduct.setUnitPrice(updatedProduct.getUnitPrice());
            existingProduct.setUnitsInStock(updatedProduct.getUnitsInStock());
            existingProduct.setLastUpdated(new Date());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new ResourceNotFoundException("Product not found with id : " + id));
    }

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setImages(imageRepository.findByProductId(product.getId()));
        }
        return product;
    }

    @Transactional
    public Product createProduct(ProductDto productDto) {
        Product newProduct = new Product();
        newProduct.setName(productDto.getName());
        newProduct.setProductDescription(productDto.getProductDescription());
        newProduct.setUnitPrice(productDto.getUnitPrice());
        newProduct.setUnitsInStock(productDto.getUnitsInStock());
        newProduct.setCategory(categoryRepository.findById((long) productDto.getCategoryId()).orElse(null));
        newProduct.setDateCreated(new Date());
        newProduct.setLastUpdated(new Date());

        Product savedProduct = productRepository.save(newProduct);

        productDto.getImages().forEach(image -> {
            Image imageToSave = new Image();
            imageToSave.setImageUrl(image.getImageUrl());
            imageToSave.setRankShow(image.getRankShow());
            imageToSave.setProduct(savedProduct);
            Image newImage = imageRepository.save(imageToSave);

        });
        return productRepository.save(savedProduct);
    }

    @Transactional
    public void deleteProductById(Long id) {
        imageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }






}
