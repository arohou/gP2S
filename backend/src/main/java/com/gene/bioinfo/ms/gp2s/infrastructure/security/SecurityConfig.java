/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gene.bioinfo.ms.gp2s.infrastructure.security;

import com.gene.bioinfo.ms.gp2s.web.ApiParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableWebMvcSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	LdapAuthProperties ldapAuthProperties;

	@Autowired
	private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

	@Autowired
	private NonRedirectingSavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

	private final SimpleUrlAuthenticationFailureHandler  authenticationFailureHandler =
			new SimpleUrlAuthenticationFailureHandler();


	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**", "/webapp/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		if(ldapAuthProperties.isLdapAuthEnabled()) {
			runSecured(http);
		} else {
			runUnsecured(http);
		}
	}

	private void runUnsecured(HttpSecurity http) throws Exception {
		log.info("Running unsecured");
		http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
	}

	private void runSecured(HttpSecurity http) throws Exception {
		http
				.formLogin()
				.loginProcessingUrl(ApiParameters.API_V1 + "/login")
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler)
				.permitAll()
			.and()
				.logout()
				.logoutUrl(ApiParameters.API_V1 + "/logout")
				.permitAll()
			.and()
				.authorizeRequests()
            	.antMatchers(ApiParameters.API_V1 + "/**").authenticated()
			.and()
            	.requestCache().requestCache(new NullRequestCache())
            .and()
            	.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(http401UnauthorizedEntryPoint);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (!ldapAuthProperties.isLdapAuthEnabled()) {
			log.info("Running unsecured. No valid LDAP Authentication properties configured");
			return;
		}

		log.info("Running secured with LDAP authentication");
		log.debug(ldapAuthProperties.toString());

		auth
            .ldapAuthentication()
            .userSearchFilter(ldapAuthProperties.getUserSearchQuery())
            .contextSource()
            .url(ldapAuthProperties.getUrl())
            .managerDn(ldapAuthProperties.getManagerDn())
            .managerPassword(ldapAuthProperties.getManagerPassword());
	}
}
