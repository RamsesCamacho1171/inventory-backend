package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IproductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;
import com.company.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {
	
	private ICategoryDao categoryDao;
	private IproductDao productDao;
	

	public ProductServiceImpl(ICategoryDao categoryDao, IproductDao productDao) {
		super();
		this.categoryDao = categoryDao;
		this.productDao=productDao;
	}



	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long CategoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//buscar category para meterla en el producto
			Optional<Category> category = categoryDao.findById(CategoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
				
			}else {
				response.setMetadata("respuesta nok", "-1", "Categoria no existe");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			//guardar producto
			Product productSaved = productDao.save(product);
			
			if(productSaved!=null) {
				list.add(productSaved);
				response.getProducts().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Producto guardado");
				

			}else {
				response.setMetadata("respuesta nok", "-1", "Producto no guardado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al guardar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long Id) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//buscar producto por id
			Optional<Product> product = productDao.findById(Id);
			
			if(product.isPresent()) {
				
				byte[] pictureDescompressed = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(pictureDescompressed);
				list.add(product.get());
				response.getProducts().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Producto encontrado");
				
			}else {
				response.setMetadata("respuesta nok", "-1", "Producto no existe");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al buscar producto");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}



	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		
		try {
			//buscar producto por name
			listAux=productDao.findByName(name);
			
			if(listAux.size() > 0 ) {
				//mañana la seguimos 112: min 4
				
				listAux.stream().forEach((p)->{
					byte[] pictureDescompressed = Util.decompressZLib(p.getPicture());
					p.setPicture(pictureDescompressed);
					list.add(p);
				});
				
				response.getProducts().setProducts(list);
				response.setMetadata("respuesta ok", "00", "Producto encontrado");
				
			}else {
				response.setMetadata("respuesta nok", "-1", "Producto no encontrado");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta nok", "-1", "Error al buscar producto por nombre");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
	}
 
}
