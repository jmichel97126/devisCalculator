package fr.jmichel.devis.tools.factory.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Product;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.ProductBatch;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.WeighedProduct;
import fr.jmichel.devis.tools.DevisConstant;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration
public class ProductFactoryTest {

    @Autowired
    private ProductFactory productFactory;   

    @Configuration
    @Import({ServicesConfiguration.class})
    public static class ProductServiceTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }


    @Test
    public void testMakeProductSimple() {
    	AbstractProduct productToFactory = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct manufacturedProduct = productFactory.createProduct(productToFactory.getName(), productToFactory.getPrice(), productToFactory.getTypeOfSale(), productToFactory.isPromotion(), productToFactory.getPromotionRate(), 0, new BigDecimal(0));
    	
        assertNotNull("the product can't be null", productToFactory);
        
        assertTrue(manufacturedProduct instanceof Product);

        assertEquals(manufacturedProduct.getName(), productToFactory.getName());
        assertEquals(manufacturedProduct.getPrice(), productToFactory.getPrice());
        assertEquals(manufacturedProduct.isPromotion(), productToFactory.isPromotion());
        assertEquals(manufacturedProduct.getTypeOfSale(), productToFactory.getTypeOfSale());
        assertEquals(manufacturedProduct.getUpdateDate(), productToFactory.getUpdateDate());
        assertEquals(manufacturedProduct.getPromotionRate(), null);

    }
    
    @Test
    public void testMakeProductBatch() {
    	ProductBatch productToFactory = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct manufacturedProduct = productFactory.createProduct(productToFactory.getName(), productToFactory.getPrice(), productToFactory.getTypeOfSale(), productToFactory.isPromotion(), productToFactory.getPromotionRate(), productToFactory.getNbProduct(), new BigDecimal(0));
    	
        assertNotNull("the product can't be null", productToFactory);
        
        assertTrue(manufacturedProduct instanceof ProductBatch);

        assertNotNull("the product can't be null", manufacturedProduct);
        assertEquals(manufacturedProduct.getName() , productToFactory.getName());
        assertEquals(manufacturedProduct.getPrice() , productToFactory.getPrice());
        assertEquals(manufacturedProduct.isPromotion() , false);
        assertEquals(manufacturedProduct.getTypeOfSale(), productToFactory.getTypeOfSale());
        assertEquals(manufacturedProduct.getUpdateDate(), productToFactory.getUpdateDate());
        assertEquals(((ProductBatch) manufacturedProduct).getNbProduct(), productToFactory.getNbProduct());
        assertEquals(manufacturedProduct.getPromotionRate(), null);
    }
    
    @Test
    public void testMakeWeighedProduct() {
    	WeighedProduct productToFactory = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal("55"))
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct manufacturedProduct = productFactory.createProduct(productToFactory.getName(), productToFactory.getPrice(), productToFactory.getTypeOfSale(), productToFactory.isPromotion(), productToFactory.getPromotionRate(), 0, productToFactory.getWeight());
    	
        assertNotNull("the product can't be null", productToFactory);
        
        assertTrue(manufacturedProduct instanceof WeighedProduct);

        assertNotNull("the product can't be null", manufacturedProduct);
        assertEquals(manufacturedProduct.getName() , productToFactory.getName());
        assertEquals(manufacturedProduct.getPrice() , productToFactory.getPrice());
        assertEquals(manufacturedProduct.isPromotion() , false);
        assertEquals(manufacturedProduct.getTypeOfSale(), productToFactory.getTypeOfSale());
        assertEquals(manufacturedProduct.getUpdateDate(), productToFactory.getUpdateDate());
        assertTrue(((WeighedProduct) manufacturedProduct).getWeight().equals(productToFactory.getWeight()));
        assertEquals(manufacturedProduct.getPromotionRate(), null);
    }    
 
}
