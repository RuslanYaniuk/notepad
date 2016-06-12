package com.mynote.utils;

import com.mynote.dto.MessageDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author Ruslan Yaniuk
 * @date September 2015
 */
public class CustomMessageSource implements MessageSource {

    private ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();

    public MessageDTO getMessageDTO(String code, Locale locale, String... args) {
        return new MessageDTO(code, getMessage(code, args, locale));
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        return source.getMessage(code, args, defaultMessage, locale);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return source.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return source.getMessage(resolvable, locale);
    }

    public void setBasename(String baseName) {
        source.setBasename(baseName);
    }

    public void setDefaultEncoding(String encoding) {
        source.setDefaultEncoding(encoding);
    }
}
