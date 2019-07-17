package fr.jmichel.devis.cloudfoundry.mvc.services.beans;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;

import javax.persistence.Entity;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonTypeName;

import fr.jmichel.devis.tools.DevisConstant;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@AllArgsConstructor
@JsonTypeName(DevisConstant.TYPE_OF_SALE_SIMPLE)
public class Product extends AbstractProduct {

	private static final long serialVersionUID = 2L;

	public BigDecimal calculatePrice(BigDecimal quantity) {
		BigDecimal price = this.getPrice().multiply(quantity);
		BigDecimal priceRate = this.isPromotion() ? price.multiply(this.getPromotionRate().divide(new BigDecimal(100))) : new BigDecimal(0);
		
		price = price.subtract(priceRate).round(DevisConstant.MATH_CONTEXT);
		return price;
	}

}
