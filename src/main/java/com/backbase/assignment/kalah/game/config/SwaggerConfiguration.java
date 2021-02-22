package com.backbase.assignment.kalah.game.config;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configures the spring fox swagger.
 * 
 * @author @vanish
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", value = "enable", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {
	@Autowired
	private SwaggerProperties properties;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.forCodeGeneration(true).directModelSubstitute(ByteBuffer.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class).select()
				.apis(RequestHandlerSelectors.basePackage("com.backbase.assignment.kalah.game")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(properties.getTitle()).description(properties.getDescription())
				.license(properties.getLicense()).licenseUrl(properties.getLicenseUrl())
				.termsOfServiceUrl(properties.getTermsOfServiceUrl()).version(properties.getVersion())
				.contact(new Contact(properties.getContactName(), properties.getContactUrl(),
						properties.getContactEmail()))
				.build();
	}
}
