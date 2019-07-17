package org.springsource.cloudfoundry.mvc.services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Parameter;


@Service
@SuppressWarnings("unchecked")
@Transactional
public class ParameterService {
	
    static private final String PARAMETERS_REGION = "parameters";
    
    @PersistenceContext
    private EntityManager em;


//    @Transactional(readOnly = true)
//    public Collection<Parameter> getAllParameters() {
//    	String sql = "select p.* from parameter p";
//        return em.createNativeQuery(sql, Parameter.class).getResultList();
//    }
//
//
//    @Cacheable(PARAMETERS_REGION)
//    @Transactional(readOnly = true)
//    public Parameter getParameterById(Integer id) {
//        return em.find(Parameter.class, id);
//    }
    
    @Cacheable(PARAMETERS_REGION)
    @Transactional(readOnly = true)
    public Collection<Parameter> getParametersByType(String type) {
        String sql = "select p.* from parameter p where p.type = :ln";
        return em.createNativeQuery(sql, Parameter.class)
                .setParameter("ln", type)
                .getResultList();
    	
    }
}
