package com.algaworks.algamoney.api.mail;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AlgaMoneyApiProperty property;

	@Autowired
	private TemplateEngine thymeleaf;

	@EventListener
	public void test(ApplicationReadyEvent event) {

		System.out.println("Aplicação rodando!");

	}

	public void enviarEmail(List<String> destinatarios, String assunto, String template,
			Map<String, Object> variaveis) {

		Context context = new Context(new Locale("pt", "BR"));

		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

		String mensagem = thymeleaf.process(template, context);

		this.enviarEmail(destinatarios, assunto, mensagem);

	}

	public void enviarEmail(List<String> destinatarios, String assunto, String mensagem) {

		try {

			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(property.getMail().getFrom());
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			if (property.getMail().isAtivo()) {
				mailSender.send(mimeMessage);

				System.out.println("Envio de e-mail realizado.");

			} else {

				System.out.println("Envio de e-mail está desativado, não foi enviado.");
				System.out.println(mensagem);

			}

		} catch (Exception e) {

			System.out.println("Problemas com envio de e-mail");
			throw new RuntimeException(e);

		}
	}

}
