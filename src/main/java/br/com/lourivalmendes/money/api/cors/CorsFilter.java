package br.com.lourivalmendes.money.api.cors;

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

import br.com.lourivalmendes.money.api.config.property.MoneyApiProperty;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CorsFilter implements Filter {

	@Autowired
	private MoneyApiProperty moneyApiProperty;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		String originPermitida = moneyApiProperty.getOriginPermitida();

		httpServletResponse.setHeader("Access-Control-Allow-Origin", originPermitida);
		httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");

		if ("OPTIONS".equals(httpServletRequest.getMethod())
				&& originPermitida.equals(httpServletRequest.getHeader("Origin"))) {
			httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			httpServletResponse.setHeader("Access-Control-Allow-Max-Age", "3600");

			httpServletResponse.setStatus(HttpServletResponse.SC_OK);
		} else
			chain.doFilter(httpServletRequest, httpServletResponse);
		
		System.out.println(httpServletResponse.getHeader("Origin"));

	}

}
