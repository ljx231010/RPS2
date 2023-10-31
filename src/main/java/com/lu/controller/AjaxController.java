package com.lu.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.javabean.ExamplePath;
import com.lu.service.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AjaxController {
    @Autowired
    @Qualifier("PathServiceImpl")
    private PathService pathService;

    @RequestMapping("/examplePath")
    public List<ExamplePath> getExamplePath(Model model) throws JsonProcessingException {
        System.out.println("getExamplePath");
        ObjectMapper mapper = new ObjectMapper();
        List<ExamplePath> pathList = new ArrayList<>();
        pathList.add(new ExamplePath("C00022->R00006->C00900->R03051->C04039->R01209->C00141->R01434->C00183", "a,b,c", "page1"));
        pathList.add(new ExamplePath(" C00197->R01513->C03232->R04173->C01005->R00582->C00065", "a,b,m", "page2"));
        pathList.add(new ExamplePath("C00059->R00529->C00224->R00509->C00053->R02021->C00094->R00859->C00283", "a,y,u,o", "page3"));
        mapper.writeValueAsString(pathList);
        return pathList;
    }

    @RequestMapping("/compoundIdToName")
    public String compoundIdToName(String path) throws JsonProcessingException {
        System.out.println("@RequestMapping(/compoundIdToName)");
        System.out.println(path);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> cmap = pathService.getCompoundIdToName(path);
        String s = mapper.writeValueAsString(cmap);
        System.out.println(s);
        return s;
    }

    @RequestMapping("/reactionIdToName")
    public String reactionIdToName(String path) throws JsonProcessingException {
        System.out.println("@RequestMapping(/reactionIdToName)");
        System.out.println(path);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> rmap = pathService.getReactionIdToName(path);
        String s = mapper.writeValueAsString(rmap);
//        System.out.println(s);
        return s;
    }
}
