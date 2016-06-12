package com.mynote.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.mynote.utils.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.List;

import static com.mynote.config.Constants.MEDIA_TYPE_APPLICATION_JSON_UTF8;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
@Configuration
@ComponentScan("com.mynote.controllers")
public class WebConfig extends WebMvcConfigurationSupport {

    public static final String VIEWS_LOCATION = "/WEB-INF/app/bin/";
    public static final String VIEW_SUFFIX = ".html";

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private HttpMessageConverter<String> responseBodyConverter;

    @Autowired
    private CustomValidator customValidator;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/vendor/**").addResourceLocations("/WEB-INF/app/bin/vendor/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/WEB-INF/app/bin/assets/");
        registry.addResourceHandler("/src/**").addResourceLocations("/WEB-INF/app/bin/src/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        converters.add(responseBodyConverter);
        converters.add(new MappingJackson2HttpMessageConverter(jacksonObjectMapper));
    }

    @Override
    public Validator mvcValidator() {
        return customValidator;
    }

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();

        resolver.setViewClass(InternalResourceView.class);
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
    public HttpMessageConverter jsonConverter(ObjectMapper jacksonObjectMapper) {
        MappingJackson2HttpMessageConverter jacksonConverter = new
                MappingJackson2HttpMessageConverter();

        jacksonConverter.setSupportedMediaTypes(Lists.newArrayList(MEDIA_TYPE_APPLICATION_JSON_UTF8));
        jacksonConverter.setObjectMapper(jacksonObjectMapper);
        return jacksonConverter;
    }
}
