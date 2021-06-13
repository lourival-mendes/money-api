package br.com.lourivalmendes.money.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("money")
public class MoneyApiProperty {

	private String originPermitida;
	private int accessTokenValiditySeconds;
	private int refreshTokenValiditySeconds;
	private final Seguranca seguranca = new Seguranca();
	private final Mail mail = new Mail();
	private final S3 s3 = new S3();

	@Setter
	@Getter
	public static class Seguranca {

		private boolean enableHttps;

	}

	@Setter
	@Getter
	public static class Mail {

		private String host;
		private int port;
		private String username;
		private String password;
		private String email;
		private String from;
		private boolean ativo;

	}

	@Setter
	@Getter
	public static class S3 {

		private String accessKeyId;
		private String secretAccessKey;
		private String bucket="lourival-mendes-files";

	}

}
