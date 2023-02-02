package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.dao.IproductDao;
import com.company.inventory.model.Category;
import com.company.inventory.model.Product;
import com.company.inventory.response.ProductResponseRest;

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
	public ResponseEntity<ProductResponseRest> save(Product product, Long CategoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		
		try {
			//buscar category para meterla en el producto
			Optional<Category> category = categoryDao.findById(CategoryId);
			
			if(category.isPresent()) {
				product.setCategory(category.get());
				System.out.println("---------------------llegue aqui----------------");
			}else {
				response.setMetadata("respuesta nok", "-1", "Categoria no existe");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			//guardar producto
			Product productSaved = productDao.save(product);
			System.out.println("---------------------llegue aqui2----------------");
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
 
}
