package com.parser.parser.client;

import com.parser.parser.models.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitRepositoryService {

    @Autowired
    private UsernamePasswordCredentialsProvider credentialsProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${apiUrl}")
    private String apiUrl;

    @Value("${localPath}")
    private String localpath;

    public GitRepositoryService(UsernamePasswordCredentialsProvider credentialsProvider,
                                RestTemplate restTemplate, String apiUrl, String localPath) {
        this.credentialsProvider = credentialsProvider;
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.localpath=localPath;
    }
    public GitRepositoryService(UsernamePasswordCredentialsProvider credentialsProvider,
                                RestTemplate restTemplate) {
        this.credentialsProvider = credentialsProvider;
        this.restTemplate = restTemplate;
    }

    public GitRepositoryService(){

    }

    public List<String> getRepositoryFiles(String cloneUrl, String name) {
        List<String> pomFileList = new ArrayList<>();
        File path = new File(localpath + name);
        try {
            Git git = Git.cloneRepository()
                    .setURI(cloneUrl)
                    .setCredentialsProvider(credentialsProvider)
                    .setDirectory(path)
                    .call();
            File[] files = path.listFiles();
            for (File file : files) {
                findAndAddPomFile(pomFileList,file);
            }
            git.getRepository().close();
        } catch (GitAPIException e) {
            System.out.println("Exception occurred while getRepositoriesFiles / cloning repository for :" + name+ " with error:"+e);
        }
        return pomFileList;
    }

    private void findAndAddPomFile(List<String> pomFileList, File file) {
        if (!file.isDirectory()) {
            if (file.getName().equalsIgnoreCase("pom.xml")) {
                //If file found is pom.xml only, Add it to the pomFileList.
                pomFileList.add(file.getPath());
            } else {
                return;
            }
        } else {
            //If file is a directory, recursively look for pom.xml files.
            if (file.listFiles() == null) return;
            for (File newfile : file.listFiles()) {
                findAndAddPomFile(pomFileList, newfile);
            }
        }
    }

    public Repository[] getAllRepositories() {
        Repository[] repositories = new Repository[]{};
        try {
            repositories = restTemplate.getForObject(apiUrl, Repository[].class);
        } catch (Exception e) {
            System.out.println("Error occurred while getAllRepositories call with error:"+e);
        }
        return repositories;
    }

}
