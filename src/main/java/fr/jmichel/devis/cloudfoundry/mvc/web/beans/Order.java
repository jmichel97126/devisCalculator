package fr.jmichel.devis.cloudfoundry.mvc.web.beans;

import java.math.BigDecimal;
import java.util.Map;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order{

	private int id;	
	private Map<AbstractProduct, BigDecimal> products;

}
