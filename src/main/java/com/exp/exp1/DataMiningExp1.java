package com.exp.exp1;

import com.exp.entity.ClassData;
import com.exp.entity.Exp1Data;
import com.exp.util.DataReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能: 数据文件标准化
 */
public class DataMiningExp1 {
    private DataReader dr;

    public DataMiningExp1() {
        dr = new DataReader();
    }

    public DataMiningExp1(DataReader dr) {
        this.dr = dr;
    }

    public Exp1Data analysisExp1(String TrainPath, ArrayList<String> testData) {
        // pie chart
        Exp1Data exp1Data = new Exp1Data();
        exp1Data.setPieTitle("类别描述-饼图");
        exp1Data.setSubtitle("各个类别数目");
        exp1Data.setAttrname("瓜的质量");
        List<ClassData> piedata = new ArrayList<>();
        //radar chart
        exp1Data.setRadarTitle("各个属性在各个分类下的概率");
        List<String> legendList = new ArrayList<>();
        legendList.add("前提是好瓜");
        legendList.add("前提是坏瓜");
        exp1Data.setLegendDataList(legendList);
        // 顺序要和dMap一一对应
        List<Map<String, String>> indicatorList = new ArrayList<>();
        exp1Data.setSeriesDesc("好瓜 vs 坏瓜");
        Map<String, List<Double>> dMap = new HashMap<>();
        //分类
        Map<String, ArrayList<ArrayList<String>>> dataMap = dataClassifiedForExp1(TrainPath);

        //计算每个类的概率P(Ci)
        ArrayList<ArrayList<String>> tdata = dataMap.get("是");
        piedata.add(new ClassData(tdata.size(), "好瓜数目"));
        ArrayList<ArrayList<String>> fdata = dataMap.get("否");
        piedata.add(new ClassData(fdata.size(), "坏瓜数目"));
        exp1Data.setPiedata(piedata);
        double Pc1 = (double) tdata.size() / (tdata.size() + fdata.size());
        double Pc2 = (double) fdata.size() / (tdata.size() + fdata.size());
        //计算每个类下各个属性的概率P(X | Ci)
        double PXc1 = 1d;
        double PXc2 = 1d;
        List<Double> p1list = new ArrayList<>();
        List<Double> p2list = new ArrayList<>();
        for (String item : testData) {
            double p1 = calcAttr(tdata, item, testData.indexOf(item));
            double p2 = calcAttr(fdata, item, testData.indexOf(item));
            PXc1 *= p1;
            PXc2 *= p2;
            Map<String, String> indicatorItem = new HashMap<>();
            indicatorItem.put("name", item);
            indicatorItem.put("max", "1");
            indicatorList.add(indicatorItem);
            p1list.add(p1);
            p2list.add(p2);
        }
        exp1Data.setIndicatorList(indicatorList);
        dMap.put("good_melon", p1list);
        dMap.put("bad_melon", p2list);
        exp1Data.setDataMap(dMap);
        exp1Data.setGoodP(Pc1 * PXc1);
        exp1Data.setBadP(Pc2 * PXc2);

        if ((Pc1 * PXc1 - Pc2 * PXc2) > 0) {
            System.out.println("是好瓜");
        } else {
            System.out.println("否好瓜");
        }
        System.out.println("P(条件|是) * P(是):"+Pc1 * PXc1 +"\tP(条件|否) * P(否):"+ Pc2 * PXc2);

        return exp1Data;
    }

    /**
     * 对实验1数据进行分类
     * @param path 文件路径
     * @return 分类的数据
     */
    public Map<String, ArrayList<ArrayList<String>>> dataClassifiedForExp1(String path) {
        List<ArrayList<String>> dataSet = dr.readTxt1(path);
        Map<String, ArrayList<ArrayList<String>>> dataMap = new HashMap<>();
        ArrayList<ArrayList<String>> tdata = new ArrayList<>();
        ArrayList<ArrayList<String>> fdata = new ArrayList<>();
        for (ArrayList<String> elem : dataSet) {
            String type = elem.get(elem.size() - 1); // 类别是最后一列属性
            if (type.equals("是")) {
                tdata.add(elem);
            } else {
                fdata.add(elem);
            }
        }
        dataMap.put("是", tdata);
        dataMap.put("否", fdata);
        return dataMap;
    }// end 数据分类

    /**
     * 计算指定类下指定属性的概率P(Xi | Ci)
     * @param data 指定类的数据
     * @param attr 指定属性
     * @param attrIdx 指定属性的下标
     * @return 概率
     */
    public double calcAttr(List<ArrayList<String>> data, String attr, int attrIdx) {
        int cnt = 0;
        for (ArrayList<String> elem : data) {
            if (elem.get(attrIdx).equals(attr)) {
                cnt++;
            }
        }
        return (double) cnt/data.size();
    }

}
