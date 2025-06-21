package com.example.springboot.paginationapi.service;

import com.example.springboot.paginationapi.model.Product;
import com.example.springboot.paginationapi.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getProducts(String name, String category, Double minPrice, Double maxPrice, int page, int size, String sortBy, String direction){
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sortBy);

        Specification<Product> spec = null;

        if (name != null) {
            spec = (root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        }

        if (category != null) {
            Specification<Product> categorySpec = (root, query, cb) ->
                    cb.equal(root.get("category"), category);
            spec = spec == null ? categorySpec : spec.and(categorySpec);
        }

        if (minPrice != null) {
            Specification<Product> minPriceSpec = (root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            spec = spec == null ? minPriceSpec : spec.and(minPriceSpec);
        }

        if (maxPrice != null) {
            Specification<Product> maxPriceSpec = (root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            spec = spec == null ? maxPriceSpec : spec.and(maxPriceSpec);
        }

        return spec != null
                ? productRepository.findAll(spec, pageable)
                : productRepository.findAll(pageable);
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Optional<Product> getById(Long id){
        return productRepository.findById(id);
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }
}
