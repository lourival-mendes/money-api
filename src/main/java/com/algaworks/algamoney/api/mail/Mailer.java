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

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@EventListener
	public void test(ApplicationReadyEvent event) {
		
		this.enviarEmail("mensagem@apis.com.br", Arrays.asList("minhavirtude@gmail.com"), "Teste de envio de e-mail", "Mensagem do este de envio de e-mail usando Spring.");

	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			//mailSender.send(mimeMessage);
			//TODO: liberar evio de e-mail
			
		} catch (Exception e) {
			throw new RuntimeException("Problemas com envio de e-mail", e);
		}
	}

}
