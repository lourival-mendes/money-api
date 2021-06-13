package br.com.lourivalmendes.money.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import br.com.lourivalmendes.money.api.config.property.MoneyApiProperty;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MoneyApiProperty moneyApiProperty;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		String secret = (new BCryptPasswordEncoder()).encode("algaworks");

		clients.inMemory().withClient("angular").secret(secret).scopes("read", "write", "erase")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(moneyApiProperty.getAccessTokenValiditySeconds())
				.refreshTokenValiditySeconds(moneyApiProperty.getRefreshTokenValiditySeconds()).and()
				.withClient("mobile").secret(secret).scopes("read", "write", "erase")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(moneyApiProperty.getAccessTokenValiditySeconds())
				.refreshTokenValiditySeconds(moneyApiProperty.getRefreshTokenValiditySeconds());

	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtAccessTokenConverter()).reuseRefreshTokens(false)
				.userDetailsService(userDetailsService).authenticationManager(authenticationManager);
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey("algaworks");
		return jwtAccessTokenConverter;
	}

}
