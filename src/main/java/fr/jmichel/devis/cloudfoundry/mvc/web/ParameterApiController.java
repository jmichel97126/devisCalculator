package fr.jmichel.devis.cloudfoundry.mvc.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springsource.cloudfoundry.mvc.services.ParameterService;

import fr.jmichel.devis.cloudfoundry.mvc.services.beans.Parameter;

@Controller
public class ParameterApiController {
	
	@Autowired
	private ParameterService parameterService;

	private static final String PARAMETERS_GET_LIST_TYPE_OF_SALES_URL = "/prm/getListTypeOfSales";
	private static final String PARAMETERS_TYPE_OF_SALES = "TOS";

	@ResponseBody
	@RequestMapping(value = PARAMETERS_GET_LIST_TYPE_OF_SALES_URL, method = RequestMethod.GET)
	public Collection<Parameter> getListTypeOfSales() throws Exception {
		return parameterService.getParametersByType(PARAMETERS_TYPE_OF_SALES);
	}

}