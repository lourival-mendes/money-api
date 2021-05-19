package com.algaworks.algamoney.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class RecursoAtualizadoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private HttpServletResponse response;

	public RecursoAtualizadoEvent(Object source, HttpServletResponse response) {
		super(source);
		this.response = response;
	}

}
