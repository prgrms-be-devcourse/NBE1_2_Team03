package com.sscanner.team.products.repository;

import com.sscanner.team.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

}
