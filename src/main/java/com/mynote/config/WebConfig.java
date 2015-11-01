package com.mynote.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mynote.config.db.DatabaseInitializer;
import com.mynote.config.security.SecurityConfig;
import com.mynote.config.validation.CustomValidator;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
@ComponentScan(basePackages = {"com.mynote"})
@Configuration
@Import({SecurityConfig.class})
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /* Dev mode assets*/
        registry.addResourceHandler("/bin/assets/**").addResourceLocations("/WEB-INF/app/client/assets/").setCachePeriod(31556926);
        registry.addResourceHandler("/assets/**").addResourceLocations("/WEB-INF/app/client/assets/").setCachePeriod(31556926);
        registry.addResourceHandler("/bin/src/**").addResourceLocations("/WEB-INF/app/client/src/").setCachePeriod(31556926);
        registry.addResourceHandler("/bin/vendor/**").addResourceLocations("/WEB-INF/app/client/vendor/").setCachePeriod(31556926);
        registry.addResourceHandler("/vendor/**").addResourceLocations("/WEB-INF/app/client/vendor/").setCachePeriod(31556926);
        registry.addResourceHandler("/src/**").addResourceLocations("/WEB-INF/app/client/src/").setCachePeriod(31556926);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        converters.add(responseBodyConverter());
        converters.add(jsonConverter());
    }

    @Bean
    public ViewResolver getInternalResourceViewResolver() {
        UrlBasedViewResolver resolver = new ChainableUrlBasedViewResolver();

        resolver.setPrefix("/WEB-INF/app/client/");
        resolver.setSuffix(".html");
        return resolver;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();

        converter.setSupportedMediaTypes(Arrays.asList(MEDIA_TYPE_APPLICATION_JSON_UTF8));
        return converter;
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Bean
    public HttpMessageConverter jsonConverter() {
        MappingJackson2HttpMessageConverter jacksonConverter = new
                MappingJackson2HttpMessageConverter();

        jacksonConverter.setSupportedMediaTypes(Arrays.asList(MEDIA_TYPE_APPLICATION_JSON_UTF8));
        jacksonConverter.setObjectMapper(jacksonObjectMapper());
        return jacksonConverter;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExtendedMessageSource messageSource() {
        ExtendedMessageSource source = new ExtendedMessageSource();

        source.setBasename("classpath:messages/messages");
        source.setDefaultEncoding("UTF-8");

        return source;
    }

    @Bean(initMethod = "init")
    @Profile({"dev", "prod"})
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer();
    }

    @Bean
    public DataSource dataSource() {
        Context envContext;
        DataSource dataSource;

        try {
            envContext = (Context) initialContext().lookup("java:/comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/mynote_datasource");
        } catch (NamingException e) {
            throw new BeanCreationException("dataSource", e);
        }

        return dataSource;
    }

    @Bean
    public InitialContext initialContext() throws NamingException {
        return new InitialContext();
    }

    @Override
    public Validator mvcValidator() {
        return new CustomValidator();
    }
}
