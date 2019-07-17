package fr.jmichel.devis.cloudfoundry.mvc.services.beans;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import fr.jmichel.devis.tools.DevisConstant;

public class ProductTest {

	@Test
	public void testCalculatePriceForProductSimpleWithOutPromotion() {
    	AbstractProduct product = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal("1"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal(3); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(3));
    	
    	assertTrue(priceExpected.equals(price));
	}
	
	@Test
	public void testCalculatePriceForProductSimpleWithPromotion() {
    	AbstractProduct product = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(true)
    			.promotionRate(new BigDecimal("20"))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal("2.4"); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(3));
    	
    	assertTrue(priceExpected.equals(price));
	}
	
	@Test
	public void testCalculatePriceForProductBatchWithOutPromotion() {
    	AbstractProduct product = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal(3); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(3));
    	
    	assertTrue(priceExpected.equals(price));
	}
	
	@Test
	public void testCalculatePriceForProductBatchWithPromotion() {
    	AbstractProduct product = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(true)
    			.promotionRate(new BigDecimal(20))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal("2.4"); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(3));
    	
    	assertTrue(priceExpected.equals(price));
	}
	
	@Test
	public void testCalculatePriceForWeightedProductWithOutPromotion() {
		AbstractProduct product = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal(1))
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal(0.5); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(0.5));
    	
    	assertTrue(priceExpected.equals(price));
	}
	
	@Test
	public void testCalculatePriceForWeightedProductWithPromotion() {
		AbstractProduct product = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(true)
    			.promotionRate(new BigDecimal(50))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal(1))
    			.updateDate(new Date())
    			.build();
    	BigDecimal priceExpected = new BigDecimal(0.25); 
    	BigDecimal price = product.calculatePrice(new BigDecimal(0.5));
    	
    	assertTrue(priceExpected.equals(price));
	}

}
