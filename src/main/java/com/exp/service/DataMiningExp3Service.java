package com.exp.service;


import com.exp.entity.Exp3Data;
import com.exp.exp3.DataMiningExp3;

import java.util.ArrayList;
import java.util.Map;

public class DataMiningExp3Service {
    public Exp3Data getExp3Data(String path, Map<Integer, ArrayList<Double>> centers, double delta) {
        System.out.println("DataMiningExp3.getExp3Data: centers " + centers);
        DataMiningExp3 dataMiningExp3 = new DataMiningExp3();
        return dataMiningExp3.kMeansAlgo(path, centers, delta);
    }
}
