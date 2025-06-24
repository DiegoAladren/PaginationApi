package com.example.springboot.paginationapi.service;

import com.example.springboot.paginationapi.model.Product;
import com.example.springboot.paginationapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    @Test
    public void testGetProductsReturnsPage() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        Page<Product> mockPage = new PageImpl<>(Collections.singletonList(
                Product.builder().id(1L).name("Test").category("Books").price(10.0).build()
        ));
        when(productRepository.findAll(pageable)).thenReturn(mockPage);

        Page<Product> result = productService.getProducts(null, null, null, null, 0, 5, "id", "asc");

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    public void testSaveProduct() {
        Product p = Product.builder().name("Libro Spring Boot").category("Books").price(2.0).build();
        when(productRepository.save(p)).thenReturn(p);

        Product saved = productService.save(p);

        assertEquals("Libro Spring Boot", saved.getName());
        verify(productRepository, times(1)).save(p);
    }

    @Test
    public void testGetByIdFound() {
        Product p = Product.builder().id(1L).name("Monitor Samsung").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Product> found = productService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("Monitor Samsung", found.get().getName());
    }

    @Test
    public void testDeleteProduct() {
        productService.delete(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
