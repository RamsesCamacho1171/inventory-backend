package com.company.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.company.inventory.model.Product;

public interface IproductDao extends CrudRepository<Product, Long>{

	//pones la consulta y ya jala la tabla se debe llamar como el nombre de la clase, si no no funciona,porque mi tabla es product con minuscula
		@Query("select p from Product p where p.name like %?1%")
		List<Product> findByName(String name);
		//leer docmentacion de spring, son metodos ya hechos, dependiendo el nombre del metodo
		List<Product> findByNameContainingIgnoreCase(String name);
}
