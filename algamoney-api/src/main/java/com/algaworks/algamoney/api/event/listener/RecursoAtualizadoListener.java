package com.algaworks.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.event.RecursoAtualizadoEvent;

@Component
public class RecursoAtualizadoListener implements ApplicationListener<RecursoAtualizadoEvent> {

	@Override
	public void onApplicationEvent(RecursoAtualizadoEvent event) {

		HttpServletResponse response = event.getResponse();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand("").toUri();
		response.setHeader("Location", uri.toASCIIString());

	}

}
