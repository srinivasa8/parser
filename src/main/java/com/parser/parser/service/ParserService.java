package com.parser.parser.service;

import com.parser.parser.client.GitRepositoryService;
import com.parser.parser.models.Repository;
import com.parser.parser.service.utils.ParserFileUtils;
import org.apache.maven.model.Dependency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ParserService {
    @Value("${localpath}")
    private String localPath;

    @Autowired
    private GitRepositoryService gitRepositoryService;

    public ParserService(GitRepositoryService gitRepositoryService) {
        this.gitRepositoryService = gitRepositoryService;
    }

    public ParserService(GitRepositoryService gitRepositoryService, String localPath) {
        this.gitRepositoryService = gitRepositoryService;
        this.localPath = localPath;
    }

    public ParserService() {

    }

    public void parse() {
        //Fetch all repositories
        Repository[] repositories = gitRepositoryService.getAllRepositories();
        if (repositories != null) {

            System.out.println("List of repositories found:");
            System.out.println();

            for (Repository repository : repositories) {
                System.out.println(repository.getName());
            }
            System.out.println();

            //Filtering just to print one repository:
            Optional<Repository> repositoryOptional = Arrays.stream(repositories).filter(f -> f.getName().equalsIgnoreCase("test-repo"))
                    .findFirst();

            if (repositoryOptional.isPresent()) {

                Repository repository = repositoryOptional.get();

                //find all poms for the repository
                System.out.println("Finding pom files for repository : " + repository.getName());
                System.out.println();

                List<String> pomFilePathList = gitRepositoryService.getRepositoryFiles(repository.getCloneUrl(), repository.getName());

                if (!pomFilePathList.isEmpty()) {
                    for (String pomFilePath : pomFilePathList) {

                        System.out.println("Finding dependencies for pom file: " + pomFilePath);
                        System.out.println();

                        List<Dependency> dependencyList = ParserFileUtils.extractDependencies(pomFilePath);

                        if (!dependencyList.isEmpty()) {
                            printDependencyDetails(dependencyList);
                        } else {
                            System.out.println("No dependencies found for pom file:" + pomFilePath);
                        }
                        System.out.println();
                    }

                } else {
                    System.out.println("No pom file found for repository:" + repository.getName());
                    System.out.println();
                }
                clearAllUsedFiles();
            }
        } else {
            System.out.println("No repositories found!");
        }
    }

    private void printDependencyDetails(List<Dependency> dependencyList) {
        for (Dependency dependency : dependencyList) {
            System.out.println(dependency.getGroupId() + ": Version " + dependency.getVersion());
        }
    }

    void clearAllUsedFiles() {
        ParserFileUtils.clearFiles(localPath);
    }
}