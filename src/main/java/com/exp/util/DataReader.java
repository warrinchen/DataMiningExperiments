package com.exp.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 功能: 读取数据文件
 */
public class DataReader {
    /**
     * 读取实验1的数据文件,会去除首行标题和首列编号
     * 文件格式示例:<br/>
     * title1,title2<br/>
     * no1,d11,d12<br/>
     * no2,d21,d22
     * @param path 文件路径
     * @return 一个列表,每个元素是一个列表,代表文件中的一行数据,元素数据项是由','分隔的数据,不包含第一行
     */
    public ArrayList<ArrayList<String>> readTxt1(String path) {
        BufferedReader br = null;
        // 数据集(文件)ArrayList, 每一个数据元素(一行)是ArrayList, 数据项(逗号分隔的字符串)是String
        ArrayList<ArrayList<String>> dataSet = new ArrayList<ArrayList<String>>();
        try {
            // 以GBK编码读入参数指定的数据文件
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "gbk"));
            String line = br.readLine(); //取出并丢弃第一行的标题
            // 读取文件每一行数据, 保存到dataSet里
            System.out.println("实验一数据集:");
            for (; (line = br.readLine()) != null; ) {
//                System.out.println(line);
                //对这一行数据进行处理
                line = line.replace(" ", ""); //去空格
                String[] items = line.split(",");
                    // 获取数据元素
                ArrayList<String> elem = new ArrayList<>();
                elem.addAll(Arrays.asList(items));
                    // 去除首列
                elem.remove(0);
                    //保存到数据集中
                System.out.println(elem.toString());
                dataSet.add(elem);
            }// end for:遍历文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return dataSet;
    }// end of function : readTxt1 读取实验一文件获取数据集

    /**
     * 读取实验二的文件, 读取数据并完成清洗, 排序
     * 文件格式示例:<br/>
     * 交易号TID	顾客购买的商品<br/>
     * T1	bread, cream, milk, tea<br/>
     * T2	bread, cream, milk<br/>
     * 项集I包含了所有项
     * @param path 文件路径
     * @return 一个列表(事务集), 每个元素是一个列表(事务,都是项集I的子项集), 元素内的最小单位是String(项)
     */
    public ArrayList<ArrayList<String>> readTxt2(String path){
        BufferedReader br = null;
        // 数据集(文件)ArrayList, 每一个数据元素(一行)是ArrayList, 数据项(逗号分隔的字符串)是String
        ArrayList<ArrayList<String>> dataSet = new ArrayList<>();
        try {
            // 以GBK编码读入参数指定的数据文件
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "gbk"));
            String line = br.readLine(); //取出并丢弃第一行的标题
            // 读取文件每一行数据, 保存到dataSet里
            for (; (line = br.readLine()) != null; ) {
                //对这一行数据进行处理
                line = line.replace(" ", "").substring(line.indexOf("\t") + 1); //去空格, 去回车, 去编号
                String[] items = line.split(",");
                // 获取数据元素
                ArrayList<String> elem = new ArrayList<>();
                elem.addAll(Arrays.asList(items));
                Collections.sort(elem); // 自然排序
                //保存到数据集列表中
                System.out.println("readTxt2 新增"+elem);
                dataSet.add(elem);
            }// end for:遍历文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("实验二数据集: "+dataSet);
        return dataSet;
    } // end of function readTxt1 读取实验二数据文件并返回数据

    /**
     * 按参数路径读取数据并统一放入"0"簇下, 返回簇Map
     * @param path 实验三数据文件路径
     * @return 原始簇
     */
    public Map<Integer, ArrayList<ArrayList<Double>>> readTxt3(String path) {
        BufferedReader br = null;
        // 数据集(文件)ArrayList, 每一个数据元素(一行)是ArrayList, 原始数据集都属于"0"类
        Map<Integer, ArrayList<ArrayList<Double>>> dataset = new HashMap<>();
        ArrayList<ArrayList<Double>> data = new ArrayList<>();
        dataset.put(0, data);

        try {
            // 以GBK编码读入参数指定的数据文件
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "gbk"));
            String line = br.readLine(); //取出并丢弃第一行的标题
            // 读取文件每一行数据, 保存到dataSet里
            for (; (line = br.readLine()) != null; ) {
                //对这一行数据进行处理
                ArrayList<Double> elem = new ArrayList<>();
                for (String it : line.substring(line.indexOf(",") + 1).split(",")) {
                    elem.add(Double.parseDouble(it));
                }
                //保存到数据集列表中
                System.out.println("readTxt2 新增项"+elem);
                data.add(elem);
            }// end for:遍历文件
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//        System.out.println("实验三数据集: "+dataset);

        return dataset;
    } // end of function readTxt3 读取实验二数据文件并返回数据
}
