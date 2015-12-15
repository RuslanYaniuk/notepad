package com.mynote.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.common.collect.Lists;
import com.mynote.config.db.DatabaseInitializer;
import com.mynote.config.validation.CustomValidator;
import com.mynote.config.web.ChainableUrlBasedViewResolver;
import com.mynote.config.web.ExtendedMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

import java.util.List;

import static com.mynote.config.web.Constants.APPLICATION_ENCODING;
import static com.mynote.config.web.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
@Configuration
public abstract class WebConfig extends WebMvcConfigurationSupport {

    public static final String MESSAGES_LOCATION = "messages/messages";

    public static final String VIEWS_LOCATION = "/WEB-INF/app/client/";
    public static final String VIEW_SUFFIX = ".html";

    @Autowired
    private HttpMessageConverter jsonConverter;

    @Autowired
    private HttpMessageConverter<String> responseBodyConverter;

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

        converters.add(responseBodyConverter);
        converters.add(jsonConverter);
    }

    @Bean
    public ViewResolver getInternalResourceViewResolver() {
        UrlBasedViewResolver resolver = new ChainableUrlBasedViewResolver();

        resolver.setPrefix(VIEWS_LOCATION);
        resolver.setSuffix(VIEW_SUFFIX);
        return resolver;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter();

        converter.setSupportedMediaTypes(Lists.newArrayList(MEDIA_TYPE_APPLICATION_JSON_UTF8));
        return converter;
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JSR310Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return objectMapper;
    }

    @Bean
    public HttpMessageConverter jsonConverter(ObjectMapper jacksonObjectMapper) {
        MappingJackson2HttpMessageConverter jacksonConverter = new
                MappingJackson2HttpMessageConverter();

        jacksonConverter.setSupportedMediaTypes(Lists.newArrayList(MEDIA_TYPE_APPLICATION_JSON_UTF8));
        jacksonConverter.setObjectMapper(jacksonObjectMapper);

        return jacksonConverter;
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ExtendedMessageSource messageSource() {
        ExtendedMessageSource source = new ExtendedMessageSource();

        source.setBasename("classpath:" + MESSAGES_LOCATION);
        source.setDefaultEncoding(APPLICATION_ENCODING);

        return source;
    }

    @Bean(initMethod = "init")
    @Profile({"dev", "prod"})
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer();
    }

    @Override
    public Validator mvcValidator() {
        return new CustomValidator();
    }
}
