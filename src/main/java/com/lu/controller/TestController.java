package com.lu.controller;

import com.lu.pojo.Species;
import com.lu.service.SpeciesNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {
    @Autowired
    @Qualifier("SpeciesNetworkServiceImpl")
    private SpeciesNetworkService speciesNetworkService;

    @RequestMapping("/t1")
    public String t1(Model model) {
        List<Species> allSpecies = speciesNetworkService.getAllSpecies();
        Map<String, Species> map = new HashMap<>();
        for (Species species : allSpecies) {
            map.put(species.getSpeciesId(), species);
        }
        model.addAllAttributes(map);
        return "test1";
    }
}
