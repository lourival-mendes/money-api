package com.algaworks.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Autowired
	private AlgaMoneyApiProperty algaMoneyApiProperty;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String originPermitida = algaMoneyApiProperty.getOriginPermitida();

		httpServletResponse.setHeader("Access-Cotrol-Allow-Origin", originPermitida);
		httpServletResponse.setHeader("Access-Cotrol-Allow-Credentials", "true");

		if ("OPTIONS".equals(httpServletRequest.getMethod())
				&& originPermitida.equals(httpServletRequest.getHeader("Origin"))) {
			httpServletResponse.setHeader("Access-Cotrol-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			httpServletResponse.setHeader("Access-Cotrol-Allow-Headers", "Authorization, Content-Type, Accept");
			httpServletResponse.setHeader("Access-Cotrol-Allow-Max-Age", "3600");

			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else
			chain.doFilter(httpServletRequest, httpServletResponse);

	}

}
