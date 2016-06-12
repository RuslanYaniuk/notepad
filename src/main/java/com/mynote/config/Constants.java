package com.mynote.config;

import com.mynote.models.UserRole;
import com.mynote.services.UserRoleService;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public interface Constants {
    String APPLICATION_ENCODING = StandardCharsets.UTF_8.displayName();

    String APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8).toString();

    MediaType MEDIA_TYPE_APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    String SYSTEM_MESSAGE_CODE = "system-message-code";

    UserRole ROLE_ADMIN = new UserRole(UserRoleService.ROLE_ADMIN);
    UserRole ROLE_USER = new UserRole(UserRoleService.ROLE_USER);
    SimpleGrantedAuthority ROLE_ANONYMOUS = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
}
