package com.exp.service;

import com.exp.entity.Exp2Data;
import com.exp.exp2.DataMiningExp2;

import java.util.Map;

public class DataMiningExp2Service {
    public Exp2Data getExp2Data(String path, Map<String, Double> info) {
        System.out.println("DataMiningExp2.getExp2Data: info " + info);
        DataMiningExp2 dataMiningExp2 = new DataMiningExp2();
        return dataMiningExp2.AprioriAlgm(path, info);
    }
}
