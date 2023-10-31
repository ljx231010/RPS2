package com.lu.controller;

import com.lu.pojo.KeyValue;
import com.lu.pojo.Path;
import com.lu.pojo.Species;
import com.lu.service.MREService;
import com.lu.service.PathService;
import com.lu.service.SpeciesNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PathController {
    @Autowired
    @Qualifier("PathServiceImpl")
    private PathService pathService;
    @Autowired
    @Qualifier("SpeciesNetworkServiceImpl")
    private SpeciesNetworkService speciesNetworkService;
    @Autowired
    @Qualifier("MREServiceImpl")
    private MREService mreService;

    @RequestMapping("/path")
    public String createCompletePath(Model model, String path) {
        System.out.println(path);
        Path completePath = pathService.createCompletePath(path);
        System.out.println(completePath);
        model.addAttribute("path", completePath);
        return "hello";
    }

    @RequestMapping(value = "/pathMain")
    public String pathMain(Model model, String path, Double inOut, Double subAndProduct, Double compete) throws IOException {
        System.out.println("pathcontroller + pathMain");
        System.out.println(path);
        model.addAttribute("path", path);
        path = path.replace(" ", "");
        System.out.println(inOut);
        System.out.println(subAndProduct);
        System.out.println(compete);
        List<KeyValue> keyValues = speciesNetworkService.calculateScoreOfSpecies(path, inOut, subAndProduct, compete);
        String message = "There is a problem between the compound and the reaction,please return for inspection.";
        if (keyValues == null)
            model.addAttribute("message", message);
        model.addAttribute("keyValues", keyValues);

        List<Species> allSpecies = speciesNetworkService.getAllSpecies();
        Map<String, Species> map = new HashMap<>();
        for (Species species : allSpecies) {
            map.put(species.getSpeciesId(), species);
        }
        model.addAllAttributes(map);

        return "species_show";
    }
}
