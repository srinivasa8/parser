package com.parser.parser.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

class RestTemplateConfig{

    @Autowired
    Environment env;

    @Bean
    RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(
                (request,body,execution)->{ request.getHeaders().add("Authorization","Bearer "+env.getProperty("apikey"));
                    return execution.execute(request,body);
                }
        );
        return restTemplate;
    }

}