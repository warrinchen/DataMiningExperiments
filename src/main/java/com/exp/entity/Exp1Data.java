package com.exp.entity;

import java.util.List;
import java.util.Map;

public class Exp1Data {
    // 饼图
    private String pieTitle;     // 标题
    private String subtitle;  // 子标题
    private String attrname;      // 属性名-什么属性下的分类
    private List<ClassData> piedata;
    // 雷达图
    private String radarTitle;
    private List<String> legendDataList; //图例
    private List<Map<String, String>> indicatorList; //指定属性和最值
    private String seriesDesc;       //对比数据描述
    private Map<String, List<Double>> dataMap; //各类别下的属性概率
    private double goodP;
    private double badP;

    public String getPieTitle() {
        return pieTitle;
    }

    public void setPieTitle(String pieTitle) {
        this.pieTitle = pieTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public List<ClassData> getPiedata() {
        return piedata;
    }

    public void setPiedata(List<ClassData> piedata) {
        this.piedata = piedata;
    }

    public String getRadarTitle() {
        return radarTitle;
    }

    public void setRadarTitle(String radarTitle) {
        this.radarTitle = radarTitle;
    }

    public List<String> getLegendDataList() {
        return legendDataList;
    }

    public void setLegendDataList(List<String> legendDataList) {
        this.legendDataList = legendDataList;
    }

    public List<Map<String, String>> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Map<String, String>> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public Map<String, List<Double>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<Double>> dataMap) {
        this.dataMap = dataMap;
    }

    public double getGoodP() {
        return goodP;
    }

    public void setGoodP(double goodP) {
        this.goodP = goodP;
    }

    public double getBadP() {
        return badP;
    }

    public void setBadP(double badP) {
        this.badP = badP;
    }
}

