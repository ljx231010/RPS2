package com.lu.mapper;

import com.lu.javabean.Url;
import com.lu.pojo.EnzymeResult;
import com.lu.pojo.KeyValue;
import com.lu.pojo.Message;
import com.lu.pojo.Path;
import com.lu.service.EnzymeService;
import com.lu.service.PathService;
import com.lu.service.SpeciesNetworkService;
import com.lu.utils.FileUtils;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Test1 {

    public String[] paths = new String[]{"C00049->R00480->C03082->R02291->C00441->R01773->C00263->R01771->C01102->R01466->C00188", "C00049->R00480->C03082->R02291->C00441->R01775->C00263->R01771->C01102->R01466->C00188", "C00197->R01513->C03232->R04173->C01005->R00582->C00065", "C00114->R01025->C00576->R02565->C00719", "C00114->R08557->C00576->R02565->C00719", "C00114->R08558->C00576->R02565->C00719", "C00114->R07409->C00576->R02565->C00719", "C00114->R01022->C00576->R02565->C00719", "C00114->R01025->C00576->R02566->C00719", "C00114->R08557->C00576->R02566->C00719", "C00114->R08558->C00576->R02566->C00719", "C00114->R07409->C00576->R02566->C00719", "C00114->R01022->C00576->R02566->C00719", "C00114->R01025->C00576->R08211->C00719", "C00114->R08557->C00576->R08211->C00719", "C00114->R08558->C00576->R08211->C00719", "C00114->R07409->C00576->R08211->C00719", "C00114->R01022->C00576->R08211->C00719", "C00049->R00480->C03082->R02291->C00441->R01773->C00263->R01777->C01118->R03260->C02291->R01286->C00155->R00946->C00073", "C00049->R00480->C03082->R02291->C00441->R01775->C00263->R01777->C01118->R03260->C02291->R01286->C00155->R00946->C00073", "C00049->R00480->C03082->R02291->C00441->R01773->C00263->R01777->C01118->R03260->C02291->R01286->C00155->R04405->C00073", "C00049->R00480->C03082->R02291->C00441->R01775->C00263->R01777->C01118->R03260->C02291->R01286->C00155->R04405->C00073", "C00065->R00586->C00979->R00897->C00097", "C00283->R00897->C00097", "C00073->R00177->C00019->R10404->C00021->R00194->C03539->R01291->C00155->R10305->C02291->R01001->C00097", "C00109->R08648->C06006->R05069->C14463->R05068->C06007->R05070->C00671->R02199->C00407", "C00188->R00996->C00109->R08648->C06006->R05069->C14463->R05068->C06007->R05070->C00671->R02199->C00407", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04198->C03972->R04364->C05539->R04467->C04390->R02733->C00666->R02735->C00680->R00451->C00047", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04199->C03972->R04364->C05539->R04467->C04390->R02733->C00666->R02735->C00680->R00451->C00047", "C00025->R00259->C00624->R02649->C04133->R03443->C01250->R02283->C00437->R00669->C00077", "C00025->R00259->C00624->R02649->C04133->R03443->C01250->R02283->C00437->R02282->C00077", "C00169->R01398->C00327->R01954->C03406->R01086->C00122", "C00077->R01398->C00327->R01954->C03406->R01086->C00122", "C00049->R01954->C03406->R01086->C00122", "C00025->R00239->C03287->R03313->C01165->R03314->C03912->R01251->C00148", "C00062->R00566->C00179->R01157->C00134->R01920->C00315", "C00019->R00178->C01137->R01920->C00315", "C00119->R01071->C02739->R04035->C02741->R04037->C04896->R04640->C04916->R04558->C04666->R03457->C01267->R03243->C01100->R03013->C00860->R03012->C01929->R01163->C00135", "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02285->C00025", "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02287->C00025", "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02286->C01045->R00525->C00025", "C00074->R01826->C04691->R03083->C00944->R03084->C02637->R02413->C00493->R02412->C03175->R03460->C01269->R01714->C00251", "C00279->R01826->C04691->R03083->C00944->R03084->C02637->R02413->C00493->R02412->C03175->R03460->C01269->R01714->C00251", "C00024->R00230->C00227->R00315->C00033", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04198->C03972->R07613->C00666->R02735->C00680->R00451->C00047", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04199->C03972->R07613->C00666->R02735->C00680->R00451->C00047", "C00251->R00985->C00108->R01073->C04302->R03509->C01302->R03508->C03506->R02722->C00078", "C00251->R00986->C00108->R01073->C04302->R03509->C01302->R03508->C03506->R02722->C00078", "C00025->R00261->C00334->R01648->C00232->R00713->C00042", "C00025->R00261->C00334->R10178->C00232->R00713->C00042", "C00025->R00261->C00334->R01648->C00232->R00714->C00042", "C00025->R00261->C00334->R10178->C00232->R00714->C00042", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04198->C03972->R04365->C04462->R04475->C04421->R02734->C00666->R02735->C00680->R00451->C00047", "C00049->R00480->C03082->R02291->C00441->R10147->C20258->R04199->C03972->R04365->C04462->R04475->C04421->R02734->C00666->R02735->C00680->R00451->C00047", "C00062->R00832->C03296->R04189->C03415->R04217->C05932->R05049->C05931->R00411->C00025", "C00251->R01715->C00254->R01373->C00166->R00694->C00079", "C00251->R01715->C00254->R01728->C01179->R00734->C00082", "C00251->R01715->C00254->R01730->C01179->R00734->C00082", "C00025->R00894->C00669->R00497->C00051", "C00134->R07414->C15699->R07415->C15700->R07417->C15767->R07419->C00334", "C00134->R07414->C15699->R07415->C15700->R07418->C15767->R07419->C00334", "C01161->R03303->C04642->R04418->C04186->R04379->C04052->R04380->C05600->R04134->C03063", "C00423->R06783->C12622->R06785->C12623->R06788->C12624->R06789->C00596->R02601->C03589->R00750->C00084->R00228->C00024", "C00423->R06781->C12621->R06787->C12623->R06788->C12624->R06789->C00596->R02601->C03589->R00750->C00084->R00228->C00024", "C07086->R02539->C00582->R09838->C20062->R09837->C19975->R09836->C19946->R09820->C19945->R09839->C14144->R06942->C14145->R06941->C02232->R00829->C00091", "C00065->R01290->C02291->R01001->C00097", "C00155->R01290->C02291->R01001->C00097", "C00073->R00177->C00019->R04858->C00021->R00192->C00155->R01290->C02291", "C00065->R01290->C02291", "C00062->R00551->C00077->R00670->C00134", "C00100->R01859->C00683->R02765->C01213->R00833->C00091", "C00014->R00149->C00169->R01398->C00327->R01954->C03406->R01086->C00062->R00551->C00086", "C00049->R01954->C03406->R01086->C00062->R00551->C00086", "C00135->R12313->C06419->R12341->C22025->R12237->C22024", "C00026->R00271->C01251->R03444->C04002->R04371->C05662->R01934->C00322->R01939->C00956->R03098->C05560->R04863->C05535->R04390->C04076->R02315->C00449->R00715->C00047", "C00024->R00271->C01251->R03444->C04002->R04371->C05662->R01934->C00322->R01939->C00956->R03098->C05560->R04863->C05535->R04390->C04076->R02315->C00449->R00715->C00047", "C00026->R00271->C01251->R03444->C04002->R04371->C05662->R01934->C00322", "C00026->R00271->C01251->R09720->C05662->R01934->C00322", "C00024->R00271->C01251->R03444->C04002->R04371->C05662->R01934->C00322", "C00024->R00271->C01251->R09720->C05662->R01934->C00322", "C00078->R00678->C02700->R01959->C00328->R01960->C03227->R02668->C00632->R02665->C04409->R04293->C03722->R03348->C01185->R03005->C00857->R00189->C00003", "C00078->R00678->C02700->R01959->C00328->R01960->C03227->R02668->C00632->R02665->C04409->R04293->C03722->R03348->C01185->R03005->C00857->R00257->C00003"};

    @Test
    public void t1() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        PathService bookService = (PathService) context.getBean("PathServiceImpl");
        Path completePath = bookService.createCompletePath("C00022->R00006->C00900->R03051->C04039->R01209->C00141->R01434->C00183");
        System.out.println(completePath);
    }

    @Test
    //路径测试
    public void t2() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpeciesNetworkService speciesNetworkService = (SpeciesNetworkService) context.getBean("SpeciesNetworkServiceImpl");
//                String path = "C00022->R00196->C00186->R01449->C00827->R02963->C00894->R03045->C05668->R03157->C01013";
//        String path = "C00074->R00199->C00022->C00024->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
//        String path = "C00074->R00199->C00022->R00209->C00024->R00351->C00158->R01325->C00417->R01900->C00311->R00709->C00026->R09784->C06547";
//        String path = "C00082->R00737->C00811->R01616->C00223->R01613->C06561->R02446->C00509";
//        String path = "C00024->R00351->C00158->R01324->C00311->R00267->C00026->R09784->C06547";
//        String path = "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02285->C00025";
//        String path = "C00024->R01978->C00356->R02082->C00418->R02245->C01107->R03245->C01143->R01121->C00129->R02003->C00448->R07630->C16028->R10026->C20309";
//        String path = "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02285->C00025";
//        String path = "C00074->R00208->C00022->R01196->C00024->R00351->C00158->R01324->C00311->R00267->C00026->R09784->C06547";
//        String path = "C00079->R00697->C00423->R02253->C00811->R01616->C00223->R01614->C03582";
//        String path = "C00022->R00224->C00084->R00754->C00469";
//        String path = "C00116->R01047->C00969->R03119->C02457";
//        String path = "C00024->R00351->C00158->R01324->C00311->R00267->C00026->R09784->C06547";
        String path="C05791->R00070->C05795";
        System.out.println(path);
        long l = System.currentTimeMillis();
        List<KeyValue> keyValues = speciesNetworkService.calculateScoreOfSpecies(path, 0.0, 0.0, 0.0);
        System.out.println(keyValues);
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void t4() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpeciesNetworkService speciesNetworkService = (SpeciesNetworkService) context.getBean("SpeciesNetworkServiceImpl");
        for (String path : paths) {
            System.out.println(path);
            List<KeyValue> keyValues = speciesNetworkService.calculateScoreOfSpecies(path, 0.0, 0.0, 0.0);
        }
    }

    @Test
    public void testOneDeadEnd() throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpeciesNetworkService speciesNetworkService = (SpeciesNetworkService) context.getBean("SpeciesNetworkServiceImpl");
        String path = "C00135->R01168->C00785->R02914->C03680->R02288->C00439->R02285->C00025";
        String speciesId = "ecf";
        List<Message> deadEndMetaboliteInOneSpecies = speciesNetworkService.getDeadEndMetaboliteInOneSpecies(path, speciesId);
    }

    @Test
    public void t3() {
        String reactionId = "R00238";
        String speciesId = "syf";
        System.out.println(reactionId);
        System.out.println(speciesId);
        double disWeight = 0.5;
        double KMWeight = 0.5;
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        EnzymeService enzymeService = (EnzymeService) context.getBean("EnzymeServiceImpl");
        List<EnzymeResult> resultList = enzymeService.calculateBothOneReaction(reactionId, speciesId, disWeight, KMWeight);
        List<EnzymeResult> collect = resultList.stream().limit(500).collect(Collectors.toList());
        enzymeService.addUrlsAboutEnzymeResult(collect);//添加网址
        System.out.println("--------***----------");
        for (EnzymeResult enzymeResult : collect) {
            System.out.println(enzymeResult);
        }

        List<String[]> enzymeList = new ArrayList<>();
        Map<Integer, String[]> map = new HashMap<>();
        for (int i = 0; i < collect.size(); i++) {
            EnzymeResult enzymeResult = collect.get(i);
//            String rank = String.valueOf(i + 1);
            String ecNUmber = enzymeResult.getEcNumber();
            String sourceOrganismId = enzymeResult.getSourceOrganismId();
            String score = String.valueOf(enzymeResult.getScore());
            StringBuilder entrys = new StringBuilder();
            for (Url url : enzymeResult.getUrls()) {
                entrys.append(url.getWebSite().split("\\/")[4]);
                entrys.append(";");
            }
            String[] e = new String[]{
                    ecNUmber, sourceOrganismId, score, entrys.toString()
            };
            map.put(i + 1, e);
        }

        System.out.println("*-*-*-*-*-**");
        int i = 1;
        for (Integer integer : map.keySet()) {
            System.out.println(i++ + ":" + Arrays.toString(map.get(integer)));
        }
    }

    @Test
    public void t5() throws IOException, InterruptedException {
        String s = FileUtils.readJsonFile("d:/r2.txt");
        String[] split = s.split(", ");
        String s2 = FileUtils.readJsonFile("d:/r2result/a.txt");
        String[] saveReactions = s2.split(",");

        for (String s1 : split) {
            //第二步
            String reactionId = s1;
            if (Arrays.asList(saveReactions).contains(reactionId)) {
                continue;
            }
            String speciesId = "ebe";
            System.out.println(reactionId);
            System.out.println(speciesId);
            double disWeight = 0.5;
            double KMWeight = 0.5;
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            EnzymeService enzymeService = (EnzymeService) context.getBean("EnzymeServiceImpl");
            List<EnzymeResult> resultList = enzymeService.calculateBothOneReaction(reactionId, speciesId, disWeight, KMWeight);
            List<EnzymeResult> collect = resultList.stream().limit(500).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (EnzymeResult enzymeResult : collect) {
                sb.append(enzymeResult.toString());
                sb.append("\n");
            }
            String saveName = "d:/r2result/a.txt";
            FileUtils.writeToTxt(saveName, "a", reactionId + ",");
            if (collect.size() != 0 && collect.get(0).getScore() > 1.3) {
                String name = "d:/r2result/" + reactionId + ".txt";
                FileUtils.writeToTxt(name, "w", sb.toString());

            }
            Thread.sleep(3 * 1000);
        }

    }
}
