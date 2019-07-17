package org.springsource.cloudfoundry.mvc.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.AbstractProduct;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Product;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.ProductBatch;
import fr.jmichel.devis.cloudfoundry.mvc.services.beans.WeighedProduct;
import fr.jmichel.devis.tools.DevisConstant;

@Service
@SuppressWarnings("unchecked")
@Transactional
public class ProductService {

	static private final String PRODUCTS_REGION = "products";

	@PersistenceContext
	private EntityManager em;

	public AbstractProduct createProduct(AbstractProduct newProduct) {
		em.persist(newProduct);
		return newProduct;
	}

	public Collection<AbstractProduct> search(String name) {
		String sqlName = ("%" + name + "%").toLowerCase();
		String sql = "select p.id from product p where (LOWER( p.name ) LIKE :fn OR LOWER( p.name ) LIKE :ln)";
		
		List<Integer> idProducts = em.createNativeQuery(sql)
				.setParameter("fn", sqlName)
				.setParameter("ln", sqlName)
				.getResultList();
		
		List<AbstractProduct> products = new ArrayList<AbstractProduct>();

		for (Integer id : idProducts) {
			products.add(getProductById(id));
		}

		return products;
		
	}

	@Transactional(readOnly = true)
	public List<AbstractProduct> getAllProducts() {
		String sql = "select p.* from product p";

		List objectsProducts = em.createNativeQuery(sql).getResultList();
		List<AbstractProduct> products = new ArrayList<AbstractProduct>();

		for (Object objectProduct : objectsProducts) {
			if (objectProduct instanceof Product) {
				products.add((Product) objectProduct);
				break;
			}

			if (objectProduct instanceof ProductBatch) {
				products.add((ProductBatch) objectProduct);
				break;
			}

			if (objectProduct instanceof WeighedProduct) {
				products.add((WeighedProduct) objectProduct);
			}
		}

		return products;
	}

	@Cacheable(PRODUCTS_REGION)
	@Transactional(readOnly = true)
	public AbstractProduct getProductById(Integer id) {
		String sqlProduct = "select p.* from product p where id = :ln";
		String sqlTypeOfSale = "select p.typeOfSale from product p where id = :ln";

		
		String typeOfProduct = String.valueOf(em.createNativeQuery(sqlTypeOfSale).setParameter("ln", id).getSingleResult());
		
		AbstractProduct product = null;
		
		switch (typeOfProduct) {
		case DevisConstant.TYPE_OF_SALE_SIMPLE:
			return (Product) em.createNativeQuery(sqlProduct, Product.class).setParameter("ln", id).getSingleResult();

		case DevisConstant.TYPE_OF_SALE_BATCHES:
			return (ProductBatch) em.createNativeQuery(sqlProduct, ProductBatch.class).setParameter("ln", id).getSingleResult();
		
		case DevisConstant.TYPE_OF_SALE_WEIGTH:
			return (WeighedProduct) em.createNativeQuery(sqlProduct, WeighedProduct.class).setParameter("ln", id).getSingleResult();
		}

		return product;
	}
	
	@Cacheable(PRODUCTS_REGION)
	@Transactional(readOnly = true)
	public Collection<ProductBatch> getProductBatchByName(String name) {
		String sqlProduct = "select p.* from product p where name = :ln and p.typeOfSale = 'batches' order by nbProduct desc";
		
		return em.createNativeQuery(sqlProduct, ProductBatch.class).setParameter("ln", name).getResultList();
	
	}

//	@CacheEvict(PRODUCTS_REGION)
//	public void deleteProduct(Integer id) {
//		AbstractProduct product = getProductById(id);
//		em.remove(product);
//	}

	@CacheEvict(value = PRODUCTS_REGION, key = "#id")
	public void updateProduct(Integer id, AbstractProduct productUpdated) {
		
		em.merge(productUpdated);
	}
}
