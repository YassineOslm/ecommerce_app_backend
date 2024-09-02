package eafc.uccle.be.service;

import eafc.uccle.be.dao.ImageRepository;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public Page<Product> getProductByCategoryId(Long id, String sortBy, boolean ascending, Pageable pageable){
        if (!"none".equalsIgnoreCase(sortBy)) {
            Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        Page<Product> products = productRepository.findByCategoryId(id, pageable);
        products.getContent().forEach(product -> product.setImages(imageRepository.findByProductId(product.getId())));
        return products;
    }

    public Page<Product> findByNameContaining(String name, String sortBy, boolean ascending, Pageable pageable) {
        if (!"none".equalsIgnoreCase(sortBy)) {
            Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Product> products = productRepository.findByNameContaining(name, pageable);
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
