package com.exp.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exp2Data {
    // TODO exp2 data entity 构建
    //频繁项集 tree结构
    private Map tree;
    private int k;
    // 每层频繁项集集合
    private Map<String, String> freqListMap;
    // 强关联规则集合
    private ArrayList<String> strongRuleList;

    public Exp2Data() {
        k = 0;
        tree = new HashMap();
        freqListMap = new HashMap<>();
        strongRuleList = new ArrayList<>();
    }

    public Map getTree() {
        return tree;
    }

    public void setTree(Map tree) {
        this.tree = tree;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Map<String, String> getFreqListMap() {
        return freqListMap;
    }

    public void setFreqListMap(Map<String, String> freqListMap) {
        this.freqListMap = freqListMap;
    }

    public ArrayList<String> getStrongRuleList() {
        return strongRuleList;
    }

    public void setStrongRuleList(ArrayList<String> strongRuleList) {
        this.strongRuleList = strongRuleList;
    }
}

