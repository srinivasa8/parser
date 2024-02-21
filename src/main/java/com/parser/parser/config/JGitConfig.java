package com.parser.parser.config;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
class JGitConfig{

    @Autowired
    Environment env;

    @Bean
    UsernamePasswordCredentialsProvider credentialsProvider(){
        return new UsernamePasswordCredentialsProvider(env.getProperty("username"), env.getProperty("apikey"));
    }

}