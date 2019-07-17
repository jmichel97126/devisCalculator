package org.springsource.cloudfoundry.mvc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Product;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.ProductBatch;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.WeighedProduct;
import fr.jmichel.devis.tools.DevisConstant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProductServiceTest {

    @Autowired
    private ProductService productService;    
    
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    JdbcTemplate jdbcTemplate;
    

    @Configuration
    @Import({ServicesConfiguration.class})
    public static class ProductServiceTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }


    @Before
    public void before() throws Exception {

        jdbcTemplate = new JdbcTemplate(dataSource);

        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("delete from Product");
            }
        });
    }

    @Test
    public void testCreatingProductSimpleWithoutPromotion() {
    	AbstractProduct productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct productInserted = productService.createProduct(productToBeInserted);
    	
        assertNotNull("the product can't be null", productInserted);
        assertEquals(productInserted.getName() , productToBeInserted.getName());
        assertEquals(productInserted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productInserted.isPromotion() , false);
        assertEquals(productInserted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productInserted.getUpdateDate(), productToBeInserted.getUpdateDate());
        assertEquals(productInserted.getPromotionRate(), null);

    }
    
    @Test
    public void testGetProductById() {
    	AbstractProduct productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    					
    	int idProduct = productService.createProduct(productToBeInserted).getId();
    	AbstractProduct productGetted = productService.getProductById(idProduct);
    	
        assertNotNull("the product can't be null", productGetted);
        assertEquals(productGetted.getId() , idProduct);
        assertEquals(productGetted.getName() , productToBeInserted.getName());
        assertEquals(productGetted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productGetted.isPromotion() , false);
        assertEquals(productGetted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productGetted.getUpdateDate(), new Timestamp(productToBeInserted.getUpdateDate().getTime()));
        assertEquals(productGetted.getPromotionRate(), null);

    }
    
    @Test
    public void testCreatingProductSimpleWithPromotion() {
    	AbstractProduct productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(true)
    			.promotionRate(new BigDecimal(25))
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct productInserted = productService.createProduct(productToBeInserted);
    	
        assertNotNull("the product can't be null", productInserted);
        assertEquals(productInserted.getName() , productToBeInserted.getName());
        assertEquals(productInserted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productInserted.isPromotion() , productToBeInserted.isPromotion());
        assertEquals(productInserted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productInserted.getUpdateDate().equals(productToBeInserted.getUpdateDate()), true);
        assertEquals(productInserted.getPromotionRate(), productToBeInserted.getPromotionRate());
    }
    
    @Test
    public void testUpdatingProductSimple() {
    	AbstractProduct productToBeInserted = Product.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct productBeforeUpdating = productService.createProduct(productToBeInserted);
        
        AbstractProduct productToUpdate = productService.getProductById(productBeforeUpdating.getId());
        productToUpdate.setPromotion(true);
        productToUpdate.setPromotionRate(new BigDecimal("15"));
        productToUpdate.setUpdateDate(new Date());
        
        productService.updateProduct(productBeforeUpdating.getId(), productToUpdate);
        
        AbstractProduct productUpdated = productService.getProductById(productBeforeUpdating.getId());

        assertEquals(productBeforeUpdating.getName() , productUpdated.getName());
        assertEquals(productBeforeUpdating.getPrice() , productUpdated.getPrice());
        assertEquals(productBeforeUpdating.getTypeOfSale(), productUpdated.getTypeOfSale());
        
        assertFalse(productBeforeUpdating.isPromotion() == productUpdated.isPromotion());
        assertFalse(productUpdated.getPromotionRate().equals(productBeforeUpdating.getPromotionRate()));
        assertFalse(productBeforeUpdating.getUpdateDate().equals(new Timestamp(productUpdated.getUpdateDate().getTime())));

        assertEquals(new Timestamp(productToUpdate.getUpdateDate().getTime()), new Timestamp(productUpdated.getUpdateDate().getTime()));
        assertTrue(productToUpdate.getPromotionRate().compareTo(productUpdated.getPromotionRate()) == 0);
        assertEquals(productToUpdate.isPromotion() , productUpdated.isPromotion());
    }
    
    @Test
    public void testCreatingProductBatch() {
    	ProductBatch productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    					
    	ProductBatch productInserted = (ProductBatch) productService.createProduct(productToBeInserted);
    	
        assertNotNull("the product can't be null", productInserted);
        assertEquals(productInserted.getName() , productToBeInserted.getName());
        assertEquals(productInserted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productInserted.isPromotion() , false);
        assertEquals(productInserted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productInserted.getUpdateDate(), productToBeInserted.getUpdateDate());
        assertEquals(productInserted.getNbProduct(), productToBeInserted.getNbProduct());
        assertEquals(productInserted.getPromotionRate(), null);

    }
    
    @Test
    public void testGetProductBatchById() {
    	ProductBatch productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    					
    	int idProduct = productService.createProduct(productToBeInserted).getId();
    	
    	ProductBatch productGetted = (ProductBatch) productService.getProductById(idProduct);
    	
        assertNotNull("the product can't be null", productGetted);
        assertEquals(productGetted.getId() , idProduct);
        assertEquals(productGetted.getName() , productToBeInserted.getName());
        assertEquals(productGetted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productGetted.isPromotion() , false);
        assertEquals(productGetted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productGetted.getUpdateDate(), new Timestamp(productToBeInserted.getUpdateDate().getTime()));
        assertEquals(productGetted.getPromotionRate(), null);

    }
    
    @Test
    public void testUpdatingProductBatch() {
    	ProductBatch productToBeInserted = ProductBatch.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();
    					
    	ProductBatch productBeforeUpdating = (ProductBatch) productService.createProduct(productToBeInserted);
    	
        AbstractProduct productToUpdate = productService.getProductById(productBeforeUpdating.getId());
        productToUpdate.setPromotion(true);
        productToUpdate.setPromotionRate(new BigDecimal("15"));
        productToUpdate.setUpdateDate(new Date());
        
        productService.updateProduct(productBeforeUpdating.getId(), productToUpdate);
        
        AbstractProduct productUpdated = productService.getProductById(productBeforeUpdating.getId());

        assertEquals(productBeforeUpdating.getName() , productUpdated.getName());
        assertEquals(productBeforeUpdating.getPrice() , productUpdated.getPrice());
        assertEquals(productBeforeUpdating.getTypeOfSale(), productUpdated.getTypeOfSale());
        
        assertFalse(productBeforeUpdating.isPromotion() == productUpdated.isPromotion());
        assertFalse(productUpdated.getPromotionRate().equals(productBeforeUpdating.getPromotionRate()));
        assertFalse(productBeforeUpdating.getUpdateDate().equals(new Timestamp(productUpdated.getUpdateDate().getTime())));

        assertEquals(new Timestamp(productToUpdate.getUpdateDate().getTime()), new Timestamp(productUpdated.getUpdateDate().getTime()));
        assertTrue(productToUpdate.getPromotionRate().compareTo(productUpdated.getPromotionRate()) == 0);
        assertEquals(productToUpdate.isPromotion() , productUpdated.isPromotion());
    }

    @Test
    public void testCreatingWeighedProduct() {
    	WeighedProduct productToBeInserted = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal("55"))
    			.updateDate(new Date())
    			.build();
    					
    	WeighedProduct productInserted = (WeighedProduct) productService.createProduct(productToBeInserted);
    	
        assertNotNull("the product can't be null", productInserted);
        assertEquals(productInserted.getName() , productToBeInserted.getName());
        assertEquals(productInserted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productInserted.isPromotion() , false);
        assertEquals(productInserted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(productInserted.getUpdateDate(), productToBeInserted.getUpdateDate());
        assertTrue(productInserted.getWeight().equals(productToBeInserted.getWeight()));
        assertEquals(productInserted.getPromotionRate(), null);

    }

    
    @Test
    public void testGetWeighedProductById() {
    	WeighedProduct productToBeInserted = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal("55"))
    			.updateDate(new Date())
    			.build();
    					
    	int idProduct = productService.createProduct(productToBeInserted).getId();
    	
    	WeighedProduct productGetted = (WeighedProduct) productService.getProductById(idProduct);
    	
        assertNotNull("the product can't be null", productGetted);
        assertEquals(productGetted.getName() , productToBeInserted.getName());
        assertEquals(productGetted.getPrice() , productToBeInserted.getPrice());
        assertEquals(productGetted.isPromotion() , false);
        assertEquals(productGetted.getTypeOfSale(), productToBeInserted.getTypeOfSale());
        assertEquals(new Timestamp(productGetted.getUpdateDate().getTime()), new Timestamp(productToBeInserted.getUpdateDate().getTime()));
        assertTrue(productGetted.getWeight().compareTo(productToBeInserted.getWeight()) == 0);
        assertEquals(productGetted.getPromotionRate(), null);

    }
    
    @Test
    public void testUpdatingWeighedProduct() {
    	WeighedProduct productToBeInserted = WeighedProduct.builder()
    			.name("testProduct")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal("55"))
    			.updateDate(new Date())
    			.build();
    					
    	AbstractProduct productBeforeUpdating = (WeighedProduct) productService.createProduct(productToBeInserted);
    	
        AbstractProduct productToUpdate = productService.getProductById(productBeforeUpdating.getId());
        productToUpdate.setPromotion(true);
        productToUpdate.setPromotionRate(new BigDecimal("15"));
        productToUpdate.setUpdateDate(new Date());
        
        productService.updateProduct(productBeforeUpdating.getId(), productToUpdate);
        
        AbstractProduct productUpdated = productService.getProductById(productBeforeUpdating.getId());

        assertEquals(productBeforeUpdating.getName() , productUpdated.getName());
        assertEquals(productBeforeUpdating.getPrice() , productUpdated.getPrice());
        assertEquals(productBeforeUpdating.getTypeOfSale(), productUpdated.getTypeOfSale());
        
        assertFalse(productBeforeUpdating.isPromotion() == productUpdated.isPromotion());
        assertFalse(productUpdated.getPromotionRate().equals(productBeforeUpdating.getPromotionRate()));
        assertFalse(productBeforeUpdating.getUpdateDate().equals(new Timestamp(productUpdated.getUpdateDate().getTime())));

        assertEquals(new Timestamp(productToUpdate.getUpdateDate().getTime()), new Timestamp(productUpdated.getUpdateDate().getTime()));
        assertTrue(productToUpdate.getPromotionRate().compareTo(productUpdated.getPromotionRate()) == 0);
        assertEquals(productToUpdate.isPromotion() , productUpdated.isPromotion());
    }

    @Test
    public void testSearchingForDifferentsTypesProducts() throws Exception {
    	AbstractProduct productSimpleToBeInserted = Product.builder()
    			.name("testProductSimple")
    			.price(new BigDecimal("10.99"))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_SIMPLE)
    			.updateDate(new Date())
    			.build();				
    	productService.createProduct(productSimpleToBeInserted);
    	
    	AbstractProduct productBatchToBeInserted = ProductBatch.builder()
    			.name("testProductBatch")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();	
    	productService.createProduct(productBatchToBeInserted);

    	AbstractProduct productWeighedToBeInserted = WeighedProduct.builder()
    			.name("testProductWeighed")
    			.price(new BigDecimal(10.99))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_WEIGTH)
    			.weight(new BigDecimal("55"))
    			.updateDate(new Date())
    			.build();
    	productService.createProduct(productWeighedToBeInserted);    	
    	
    	Collection<AbstractProduct> products = productService.search("testProduct");
        assertEquals(3, productService.search("testProduct").size());
        
        assertTrue(products.toArray()[0] instanceof Product);
        assertTrue(((Product)products.toArray()[0]).getName().equals(productSimpleToBeInserted.getName()));
        
        assertTrue(products.toArray()[1] instanceof ProductBatch);
        assertTrue(((ProductBatch)products.toArray()[1]).getName().equals(productBatchToBeInserted.getName()));
   
        assertTrue(products.toArray()[2] instanceof WeighedProduct); 
        assertTrue(((WeighedProduct)products.toArray()[2]).getName().equals(productWeighedToBeInserted.getName()));
    }
    
    @Test
    public void testGetProductsBatchesByName() throws Exception {
    	
    	AbstractProduct productBatchToBeInserted = ProductBatch.builder()
    			.name("testProductBatch")
    			.price(new BigDecimal(1))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(4)
    			.updateDate(new Date())
    			.build();	
    	productService.createProduct(productBatchToBeInserted);
    	
    	productBatchToBeInserted = ProductBatch.builder()
    			.name("testProductBatch")
    			.price(new BigDecimal(2))
    			.promotion(false)
    			.typeOfSale(DevisConstant.TYPE_OF_SALE_BATCHES)
    			.nbProduct(3)
    			.updateDate(new Date())
    			.build();	
    	productService.createProduct(productBatchToBeInserted);

    	Collection<AbstractProduct> products = productService.search("testProduct");
        assertEquals(2, productService.search("testProduct").size());
    }
}
