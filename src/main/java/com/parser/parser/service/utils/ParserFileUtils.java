package com.parser.parser.service.utils;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.FileUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserFileUtils{

    public static List<Dependency> extractDependencies(String pomFilePath) {
        List<Dependency> dependencyList = new ArrayList<>();
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(pomFilePath));
            dependencyList.addAll(model.getDependencies());
        } catch (Exception e) {
            System.out.println("Exception occurred while extractDependencies for pom : " + pomFilePath + " with error:" + e);
        }
        return dependencyList;
    }

    public static void clearFiles(String localPath) {
        try {
            FileUtils.deleteDirectory(localPath);
        } catch (IOException e) {
            System.out.println("Exception occurred while clearFiles for path :" + localPath + " with error : " + e);
        }
        System.out.println("Cleared all the used files!");
    }
}