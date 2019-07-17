package org.springsource.cloudfoundry.mvc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springsource.cloudfoundry.mvc.services.config.ServicesConfiguration;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Parameter;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration
public class ParameterServiceTest {


    @Autowired
    private ParameterService parameterService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    Date signupDate = new Date();

    String typeParameterForTypeOfSales = "TOS";

    String lastName = "Long";

    JdbcTemplate jdbcTemplate;


    @Configuration
    @Import({ServicesConfiguration.class})
    public static class ParameterServiceTestConfiguration {
        // noop we just want the beans in the ServicesConfiguration class
    }


//    @Before
//    public void before() throws Exception {
//
//        jdbcTemplate = new JdbcTemplate(dataSource);
//
//        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
//        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
//                jdbcTemplate.execute("delete from CUSTOMER");
//            }
//        });
//    }

    @Test
    public void testGetParameterByType() {
    	List<Parameter> parametersExpected = new ArrayList<>();
    	parametersExpected.add(Parameter.builder().id(1).name("Simple").type("TOS").code("simple").build());
    	parametersExpected.add(Parameter.builder().id(2).name("Sold in batches").type("TOS").code("batches").build());
    	parametersExpected.add(Parameter.builder().id(3).name("Sold by weight").type("TOS").code("weight").build());
    	
    	
    	Collection<Parameter> typeOfSales = parameterService.getParametersByType(typeParameterForTypeOfSales);
    	
    	assertTrue(typeOfSales.containsAll(parametersExpected));
    	
    }
}
