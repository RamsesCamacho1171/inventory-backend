package com.company.inventory.services;

import org.springframework.http.ResponseEntity;

import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

public interface IProductService {
	
	public ResponseEntity<ProductResponseRest> save(Product product, Long CategoryId);
	public ResponseEntity<ProductResponseRest> searchById(Long Id);
	public ResponseEntity<ProductResponseRest> searchByName(String name);
	public ResponseEntity<ProductResponseRest> deleteById(Long Id);
	public ResponseEntity<ProductResponseRest> search();
	
}
