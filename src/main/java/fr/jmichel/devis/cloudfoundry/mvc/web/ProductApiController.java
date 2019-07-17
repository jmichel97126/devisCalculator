package fr.jmichel.devis.cloudfoundry.mvc.web;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springsource.cloudfoundry.mvc.services.ParameterService;
import org.springsource.cloudfoundry.mvc.services.ProductService;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.tools.factory.product.AbstractProductFactory;

@Controller
public class ProductApiController {
	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private ProductService productService;
	@Autowired
	private ParameterService parameterService;

	@Autowired
	private AbstractProductFactory productFactory;

	public static final String PRODUCTS_ENTRY_URL = "/prd/products";
	public static final String PRODUCTS_SEARCH_URL = "/prd/search";
	public static final String PRODUCTS_BY_ID_ENTRY_URL = PRODUCTS_ENTRY_URL + "/{id}";

	@ResponseBody
	@RequestMapping(value = PRODUCTS_SEARCH_URL, method = RequestMethod.GET)
	public Collection<AbstractProduct> search(@RequestParam("q") String query) throws Exception {
		Collection<AbstractProduct> products = productService.search(query);
		if (log.isDebugEnabled())
			log.debug(String.format("retrieved %s results for search query '%s'", Integer.toString(products.size()),
					query));
		return products;
	}

	@ResponseBody
	@RequestMapping(value = PRODUCTS_BY_ID_ENTRY_URL, method = RequestMethod.GET)
	public AbstractProduct productById(@PathVariable Integer id) {
		return this.productService.getProductById(id);
	}

	@ResponseBody
	@RequestMapping(value = PRODUCTS_ENTRY_URL, method = RequestMethod.GET)
	public List<AbstractProduct> products() {
		return this.productService.getAllProducts();
	}

	@ResponseBody
	@RequestMapping(value = PRODUCTS_ENTRY_URL, method = RequestMethod.PUT)
	public Integer addProduct(@RequestParam String name, @RequestParam BigDecimal price,
			@RequestParam String typeOfSale, @RequestParam boolean promotion, @RequestParam BigDecimal promotionRate,
			@RequestParam int nbProduct, @RequestParam BigDecimal weight) {

		AbstractProduct product = productFactory.createProduct(name, price, typeOfSale, promotion, promotionRate,
				nbProduct, weight);

		return productService.createProduct(product).getId();
	}

	@ResponseBody
	@RequestMapping(value = PRODUCTS_BY_ID_ENTRY_URL, method = RequestMethod.POST)
	public Integer updateProduct(@PathVariable Integer id, @RequestBody Object objectProduct) {

		LinkedHashMap<String, Object> objectProductMap = (LinkedHashMap<String, Object>) objectProduct;

		AbstractProduct product = productFactory.createProduct(
				(String) objectProductMap.get("name"), 
				new BigDecimal((String)objectProductMap.get("price")), 
				(String) objectProductMap.get("typeOfSale"),
				(boolean) objectProductMap.get("promotion"), 
				new BigDecimal((String)objectProductMap.get("promotionRate")),
				(int) objectProductMap.get("nbProduct"), 
				new BigDecimal((String) objectProductMap.get("weight")));
		
		product.setId(id);
		
		productService.updateProduct(id, product);

		return id;
	}

}