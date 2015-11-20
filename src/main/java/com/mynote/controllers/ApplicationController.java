package com.mynote.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Ruslan Yaniuk
 * @date Jun 2015
 */
@Controller
@RequestMapping("/")
public class ApplicationController {

    @RequestMapping(method = GET)
    public String index() {
        return "index";
    }
}
