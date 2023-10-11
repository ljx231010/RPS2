package com.lu.service;

import com.lu.mapper.CompoundMapper;
import com.lu.pojo.GraphEntity.MyGraph;
import com.lu.pojo.GraphEntity.ShortestPath;
import com.lu.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YenKServiceImpl implements YenKService {

    @Autowired
    private CompoundMapper compoundMapper;

    public void setCompoundMapper(CompoundMapper compoundMapper) {
        this.compoundMapper = compoundMapper;
    }

    @Override
    public List<String> yunK(String startCompound, String endCompound, int k) {
        String compoundSrc = "d:/data/compound_list.txt";
        String s = FileUtils.readJsonFile(compoundSrc);
        assert s != null;
        String[] compounds = s.split(",");
        List<String> compoundList = Arrays.asList(compounds);

        int[][] network = createNetwork();
        int n = compoundList.size();
        int startIndex = compoundList.indexOf(startCompound);
        int endIndex = compoundList.indexOf(endCompound);

        MyGraph g = new MyGraph(n, network.length);
        g.createMyGraph(g, n, network.length, network);

        ShortestPath ksp = new ShortestPath();
        List<ShortestPath.MyPath> myPaths = ksp.KSP_Yen(g, startIndex, endIndex, k);
        List<String> stringsPathList = myPathToString(myPaths, compoundList);
        return stringsPathList;
    }

    @Override
    public int[][] createNetwork() {
        String networkSrc = "d:/data/universal_network.txt";

        String content = FileUtils.readJsonFile(networkSrc);
        assert content != null;
        String[] lines = content.split("\n");
        int[][] data = new int[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].replace("\r","");
            String[] split = line.split("\t")[1].split(",");
            data[i] = new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])};

        }
        return data;
    }

    public List<String> myPathToString(List<ShortestPath.MyPath> myPaths, List<String> compoundList) {
        List<String> resultPaths = new ArrayList<>();
        List<String> ridsList = new ArrayList<>();
        for (ShortestPath.MyPath myPath : myPaths) {
            List<String> paths = new ArrayList<>();
            for (int i = 0; i < myPath.path.size() - 1; i++) {
                int startIndex = myPath.path.get(i);
                int endIndex = myPath.path.get(i + 1);
                String ridsStr = compoundMapper.getRIdsByCIndex(startIndex + "," + endIndex);
                String c1 = compoundList.get(startIndex);
                String c2 = compoundList.get(endIndex);

                String[] rids = ridsStr.split(",");
                if (paths.size() == 0) {
                    for (String rid : rids) {
                        String s = c1 + "->" + rid + "->" + c2;
                        paths.add(s);
                    }
                } else {
                    List<String> newPaths = new ArrayList<>();
                    for (String path : paths) {
                        for (String rid : rids) {
                            String s = path + "->" + rid + "->" + c2;
                            newPaths.add(s);
                        }
                    }
                    paths = newPaths;
                }
            }

            resultPaths.addAll(paths);
        }


        return resultPaths;
    }
}
