package fr.jmichel.devis.tools.proxy.order;

import java.math.BigDecimal;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;

public interface Order {
	public void addProductInOrder(AbstractProduct product, BigDecimal quantity);

}
