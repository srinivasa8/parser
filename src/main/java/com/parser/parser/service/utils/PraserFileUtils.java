package com.parser.parser.service.utils;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PraserFileUtils{

    public static List<Dependency> extractDependencies(String pomFilePath){
        List<Dependency> dependencyList = new ArrayList<>();
        try{
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader(pomFilePath));
            dependencyList.addAll(model.getDependencies());
        }
        catch(Exception e){
            System.out.println("Exception occurred while extractDependencies for pom : "+pomFilePath+" with error:"+e);
        }
        return dependencyList;
    }
}