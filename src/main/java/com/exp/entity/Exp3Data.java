package com.exp.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.support.spring.annotation.FastJsonFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exp3Data {
    // TODO 定义要传送给前端的数据
    ArrayList<String> jcx;
    ArrayList<Double> jcy;
//    @JSONField(serialize = false)
    Map<String, ArrayList<ArrayList<Double>>> iterInfo;

    public Exp3Data() {
        jcx = new ArrayList<>();
        jcy = new ArrayList<>();
        iterInfo = new HashMap<>();
    }

    public ArrayList<String> getJcx() {
        return jcx;
    }

    public void setJcx(ArrayList<String> jcx) {
        this.jcx = jcx;
    }

    public ArrayList<Double> getJcy() {
        return jcy;
    }

    public void setJcy(ArrayList<Double> jcy) {
        this.jcy = jcy;
    }

    public Map<String, ArrayList<ArrayList<Double>>> getIterInfo() {
        return iterInfo;
    }

    public void setIterInfo(Map<String, ArrayList<ArrayList<Double>>> iterInfo) {
        this.iterInfo = iterInfo;
    }
}
