package fr.jmichel.devis.tools.factory.product;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;

@Service
public abstract class AbstractProductFactory {
	public abstract AbstractProduct createProduct(String name, BigDecimal price, String typeOfSale, boolean promotion, BigDecimal promotionRate, int nbProduct, BigDecimal weight);
	
}
