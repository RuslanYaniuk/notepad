package com.mynote.config.security;

import com.mynote.config.security.filters.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static com.mynote.config.web.Constants.APPLICATION_ENCODING;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/api/login";
    public static final String LOGOUT_URL = "/api/logout";

    @Autowired
    private ProviderManager providerManager;

    @Autowired
    private JsonUsernamePasswordAuthenticationFilter jsonAuthFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/bin/**",
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
                .accessDeniedHandler(customAccessDeniedHandler())
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .and()

                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/api/login/*",
                        "/api/registration/*",
                        LOGOUT_URL).permitAll()

                .antMatchers("/api/administration/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()

                .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler())
                .logoutUrl(LOGOUT_URL)
                .invalidateHttpSession(true)
                .and()

                .csrf();
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonAuthFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonAuthFilter =
                new JsonUsernamePasswordAuthenticationFilter(LOGIN_URL, "POST");

        jsonAuthFilter.setAuthenticationManager(providerManager);
        jsonAuthFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());
        jsonAuthFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler());

        return jsonAuthFilter;
    }

    @Bean
    public List<AuthenticationProvider> customAuthenticationProviders() {
        List<AuthenticationProvider> providers = new ArrayList<>();

        providers.add(customAuthenticationProvider());
        return providers;
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(customAuthenticationProviders());
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }
}
