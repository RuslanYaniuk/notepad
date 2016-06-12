package com.mynote.config.security;

import com.mynote.config.security.filters.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.mynote.config.Constants.APPLICATION_ENCODING;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Configuration
@EnableWebSecurity
@ComponentScan("com.mynote.config.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/api/auth/login";
    public static final String LOGOUT_URL = "/api/auth/logout";

    @Autowired
    private AccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private AuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private LogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private JsonUsernamePasswordAuthenticationFilter jsonAuthFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/*",
                        "/notes/**",
                        "/assets/**",
                        "/vendor/**",
                        "/src/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter utfConverter = new CharacterEncodingFilter();
        utfConverter.setEncoding(APPLICATION_ENCODING);
        utfConverter.setForceEncoding(true);

        http
                .addFilterBefore(utfConverter, CsrfFilter.class)
                .addFilterBefore(jsonAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()

                .authorizeRequests()
                .antMatchers(
                        "/api/auth/*",
                        "/api/registration/*",
                        LOGOUT_URL).permitAll()

                .antMatchers(
                        "/api/administration/**",
                        "/api/user/**").hasRole("ADMIN")
                .antMatchers(
                        "/api/user/**",
                        "/api/note/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()

                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .logoutUrl(LOGOUT_URL)
                .invalidateHttpSession(true)
                .and()

                .csrf();
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonAuthFilter(ProviderManager providerManager,
                                                                   AuthenticationFailureHandler customAuthenticationFailureHandler,
                                                                   AuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        JsonUsernamePasswordAuthenticationFilter jsonAuthFilter =
                new JsonUsernamePasswordAuthenticationFilter(LOGIN_URL, "POST");

        jsonAuthFilter.setAuthenticationManager(providerManager);
        jsonAuthFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        jsonAuthFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        return jsonAuthFilter;
    }

    @Bean
    public List<AuthenticationProvider> customAuthenticationProviders(AuthenticationProvider customAuthenticationProvider) {
        List<AuthenticationProvider> providers = new ArrayList<>();

        providers.add(customAuthenticationProvider);
        return providers;
    }

    @Bean
    public ProviderManager providerManager(List<AuthenticationProvider> customAuthenticationProviders) {
        return new ProviderManager(customAuthenticationProviders);
    }
}
