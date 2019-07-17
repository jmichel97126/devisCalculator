package fr.jmichel.devis.tools.factory.product;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Product;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.ProductBatch;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.WeighedProduct;
import fr.jmichel.devis.tools.DevisConstant;


@Service
public class ProductFactory extends AbstractProductFactory {
	@Override
	public AbstractProduct createProduct(String name, BigDecimal price, String typeOfSale, boolean promotion,
			BigDecimal promotionRate, int nbProduct, BigDecimal weight) {
		AbstractProduct product = null;

		switch (typeOfSale) {
		case DevisConstant.TYPE_OF_SALE_SIMPLE:
			product = Product.builder()
			.name(name)
			.price(price)
			.typeOfSale(typeOfSale)
			.promotion(promotion)
			.promotionRate(promotionRate)
			.updateDate(new Date())
			
			.build();
			break;

		case DevisConstant.TYPE_OF_SALE_BATCHES:
			product = ProductBatch.builder()
			.name(name)
			.price(price)
			.typeOfSale(typeOfSale)
			.promotion(promotion)
			.promotionRate(promotionRate)
			.updateDate(new Date())
			.nbProduct(nbProduct)
			
			.build();
			break;

		case DevisConstant.TYPE_OF_SALE_WEIGTH:
			product = WeighedProduct.builder()
			.name(name)
			.price(price)
			.typeOfSale(typeOfSale)
			.promotion(promotion)
			.promotionRate(promotionRate)
			.updateDate(new Date())
			.weight(weight)
			
			.build();
			break;
		default:
			break;
		}

		return product;
	}

}
