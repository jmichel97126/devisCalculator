package fr.jmichel.devis.tools.proxy.order;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springsource.cloudfoundry.mvc.services.ProductService;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Product;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.ProductBatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProxy{

	private fr.jmichel.devis.cloudfoundry.mvc.web.beans.Order order;
	
	@Autowired
	private ProductService productService;
	
	public void addProductInOrder(AbstractProduct product, BigDecimal quantity) {
		
		if(product instanceof Product) {
			Collection<ProductBatch> productsBatch = productService.getProductBatchByName(product.getName());
			
			if(productsBatch.isEmpty() || quantity.compareTo(new BigDecimal(1)) == 0) {
				order.getProducts().put(product, quantity);
			}else {
				int insufficientQuantityForBatch = 0;
				
				while (quantity.compareTo(new BigDecimal(0)) > 0 && insufficientQuantityForBatch < productsBatch.size()) {
					
					for(ProductBatch productBatch : productsBatch) {
						if(quantity.compareTo(new BigDecimal(productBatch.getNbProduct())) >= 0) {
							int nbBatches = quantity.intValue()/productBatch.getNbProduct();
							order.getProducts().put(productBatch, new BigDecimal(nbBatches));
							quantity = new BigDecimal(quantity.intValue()%productBatch.getNbProduct());
							
						}else {
							insufficientQuantityForBatch++;
						}
					}
				}
				
				if(insufficientQuantityForBatch == productsBatch.size()) {
					order.getProducts().put(product, quantity);
				}
			}
			return;
		}else {
			order.getProducts().put(product, quantity);
		}

	}
	
	public BigDecimal calculateTotal() {
		
		BigDecimal total = new BigDecimal(0);
		for(AbstractProduct product : order.getProducts().keySet()) {
			total = total.add(product.calculatePrice(order.getProducts().get(product)));
		}
		return total;
	}

}
