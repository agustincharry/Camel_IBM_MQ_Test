package org.agustincharry;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {

        from("ibmmq:queue:Q1")
                .log("Nuevo mensaje recibido: \"${body}\"")
                .to("direct:validar")
                .to("direct:transformar")
                .to("direct:enviar");



        from("direct:validar")
                .log("Validando mensaje...")
                .to("validator:file:src/main/resources/schema.xsd")
                .log("Validado con éxito!!")
                .errorHandler(deadLetterChannel("direct:error"));

        from("direct:transformar")
                .log("Transformando mensaje...")
                .to("xslt:file:src/main/resources/transformation.xsl")
                .log("Transformado con éxito!!");

        from("direct:enviar")
                .log("Enviando mensaje desde Q1 a Q2")
                .to("ibmmq:queue:Q2")
                .log("Mensaje enviado con éxito!!");

        from("direct:error")
                .log("Ocurrió un error validando el mensaje!");

    }

}
