package com.exp.service;

import com.exp.entity.Exp1Data;
import com.exp.exp1.DataMiningExp1;

import java.util.ArrayList;

public class DataMiningExp1Service {
    public Exp1Data getExp1Data(String path, ArrayList testData) {
        DataMiningExp1 exp1 = new DataMiningExp1();
        return exp1.analysisExp1(path, testData);
    }
}
