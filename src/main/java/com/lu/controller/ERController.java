package com.lu.controller;

import com.lu.pojo.*;
import com.lu.service.EnzymeService;
import com.lu.service.PathService;
import com.lu.service.SpeciesNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ERController {

    @Autowired
    @Qualifier("EnzymeServiceImpl")
    private EnzymeService enzymeService;
    @Autowired
    @Qualifier("PathServiceImpl")
    private PathService pathService;

    @Autowired
    @Qualifier("SpeciesNetworkServiceImpl")
    private SpeciesNetworkService speciesNetworkService;

    @RequestMapping("/reaction/{speciesId}/{path}")
    public String reaction(Model model, @PathVariable("speciesId") String speciesId, @PathVariable("path") String path) throws IOException {
        System.out.println("ERController + reaction");
        System.out.println(speciesId);
        System.out.println(path);

        model.addAttribute("path", path);
        Species curSpecies = enzymeService.getSpeciesById(speciesId);
        model.addAttribute("curSpecies", curSpecies);
        List<SpeciesReaction> speciesReactions = enzymeService.allReaction(path, speciesId);
        model.addAttribute("speciesReactions", speciesReactions);

        List<Message> deadEndMetabolites = speciesNetworkService.getDeadEndMetaboliteInOneSpecies(path, speciesId);
        StringBuilder s = new StringBuilder();
        for (Message message : deadEndMetabolites) {
            s.append(message.toString()).append("-");
        }
        String s1 = s.length()>0?s.substring(0, s.length() - 1):s.toString();
        model.addAttribute("deadEndMessage", s1);
        return "reaction_show";
    }

    @RequestMapping("/foreignEnzymeRecommend")
    public String foreignEnzymeRecommend(Model model, String speciesId, String reactionId, String KMWeight, String disWeight) {
        System.out.println(speciesId);
        System.out.println(reactionId);
        System.out.println("disWeight:" + disWeight);
        System.out.println("KMWeight:" + KMWeight);
        String speciesName = speciesNetworkService.getSpeciesById(speciesId).getSpeciesName();
        model.addAttribute("speciesName", speciesName);
        List<DistanceEnzyme> distanceEnzymes = enzymeService.enzymeAboutDISOneReaction(reactionId, speciesId);
        List<DistanceEnzyme> newdistanceEnzymes = new ArrayList<>();
        List<Double> nums = new ArrayList<>();
        for (DistanceEnzyme distanceEnzyme : distanceEnzymes) {
            double distance = distanceEnzyme.getDistance();
            if (nums.size() == 4) {
                newdistanceEnzymes.remove(newdistanceEnzymes.size() - 1);
                break;
            }
            if (nums.contains(distance)) {
                newdistanceEnzymes.add(distanceEnzyme);
            } else {
                nums.add(distance);
                newdistanceEnzymes.add(distanceEnzyme);
            }
        }
        List<DistanceEnzyme> newDistanceEnzymes1 = newdistanceEnzymes.stream().limit(60).collect(Collectors.toList());
        enzymeService.toNamesAboutDistanceEnzyme(newDistanceEnzymes1);
        enzymeService.addUrlsAboutDistanceEnzyme(newDistanceEnzymes1);
        model.addAttribute("reactionId", reactionId);
        model.addAttribute("distanceEnzymes", newDistanceEnzymes1);

        List<DynamicsEnzyme> dynamicsEnzymes = enzymeService.enzymeAboutKmOneReaction(reactionId);
        enzymeService.addUrlsAboutDynamicsEnzyme(dynamicsEnzymes);
        model.addAttribute("dynamicsEnzymes", dynamicsEnzymes);

        List<EnzymeResult> resultList = enzymeService.calculateBothOneReaction(reactionId, speciesId, Double.parseDouble(disWeight), Double.parseDouble(KMWeight));
        List<EnzymeResult> collect = resultList.stream().limit(50).collect(Collectors.toList());
        enzymeService.toNamesAboutEnzymeResult(collect);
        enzymeService.addUrlsAboutEnzymeResult(collect);
        model.addAttribute("collect", collect);
        return "enzyme_recommend_show";
    }


}
