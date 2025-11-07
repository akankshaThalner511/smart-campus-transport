package com.ams.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Map URL paths like /uploads/** to the physical folder "uploads/" on disk.
	 * This allows serving QR codes and other files via HTTP.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**") // URL pattern
				.addResourceLocations("file:uploads/"); // Physical folder path
	}
}
