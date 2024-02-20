package com.parser.parser.client;

import com.parser.parser.models.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitRepositoryService {

    @Autowired
    private Environment env;

    @Autowired
    private UsernamePasswordCredentialsProvider credentialsProvider;

    public GitRepositoryService(Environment env) {
        this.env = env;
    }

    @Value("${localpath}")
    private String localPath;

    @Autowired
    private RestTemplate restTemplate;

    public List<String> getRepositoryFiles(String cloneUrl, String name) {
        List<String> pomFileList = new ArrayList<>();
        File path = new File(localPath + name);
        try {
            Git git = Git.cloneRepository()
                    .setURI(cloneUrl)
                    .setCredentialsProvider(credentialsProvider)
                    .setDirectory(path)
                    .call();
            File[] files = path.listFiles();
            for (File file : files) {
                System.out.println(file.getName()+"-->"+ file.isDirectory());
                findAndAddPomFile(pomFileList,file);
//                if (file.getName().equalsIgnoreCase("pom.xml")) {
//                    System.out.println("file-name:==========> " + file.getName());
//                    pomFileList.add(file.getPath());
//                }
            }
            git.getRepository().close();
        } catch (GitAPIException e) {
            System.out.println("Exception occurred while getRepositoriesFiles / cloning repository for :" + name+ " with error:"+e);
        }
        return pomFileList;
    }

    void findAndAddPomFile(List<String> pomFileList, File file){
        if(!file.isDirectory()){
            if(file.getName().equalsIgnoreCase("pom.xml")){
                System.out.println("rec-file found-->" + file.getName());
                pomFileList.add(file.getPath());
            } else{
                return;
            }
        } else{
            if(file.listFiles()==null) return;
            for( File newfile : file.listFiles()){
                findAndAddPomFile(pomFileList, newfile);
            }
        }
    }

    public Repository[] getAllRepositories() {
        Repository[] repositories = new Repository[]{};
        try {
            repositories = restTemplate.getForObject(env.getProperty("apiurl"), Repository[].class);
        } catch (Exception e) {
            System.out.println("Error occurred while getAllRepositories call.");
        }
        return repositories;
    }

}
