package com.mynote.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
@Controller
@RequestMapping("/")
public class ApplicationController {

    @RequestMapping(method = GET)
    public String index(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LoginController.applyCsrfCookies(httpServletRequest, httpServletResponse);

        return "index";
    }
}
