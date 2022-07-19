package com.exp.exp3;

import com.exp.entity.Exp3Data;
import com.exp.util.DataReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataMiningExp3 {
    Exp3Data exp3Data;

    public DataMiningExp3() {
        exp3Data = new Exp3Data();
    }

    /**
     * 实验主控函数, 负责读取数据文件并完成聚类, 返回展示数据
     * @param path 文件路径
     * @param centers 初始中心点
     * @param delta Jc函数值的差值必须要不大于delta
     * @return 实验三展示数据
     */
    public Exp3Data kMeansAlgo(String path, Map<Integer, ArrayList<Double>> centers, double delta) {
        DataReader dataReader = new DataReader();                   // 数据读取类
        Map<Integer, ArrayList<Double>> centerPre = null;           // k 个中心点-初始中心,前中心
        Map<Integer, ArrayList<Double>> centerNew = null;           // k 个中心点-后中心
        Map<Integer, ArrayList<ArrayList<Double>>> clusters = null; // k 个簇

        centerPre = centers;

        // 1. 从数据文件获取数据
        clusters = dataReader.readTxt3(path);

        double JcPre = 0d;
        double JcNew = 0d;
        for (int i = 0; ; i++) {
            // 2.1 求每个样本数据到每个中心点I的距离 D, 并把样本数据归类到距离最小的类下
            clusters = classify(clusters, centerPre);
            System.out.println("第" + (i + 1) + "次聚类结果:");
            for (int j = 0; j < clusters.size(); j++) {
                System.out.println("第 " + j + " 类, 有" + clusters.get(j).size() + "个元素");
//                System.out.println(clusters.get(j));
            }

            // TODO 2.3 计算新中心I+1 和 误差平方和准则函数Jc的值
            centerNew = getNewCenter(clusters);
            JcNew = getJc(clusters, centerNew);

            // TODO 2.4 如果 | Jc(I+1) - Jc(I) | < delta, 结束, 否则 继续步骤2
            if (i > 0 && Math.abs(JcPre - JcNew) < delta) {
                System.out.println("第" + (i + 1) + "次聚类, 误差 |" + JcPre + " - " + JcNew +
                        "| = " + Math.abs(JcPre - JcNew) + ", 小于0.001, 聚类结束");
                exp3Data.getJcx().add(String.valueOf(i+1));
                exp3Data.getJcy().add(Math.abs(JcPre - JcNew));
                break;
            } else {
                if (i > 0) {
                    System.out.println("第" + (i + 1) + "次聚类, 误差 |" + JcPre + " - " + JcNew +"| = " + Math.abs(JcPre - JcNew));
                    exp3Data.getJcx().add(String.valueOf(i+1));
                    exp3Data.getJcy().add(Math.abs(JcPre - JcNew));
                }
                centerPre = centerNew;
                JcPre = JcNew;
            }
        }
        exp3Data.getJcx().add("次数");

        for (int i = 0; i < centerNew.size(); i++) {
            System.out.println("第 " + i + " 个中心 " + centerNew.get(i));
        }

        return exp3Data;
    }

    /**
     * 按照center, 计算每个cluster里元素到各个中心的距离, 并重新分类到距离更近的新簇中,
     * 初始cluster只有 0
     * @param clusters k个簇 对于原始簇, 数据都属于编号为"0"的簇
     * @param centers k个中心点
     * @return 重新分类后新的簇
     */
    private Map<Integer, ArrayList<ArrayList<Double>>> classify(Map<Integer, ArrayList<ArrayList<Double>>> clusters,
                          Map<Integer, ArrayList<Double>> centers) {
        ArrayList<Double> distList = new ArrayList<>(); // 暂存每个点到各个聚类中心的距离
        double curDist = (double)0x7fffffff;            // 点到中心的最小距离
        int curCenter = 0;                              // 每个点的当前最小距离对应的类中心编号
        ArrayList<ArrayList<Double>> cgdItems = new ArrayList<>();

        if (clusters.size() == 1) {
            for (int i = 1; i < centers.size(); i++) {
                clusters.put(i, new ArrayList<ArrayList<Double>>());
            }
        }
        for (int i = 0; i < clusters.size(); i++) { // 遍历每个簇
            ArrayList<ArrayList<Double>> cluster = clusters.get(i);
            for (ArrayList<Double> item : cluster) { // 遍历簇元素, 计算距离, 重新分类
                curDist = (double)0x7fffffff;
                 // 计算点到每个中心的聚类, 并保存距离最小的点的编号
                for (int j = 0; j < centers.size(); j++) { // 遍历中心点
                    ArrayList<Double> center = centers.get(j);
                    double dist = 0d;
                    for (int m = 0; m < item.size(); m++) { // 计算点到中心的距离
                        dist += Math.pow(item.get(m) - center.get(m), 2);
                    }// end for 计算点到中心的距离
//                    System.out.println("DataMiningExp3.classify 计算距离 " + item + " 到 " + center + " 的距离^2 为 " + dist);
                    if (dist < curDist) {
                        curDist = dist;
                        curCenter = j;
//                        System.out.println("DataMiningExp3.classify 最小距离更新 " + item + " 由到中心 " + i + "变为到中心 " + j);
                    }
                }// end for 计算点到各个中心的距离并修改所属分类

                if (curCenter != i) { // 如果分类发生变化
                    cgdItems.add(item);
                    clusters.get(curCenter).add(item);
//                    System.out.println("DataMiningExp3.classify " + item + "的分类变化 由 " + i + " 变为 " + curCenter);
                }// end if 调整样本的分类

            } // end for 重新分类

            // 移除改变了的元素
            for (ArrayList<Double> item : cgdItems) {
                cluster.remove(item);
            }
//            System.out.println("DataMiningExp3.classify: 改变了类的元素:\n" + cgdItems);
            cgdItems.removeAll(cgdItems);

        }// end for 遍历每个簇

        return clusters;
    }

    /**
     * 依据簇计算新的中心
     * @param clusters 簇
     * @return 新的中心点
     */
    private Map<Integer, ArrayList<Double>> getNewCenter(Map<Integer, ArrayList<ArrayList<Double>>> clusters) {
        Map<Integer, ArrayList<Double>> centersNew = new HashMap<>();
        int iterCnt = exp3Data.getIterInfo().size() + 1;
        exp3Data.getIterInfo().put(String.valueOf(iterCnt), new ArrayList<>());

        // 计算每个簇的新中心点
        for (int i = 0; i < clusters.size(); i++) { // 遍历每个簇
            ArrayList<ArrayList<Double>> cluster = clusters.get(i);
            int cnt = cluster.size(); // 簇中元素的个数
            ArrayList<Double> center = new ArrayList<>();

            // 初始化中心
            for (int j = 0; j < clusters.size(); j++) {
                center.add(0d);
            }
            for (ArrayList<Double> item : cluster) {// 遍历簇中元素, 计算新中心
                double d = 0;
                for (int j = 0; j < item.size(); j++) {// 遍历簇中点
                    d = center.get(j);
                    center.remove(j);
                    center.add(j, d + item.get(j) / cnt);
                }//end for 遍历簇中点

            }// end for 遍历簇中元素, 计算新中心

            System.out.println("DataMiningExp3.getNewCenter 更新第" + i + "个中心为 " + center);
            exp3Data.getIterInfo().get(String.valueOf(iterCnt)).add(center);
            centersNew.put(i, center);

        }// end for 遍历每个簇, 计算每个簇的新中心
        return centersNew;
    }

    /**
     * 计算中心点的误差平方和准则函数值
     * @param clusters 簇
     * @param centers 中心
     * @return Jc值
     */
    private double getJc(Map<Integer, ArrayList<ArrayList<Double>>> clusters,
                         Map<Integer, ArrayList<Double>> centers) {
        double Jc = 0d;

        for (int i = 0; i < clusters.size(); i++) {// 遍历簇
            ArrayList<Double> center = centers.get(i);

            for (ArrayList<Double> item : clusters.get(i)) {// 遍历簇中元素
                for (int j = 0; j < item.size(); j++) {
                    Jc += Math.pow(item.get(j) - center.get(j), 2);
                }//end for 累加计算每个样本的误差

            }// end for 遍历簇中元素

        }// end for 遍历簇集合

        return Jc;
    }

}
