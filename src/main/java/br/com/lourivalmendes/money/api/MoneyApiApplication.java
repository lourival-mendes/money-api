package br.com.lourivalmendes.money.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.lourivalmendes.money.api.config.property.MoneyApiProperty;

@SpringBootApplication
@EnableConfigurationProperties(MoneyApiProperty.class)
public class MoneyApiApplication {

	private static ApplicationContext APPLICATION_CONTEXT;
	
	@Autowired
	private MoneyApiProperty moneyApiProperty;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(MoneyApiApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*").allowedOrigins("*")
				.allowCredentials(true)
				.allowedMethods("POST, GET, DELETE, PUT, OPTIONS")
				.allowedHeaders("Authorization, Content-Type, Accept")
				.maxAge(3600);
			}
		};
	}

}
