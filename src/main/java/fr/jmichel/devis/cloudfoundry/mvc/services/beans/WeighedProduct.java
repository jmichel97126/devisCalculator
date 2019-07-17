package fr.jmichel.devis.cloudfoundry.mvc.services.beans;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonTypeName;

import fr.jmichel.devis.tools.DevisConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Table(name = "Product")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@JsonTypeName(DevisConstant.TYPE_OF_SALE_WEIGTH)
public class WeighedProduct extends AbstractProduct {
	private static final long serialVersionUID = 3L;
	
	@Column(nullable = true)
	private BigDecimal weight;

	@Override
	public BigDecimal calculatePrice(BigDecimal quantity) {
		
		BigDecimal price = quantity.multiply(this.getPrice()).divide(this.weight).round(DevisConstant.MATH_CONTEXT);
		
		BigDecimal priceRate = this.isPromotion() ? price.multiply(this.getPromotionRate().divide(new BigDecimal(100))) : new BigDecimal(0);
		
		price = price.subtract(priceRate).round(DevisConstant.MATH_CONTEXT);
		
		return price;
	}

}
