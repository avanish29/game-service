package com.backbase.assignment.kalah.game.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Properties specific to Swagger.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 * 
 * @author @vanish
 */
@ConfigurationProperties("swagger")
@Data
public class SwaggerProperties {
	private String title = "Application API";
	private String description = "Api Documentation";
	private String version = "0.0.1";
	private String contactName;
	private String contactUrl;
	private String contactEmail;
	private String basePackage;
	private String termsOfServiceUrl;
	private String license;
	private String licenseUrl;
}
