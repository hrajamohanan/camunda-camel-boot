package org.wipro.saga.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.stereotype.Component;

@Component
public class CamelCamundaRoute extends RouteBuilder{
	
	public static String dropFolder = System.getProperty("user.home") + System.getProperty("file.separator")
	+ "camunda-bpm-demo-camel";
	
	@Override
	public void configure() throws Exception {
		

		from("timer://camundaStarter?fixedRate=true&period=60000")
		.id("camel-camunda-starter")
		.to("log:start_camunda_bpmn_from_camel_route")
		.setBody(constant("test"))
		.to("camunda-bpm://start?processDefinitionKey=camunda-camel");

		
		
		from("direct:2camel")
		.routeId("2camel") //
		.id("2camel")
		.to("log:camunda_bpmn_called_camel_route");


	
		// ################################
		// Drop folder starts via none start event
		from("file://" + dropFolder) // use drop folder
				.routeId("file") //
				.convertBodyTo(String.class) //
				.to("log:org.camunda.demo.camel?level=INFO&showAll=true&multiline=true") // logging
				.to("camunda-bpm:start?processDefinitionKey=camel-use-cases"); // and start process instance



		// ################################
		// Synchronous Service calles from process
		from("direct://syncService") // service name in memory
				.routeId("syncService") //
				.to("log:org.camunda.demo.camel?level=INFO&showAll=true&multiline=true") // logging
				.onException(SampleException.class) // map exception to BPMN error
				.throwException(new BpmnError("camel.error")) //
				.handled(true) // TODO: Check how we can avoid logging on console
				.end().process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						// always throwing error to demonstrate error event
						throw new SampleException("some error occured in service");
					}
				});

		// ################################
		// Asynchronous Service (triggering message callback)
		from("direct://asyncService") // service name
				.routeId("asyncService") //
				.to("seda:someQueue?waitForTaskToComplete=Never");

		// now some external service would do some magic
		// then send response
		from("seda:someQueue").routeId("asyncResponse") //
				.process(new Processor() {
					@Override
					public void process(Exchange arg0) throws Exception {
						// wait a bit to see that it is async
						for (int i = 0; i < 20; i++) {
							System.out.println("...zzzzzz...");
							Thread.sleep(100);
						}
					}
				}).to("camunda-bpm:message?messageName=camel.answer");

	}
}
