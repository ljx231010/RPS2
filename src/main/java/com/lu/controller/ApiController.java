package com.lu.controller;

import com.lu.pojo.SpeciesNetwork1;
import com.lu.service.SpeciesNetworkService;
import com.lu.service.YenKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ApiController {
    @Autowired
    @Qualifier("YenKServiceImpl")
    private YenKService yenKService;
    @Autowired
    @Qualifier("speciesNetworkServiceImpl")
    private SpeciesNetworkService speciesNetworkService;

    @RequestMapping("/YenK")
    public String YenKApi(Model model, String startCompound, String endCompound, Integer k) {
        List<String> stringsPathList = yenKService.yunK(startCompound, endCompound, k);
        model.addAttribute("results", stringsPathList);
        return "linePath_result";
    }
}
