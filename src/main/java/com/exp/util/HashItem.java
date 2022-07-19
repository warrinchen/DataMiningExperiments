package com.exp.util;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * 暂时无用................
 *
 * 把数据项映射到数字, 简化计算
 */
public class HashItem {
    /**
     * 把字符数据映射到整数
     * @return 对于每个数据项,其映射值都是从1开始的
     */
    public HashMap<String,Integer> hashData1(ArrayList<ArrayList<String>> dataSet) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        int[] cnt = new int[dataSet.get(1).size()]; // 长度为数据项个数的数组,记录每一个数据项对应的属性的取值数目
        for (ArrayList<String> elem: dataSet) { //每一行
            int i = 1;
            for (String item : elem) {          //每一数据项
                TODO 编号;
                // if
                //
            }
        }
        return null;
    }
}
