package eafc.uccle.be.service;

import eafc.uccle.be.dao.ImageRepository;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }


    public Map<String, Object> getAllProducts(Pageable pageable) {
        Page<Product>  products = productRepository.findAll(pageable);
        return Map.of(
                "number", products.getNumber(),
                "size", products.getSize(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages(),
                "content", products.getContent()
        );
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

    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setImages(imageRepository.findByProductId(product.getId()));
        }
        return product;
    }

}
