package com.parser.parser.controller;

import com.parser.parser.service.ParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ParserController {

    @Autowired
    ParserService parserService;

    @GetMapping("/parseDependencies")
    void parseDependencies(){
         parserService.parse();
    }
}
