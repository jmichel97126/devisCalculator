package fr.jmichel.devis.cloudfoundry.mvc.services.beans;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springsource.cloudfoundry.mvc.services.ProductService;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

import fr.jmichel.devis.cloudfoundry.mvc.web.beans.Order;
import fr.jmichel.devis.tools.DevisConstant;
import fr.jmichel.devis.tools.proxy.order.OrderProxy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OrderTest {
	
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderProxy orderProxy;
    
    private Order order;

    @Configuration
    @Import({ServicesConfiguration.class})
    public static class OrderTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }
    
    @Before
    public void before() throws Exception {
    	order = Order.builder()
    		.products(new LinkedHashMap<AbstractProduct,BigDecimal>())
    		.build();
    	
    	orderProxy.setOrder(order);
    }
    
	@Test
	public void testAddProductInWithCalculateBatches() {
		
		AbstractProduct productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("1"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    	productService.createProduct(productToBeInserted);
    	
    	productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("2"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(3)
    			.updateDate(new Date())
    			.build();
    	productService.createProduct(productToBeInserted);
    	
    	productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal(1))
    			.promotion(true)
    			.promotionRate(new BigDecimal(25))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
     	productService.createProduct(productToBeInserted);
     	
     	orderProxy.addProductInOrder(productToBeInserted, new BigDecimal(7));
     	
     	assertTrue(order.getProducts().size() == 2);
     	
     	orderProxy.addProductInOrder(productToBeInserted, new BigDecimal(2));
     	assertTrue(order.getProducts().size() == 3);
	}
	
	@Test
	public void testCalculateTotalPrice() {
		
		AbstractProduct productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("1"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    	productService.createProduct(productToBeInserted);
    	
    	productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("2"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(3)
    			.updateDate(new Date())
    			.build();
    	productService.createProduct(productToBeInserted);
    	
    	productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal(3))
    			.promotion(false)
    			//.promotionRate(new BigDecimal(25))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
     	productService.createProduct(productToBeInserted);
     	orderProxy.addProductInOrder(productToBeInserted, new BigDecimal(7));	
     	orderProxy.addProductInOrder(productToBeInserted, new BigDecimal(2));
     	
     	assertEquals(orderProxy.calculateTotal().compareTo(new BigDecimal(9)), 0);
	}

}
