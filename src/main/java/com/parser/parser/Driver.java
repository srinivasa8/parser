package com.parser.parser;

import com.parser.parser.client.GitRepositoryService;
import com.parser.parser.service.ParserService;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class Driver {

    public static void main(String[] args) {

        String localPath = "{LOCALPATH}" + new Date().getTime() + "\\";
        String apiKey = "{GITHUB_API_KEY}";
        String userName = "{GITHUB_USER_NAME}";
        String apiUrl = "{GITHUB_API_URL}";

        RestTemplate restTemplate = new RestTemplate();


        restTemplate.getInterceptors().add(
                (request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " + apiKey);
                    return execution.execute(request, body);
                }
        );

        GitRepositoryService gs = new GitRepositoryService(
                new UsernamePasswordCredentialsProvider(userName, apiKey),
                restTemplate,
                apiUrl,
                localPath);

        ParserService ps = new ParserService(gs, localPath);
        ps.parse();

    }
}
