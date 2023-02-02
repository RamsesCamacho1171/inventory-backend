package com.company.inventory.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseRest extends ResponseRest{

	private ProductResponse products = new ProductResponse();
}
