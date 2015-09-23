package com.mynote.dto;

import org.springframework.security.web.csrf.CsrfToken;

/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
public class CsrfTokenDTO {
    private String headerName;
    private String headerValue;

    public CsrfTokenDTO() {
    }

    public CsrfTokenDTO(CsrfToken csrfToken) {
        this.headerName = csrfToken.getHeaderName();
        this.headerValue = csrfToken.getToken();
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }
}
