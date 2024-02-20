package com.parser.parser.service;

import com.parser.parser.client.GitRepositoryService;
import com.parser.parser.models.Repository;
import com.parser.parser.service.utils.PraserFileUtils;
import org.apache.maven.model.Dependency;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ParserService{

    @Autowired
    private GitRepositoryService gitRepositoryService;

    public void parse(){
        //Fetch all repositories
        Repository[] repositories = gitRepositoryService.getAllRepositories();
        if(repositories!=null){
           System.out.println("Following repositories fetched:");
           for(Repository repository : repositories){
               System.out.println(repository.getName());
           }
           //find all poms for repositories
            for(Repository repository : repositories){
                System.out.println("Finding pom files for repository " + repository.getName());
                List<String> pomFilePathList = gitRepositoryService.getRepositoryFiles(repository.getCloneUrl(), repository.getName());
                if(!pomFilePathList.isEmpty()){
                    for(String pomFilePath : pomFilePathList){
                        System.out.println("Finding dependencies for pom file:"+pomFilePath);
                        List<Dependency> dependencyList = PraserFileUtils.extractDependencies(pomFilePath);
                        if(!dependencyList.isEmpty()){
                            printDependencyDetails(dependencyList);
                        } else{
                            System.out.println("No dependencies found for pom file:"+pomFilePath);
                        }
                    }
                } else{
                    System.out.println("No pom file found for repository:"+repository.getName());
                }
            }
        }
        else{
            System.out.println("No repositories found!");
        }
    }

    private void printDependencyDetails(List<Dependency> dependencyList){
        for(Dependency dependency : dependencyList){
            System.out.println(dependency.getGroupId()+": Version "+dependency.getVersion());
        }

    }
}