package com.algaworks.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("algamoney")
public class AlgaMoneyApiProperty {

	private String originPermitida;
	private int accessTokenValiditySeconds;
	private int refreshTokenValiditySeconds;
	private final Seguranca seguranca = new Seguranca();
	private final Mail mail = new Mail();

	@Setter
	@Getter
	public static class Seguranca {

		private boolean enableHttps;

	}

	@Setter
	@Getter
	public static class Mail {

		private String host;
		private String port;
		private String username;
		private String password;
		private String email;
		private String from;
		private boolean ativo;

	}

}
