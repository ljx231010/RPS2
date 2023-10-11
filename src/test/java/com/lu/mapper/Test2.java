package com.lu.mapper;

import com.lu.pojo.SpeciesReaction;
import com.lu.service.EnzymeService;
import com.lu.service.SpeciesNetworkService;
import com.lu.service.YenKService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;

public class Test2 {
    @Test
    public void t1() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpeciesNetworkService speciesNetworkServiceImpl = (SpeciesNetworkService) context.getBean("SpeciesNetworkServiceImpl");
        speciesNetworkServiceImpl.calculateScoreOfSpecies("C00022->R00006->C00900->R03051->C04039->R01209->C00141->R01434->C00183", 0.0, 0.0, 0.0);
    }

    @Test
    public void t2() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EnzymeService enzymeServiceImpl = (EnzymeService) context.getBean("EnzymeServiceImpl");
        enzymeServiceImpl.calculateBothOneReaction("R01434", "syz", 0, 0);
    }


    @Test
    //测试yenK的生成路径转成表达的线性路径
    public void t4() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        YenKService yenKServiceImpl = (YenKService) context.getBean("YenKServiceImpl");
        yenKServiceImpl.yunK("C00022", "C00183", 5);
    }

    @Test
    public void testMadeUrl(){
        System.out.println(1);
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EnzymeService enzymeServiceImpl = (EnzymeService) context.getBean("EnzymeServiceImpl");
        enzymeServiceImpl.recomSpecificInfoOfEnzyme("4.2.3.24","sce");
    }
    @Test
    //
    public void t5() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EnzymeService enzymeServiceImpl = (EnzymeService) context.getBean("EnzymeServiceImpl");
        String path = "C00024->R01978->C00356->R02082->C00418->R02245->C01107->R03245->C01143->R01121->C00129->R02003->C00448->R07630->C16028->R10026->C20309";
        String speciesId = "sce";
        List<SpeciesReaction> speciesReactions = enzymeServiceImpl.allReaction(path, speciesId);
        for (SpeciesReaction speciesReaction : speciesReactions) {
            System.out.println(speciesReaction);
        }
    }
}
