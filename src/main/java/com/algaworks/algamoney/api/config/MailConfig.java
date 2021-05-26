package com.algaworks.algamoney.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;

@Configuration
public class MailConfig {

	@Autowired
	private AlgaMoneyApiProperty property;

	@Bean
	public JavaMailSender javaMailSender() {

		Properties props = new Properties();
		props.put("mail.transport.protocol", "gsmtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.startTLS.enable", true);
		props.put("mail.smtp.connectiontimeout", 1000 * 10);

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		mailSender.setHost(property.getMail().getHost());
		mailSender.setPort(Integer.parseInt(property.getMail().getPort()));
		mailSender.setUsername(property.getMail().getUsername());
		mailSender.setPassword(property.getMail().getPassword());

		return mailSender;

	}
}
