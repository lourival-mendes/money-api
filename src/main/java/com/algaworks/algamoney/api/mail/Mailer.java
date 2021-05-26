package com.algaworks.algamoney.api.mail;

import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AlgaMoneyApiProperty property;

	@EventListener
	public void test(ApplicationReadyEvent event) {
		
		System.out.println("Aplicação rodando!");
	  
	  }
	  
	public void enviarEmail(List<String> destinatarios, String assunto, String mensagem) {

		try {

			if (property.getMail().isAtivo()) {

				MimeMessage mimeMessage = mailSender.createMimeMessage();

				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
				helper.setFrom(property.getMail().getFrom());
				helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
				helper.setSubject(assunto);
				helper.setText(mensagem, true);

				mailSender.send(mimeMessage);

				System.out.println("Envio de e-mail realizado.");

			} else {

				System.out.println("Envio de e-mail está desativado, não foi enviado.");

			}

		} catch (Exception e) {

			System.out.println("Problemas com envio de e-mail");
			throw new RuntimeException(e);

		}
	}

}
