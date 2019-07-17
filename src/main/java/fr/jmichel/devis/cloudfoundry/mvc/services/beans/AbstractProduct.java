package fr.jmichel.devis.cloudfoundry.mvc.services.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonSubTypes;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import fr.jmichel.devis.tools.DevisConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Table(name = "Product")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=As.PROPERTY, property="typeOfSale")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Product.class, name = DevisConstant.TYPE_OF_SALE_SIMPLE),
    @JsonSubTypes.Type(value = ProductBatch.class, name = DevisConstant.TYPE_OF_SALE_BATCHES),
    @JsonSubTypes.Type(value = WeighedProduct.class, name = DevisConstant.TYPE_OF_SALE_WEIGTH)

})

public abstract class AbstractProduct implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@NotNull
	private String typeOfSale;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	 
	@NotNull
	private BigDecimal price;
	
    @NotNull
    private Date updateDate;

	
	@NotNull
	@Builder.Default
	private boolean promotion = false;
	
	@Column(nullable = true)
	private BigDecimal promotionRate;
	
	public abstract BigDecimal calculatePrice(BigDecimal quantity);
}
