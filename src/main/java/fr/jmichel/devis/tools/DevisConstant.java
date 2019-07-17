package fr.jmichel.devis.tools;

import java.math.MathContext;

import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes
public class DevisConstant {

	public static final String TYPE_OF_SALE_SIMPLE = "simple";
	public static final String TYPE_OF_SALE_WEIGTH = "weight";
	public static final String TYPE_OF_SALE_BATCHES = "batches";
	
	public static final MathContext MATH_CONTEXT = new MathContext(2);
	

}
