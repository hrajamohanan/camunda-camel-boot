package de.unioninvestment;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.camunda.bpm.camel.component.CamundaBpmComponent;
import org.camunda.bpm.camel.spring.CamelServiceImpl;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CamundaCamelBootConfiguration {

	@Autowired
	CamelContext camelContext;

	@Autowired
	ProcessEngine processEngine;

	@Bean(name = "camel")
	public CamelServiceImpl camel() {
		CamelServiceImpl camelServiceImpl = new CamelServiceImpl();
		camelServiceImpl.setCamelContext(camelContext);
		camelServiceImpl.setProcessEngine(processEngine);
		return camelServiceImpl;
	}
	
	
	@Bean
	CamelContextConfiguration nameConfiguration() {
		return new CamelContextConfiguration() {
			@Override
			public void beforeApplicationStart(CamelContext camelContext) {
				CamundaBpmComponent component = new CamundaBpmComponent(processEngine);
				SpringProcessEngineConfiguration config =  (SpringProcessEngineConfiguration)processEngine.getProcessEngineConfiguration();
				config.getBeans().put("camel", camel());
				camelContext.addComponent("camunda-bpm", component);
			}

			@Override
			public void afterApplicationStart(CamelContext arg0) {

			}
		};
	}
	
}
