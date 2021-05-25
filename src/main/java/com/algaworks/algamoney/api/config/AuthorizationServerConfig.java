package com.algaworks.algamoney.api.config;

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

import com.algaworks.algamoney.api.config.property.AlgaMoneyApiProperty;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AlgaMoneyApiProperty algaMoneyApiProperty;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		String secret = (new BCryptPasswordEncoder()).encode("algaworks");

		clients.inMemory().withClient("angular").secret(secret).scopes("read", "write", "erase")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(algaMoneyApiProperty.getAccessTokenValiditySeconds())
				.refreshTokenValiditySeconds(algaMoneyApiProperty.getRefreshTokenValiditySeconds()).and()
				.withClient("mobile").secret(secret).scopes("read", "write", "erase")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(algaMoneyApiProperty.getAccessTokenValiditySeconds())
				.refreshTokenValiditySeconds(algaMoneyApiProperty.getRefreshTokenValiditySeconds());

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
