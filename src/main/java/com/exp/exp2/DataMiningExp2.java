package com.exp.exp2;

import com.exp.entity.Exp2Data;
import com.exp.util.DataReader;

import java.util.*;

public class DataMiningExp2 {
    ArrayList<ArrayList<String>> notFreqLList = null;  // 非频繁项集列表
    ArrayList<ArrayList<String>> notConfLList = null;  // 低置信度规则前件X
    Map<ArrayList<String>, Double> pXYX = null;  // 频繁项集产生关联规则的前件/后件概率

    public DataMiningExp2() {
        notFreqLList = new ArrayList<>();
        notConfLList = new ArrayList<>();
        pXYX = new HashMap<>();
    }

    public Exp2Data AprioriAlgm(String path, Map<String, Double> info) {
        double min_sup = info.get("min_sup");         // 最小支持度 P(XY)
        double min_conf = info.get("min_conf");       // 最小置信度 P(Y | X) = P(XY) / P(X)
        int min_sup_cnt = 0;                          // 最小支持度计数
        Exp2Data exp2Data = new Exp2Data();           // 要返回的实验二数据
        DataReader dataReader = new DataReader();     // 数据读取类
        ArrayList<ArrayList<String>> dataset = null;   // 训练数据集
        ArrayList<Map<ArrayList<String>, Integer>> kLMList = null;        // k项集列表 初始k=1
        ArrayList<Map<ArrayList<String>, Integer>> k1LMList = null;       // 临时项集列表 临时保存k+1项集列表
        Map<ArrayList<String>, ArrayList<String>> strongRuleLLMap = new HashMap<>();  //  强关联规则 集合

        // 获取数据
        dataset = dataReader.readTxt2(path);
        min_sup_cnt = (int)Math.ceil(min_sup * dataset.size()); // 向上取整, 求最小支持度计数
        System.out.println("\n最小支持度:" + min_sup + "  最小支持度计数:" + min_sup_cnt
                + "  最小置信度:" + min_conf + "  事务数:" + dataset.size() + "\n");

        // TODO 一.生成频繁一项集
        // 产生频繁1-项集
        kLMList = getOneItemSetList(dataset, min_sup_cnt);
        exp2Data.getFreqListMap().put("频繁1项集", kLMList.toString());
        // exp2Data 收集数据
        exp2Data.getTree().put("name", new ArrayList<>());
        exp2Data.getTree().put("children", new ArrayList<Map>());
        for (Map<ArrayList<String>, Integer> lmap : kLMList) {
            for (ArrayList<String> list : lmap.keySet()) {
                Map treeNode = exp2Data.getTree();
                System.out.println("DataMiningExp2.AprioriAlgm: treeNode.name "+treeNode.get("name"));
                // name延长
                ArrayList<String> oldL = (ArrayList<String>) treeNode.get("name");
                oldL.addAll(list);
                treeNode.put("name", oldL);
                // 新增子节点
                Map map = new HashMap();
                map.put("name", list);
                map.put("children", new ArrayList<Map>());
                ((ArrayList<Map>) treeNode.get("children")).add(map);

            }
        }

        // 产生频繁k+1项集, 直到频繁k+1项集为空
        for(int k = 2;;k++){
            // TODO 二. 生成k+1频繁项集
            k1LMList = getK1FreqItemSetList(kLMList, dataset, min_sup_cnt);

            if (k1LMList.size() != 0) { // 若临时项集(k+1频繁项集)不为空, 保存临时项集到k项集
                kLMList = k1LMList;
                exp2Data.getFreqListMap().put("频繁"+Integer.toString(k)+"项集", kLMList.toString());
                exp2Data.setK(k);
                //把k+1频繁项集kLMList放入exp2Data中
                ArrayList<Map> children = new ArrayList<>();
                // TODO 放入exp2Data中
                Map treeNode = exp2Data.getTree();
                for (Map<ArrayList<String>, Integer> lIMap : kLMList) {
                    for (ArrayList<String> list : lIMap.keySet()) {
                        // 遍历tree, 寻找与其前k-1个节点相同的节点作为父节点
                        ArrayList<Map> node = findNode(treeNode, list);
                        Map subNode = new HashMap();
                        subNode.put("name", list);
                        subNode.put("children", new ArrayList<>());
                        node.add(subNode);
                    }// end for 提取频繁项集
                }// end for 遍历频繁项集集合
            } else { // 结束, 频繁项集为kLMList
                // 把最终节点的children属性变为value(支持度)
                Map treeNode = exp2Data.getTree();
                cdToVal(treeNode);
                System.out.println("AprioriAlgm 最终频繁项集: " + kLMList + "\n");
                break;
            }
        }

        // TODO 三.在频繁k项集集合中获取强关联规则
        for (Map<ArrayList<String>, Integer> lMap : kLMList) {
            pXYX.clear(); // 清空 前一个频繁项集产生关联规则的前件/后件概率
            ArrayList<String> X = null;
            int cnt = 0; // 频繁项集的支持度计数
            for (ArrayList<String> list : lMap.keySet()) { //取频繁项集
                X = list;
                double pXYNum = (double) lMap.get(list) / dataset.size();
                pXYX.put(X, pXYNum);
                System.out.println("AprioriAlgm: 取频繁项集XY  p(XY) " + list + " = " + pXYNum + "\n");
            }
            strongRuleLLMap.putAll(getRule(X, X, dataset, min_conf, cnt));
        }

        ArrayList<String> stgRule = new ArrayList<>();

        System.out.println("AprioriAlgm: 强关联规则 \n" + strongRuleLLMap + "\n");
        for (ArrayList<String> ruleX : strongRuleLLMap.keySet()) {
            System.out.println(ruleX + " => " + strongRuleLLMap.get(ruleX));
            stgRule.add(ruleX + " => " + strongRuleLLMap.get(ruleX));
        }
        exp2Data.getStrongRuleList().addAll(stgRule);

        return exp2Data;
    }// end of function getRules

    /**
     * 寻找匹配的父节点
     * @param treeNode 初始节点是root, 代表全集, 然后从频繁1项集开始递增
     * @param list 要匹配的子节点
     * @return
     */
    private ArrayList<Map> findNode(Map treeNode, ArrayList<String> list) {
        ArrayList<String> name= (ArrayList<String>) treeNode.get("name");
        ArrayList<Map> children = (ArrayList<Map>) treeNode.get("children");
        // 如果name是list的子集(size-1), 返回children
        int i = 0;
        // 如果name比list长, 当前节点为root, 直接遍历当前节点的子节点
        if (name.size() >= list.size()) {
            for (Map map : children) {
                ArrayList<Map> res = findNode(map, list);
                if (res != null){
                    return findNode(map, list);
                }
            }
        }
        // name比list短, 如果当前节点为list的子集且当前节点长度+1 = list, 返回
        for (i = 0; i < name.size(); i++) {
            if (!name.get(i).equals(list.get(i))) {
                i = 0;
                break;
            }
        }
        if (i == name.size() && name.size() == list.size() - 1) { // 如果name是list的子集(size-1), 返回children
            System.out.println("DataMiningExp2.findNode: name and list "+name+" 是 "+list+"的子集");
            return children;
        } else if (i == name.size() && name.size() < list.size() - 1) { //如果name是list的子集(小于size-1), 遍历子节点
            for (Map map : children) {
                ArrayList<Map> res = findNode(map, list);
                if (res != null){
                    return findNode(map, list);
                }
            }
        }
        System.out.println("DataMiningExp2.findNode: name and list "+name+" 不是 "+list+"的子集");
        return null;// name不是list的子集, 返回空
    }

    /**
     * 把 没有子节点的节点的children属性变为value = 0
     * @param treeNode
     */
    private void cdToVal(Map treeNode){
        ArrayList<String> name= (ArrayList<String>) treeNode.get("name");
        ArrayList<Map> children = (ArrayList<Map>) treeNode.get("children");
        treeNode.remove("name");
        treeNode.put("name", name.toString());
        if (children.size() == 0) {
            treeNode.remove("children");
            treeNode.put("value", 0);
        } else {
            for (Map map : children) {
                cdToVal(map);
            }
        }
    }

    /**
     * 根据事务数据集获取并返回频繁一项集
     * @param dataset 事务数据集
     * @param min_sup_cnt
     * @return 频繁一项集
     */
    private ArrayList<Map<ArrayList<String>, Integer>> getOneItemSetList(ArrayList<ArrayList<String>> dataset, int min_sup_cnt){
        ArrayList<Map<ArrayList<String>, Integer>> k1LList = new ArrayList<>();
        ArrayList<Map<ArrayList<String>, Integer>> noFreqLList = new ArrayList<>();

        System.out.println();
        for (ArrayList<String> elemList : dataset){ //  遍历事务数据集
            for (String item : elemList){ // 遍历事务(一行)
                boolean inK1LList = false;
                if(k1LList.size() > 0){ // 当临时项集列表不为空时 才 遍历
                    for(Map<ArrayList<String>, Integer> tpListMap : k1LList) { // 遍历临时项集集合
                        for(ArrayList<String> tpList: tpListMap.keySet()){ //实际只有一个kv对
                            if(tpList.indexOf(item) != -1){ //如果事务中的这一项在临时项集中
                                tpListMap.put(tpList, tpListMap.get(tpList)+1); // 一项集计数+1
                                System.out.println("getOneItemSetList 一项集支持度计数更新: "+""+tpListMap);
                                inK1LList = true;
                                break;
                            }
                        }
                    } // end for 遍历临时项集集合
                } // end if 临时项集非空, 遍历
                if(!inK1LList) {// 如果数据集中的这一项不在临时项集中, 这一项加入临时项集集合
                    Map<ArrayList<String>, Integer> itMap = new HashMap<>();
                    ArrayList<String> tList = new ArrayList<>();
                    tList.add(item);
                    itMap.put(tList, 1);
                    k1LList.add(itMap);
                    System.out.println("getOneItemSetList 一项集新增项:"+item);
                }
            }// end for 遍历事务
        }// end for 遍历事务数据集

        // 获取非频繁一项集
        for (Map<ArrayList<String>, Integer> elMap : k1LList){
            for(ArrayList<String> elList : elMap.keySet()){
                if (elMap.get(elList) < min_sup_cnt){
                    noFreqLList.add(elMap);
                    System.out.println("\ngetOneItemSetList 非频繁一项集:"+elMap);
                }
            }
        }

        // 取出非频繁一项集, 以获取频繁一项集
        k1LList.removeAll(noFreqLList);
        System.out.println("\ngetOneItemSetList 最终频繁一项集:\n" + k1LList + "\n");
        return k1LList;
    }

    /**
     * 连接, 剪枝-获取k+1的频繁项集
     * @param kLMList 频繁k项集列表
     * @param min_sup_cnt 支持度计数
     * @return 频繁k+1项集, 若无, 返回 "空列表"
     */
    private ArrayList<Map<ArrayList<String>, Integer>> getK1FreqItemSetList(
            ArrayList<Map<ArrayList<String>, Integer>> kLMList,
            ArrayList<ArrayList<String>> dataset,
            int min_sup_cnt){
        ArrayList<Map<ArrayList<String>, Integer>> res = new ArrayList<>();
        // 连接 剪枝
        // 遍历每一个k频繁项集
        for(int i = 0; i < kLMList.size(); i++){
            Map<ArrayList<String>, Integer> listMap = kLMList.get(i);
            for (ArrayList<String> list : listMap.keySet()){  //  map中实际只有一个元素, 为list寻找前k-1个元素相同的项集
                // 寻找和list的前k-1个项相同的k项集
                for (int j = i + 1; j < kLMList.size(); j++){
                    Map<ArrayList<String>, Integer> lsMap = kLMList.get(j);
                    for (ArrayList<String> ls : lsMap.keySet()) {  //  map中实际只有一个元素
                        //  比较前k-1个元素是否相同
                        if (list.subList(0, list.size() - 1).equals(ls.subList(0, ls.size() - 1))) {
                            // 连接
                            ArrayList<String> tpList = new ArrayList<>(list);
                            tpList.add(ls.get(ls.size() - 1));
                            Collections.sort(tpList); // 内部元素排序
                            System.out.println("getK1FrepItemSetList k+1项集(sorted): "+tpList);

                            // TODO 二.1.剪枝-阶段一(非频繁项集的超集是非频繁项集)
                            boolean inNotFreq = false;
                            for (ArrayList<String> notFreqList : notFreqLList) { // 遍历非频繁项集集合
                                int k = 0;
                                for (k = 0; k < notFreqList.size(); k++) {
                                    if (!tpList.contains(notFreqList.get(k))) { //如果非频繁项集里的元素不在tpList中 k < size
                                        break;
                                    }
                                }
                                if (k == notFreqList.size()) { // 所有非频繁项集的元素都在tpList中
                                    System.out.println("getK1FreqItemSetList: " + tpList + " 在 非频繁项集 " + notFreqList + "中\n");
                                    inNotFreq = true;
                                    break;
                                } // end if
                            } // end for 遍历非频繁项集集合 第一阶段-剪枝
                            // TODO 二.2.剪枝-阶段二-不在非频繁项集中, 需要遍历事务数据集
                            if (!inNotFreq) { // 如果不在非频繁项集中, 去事务数据集中检测是否为非频繁项集
                                int min_supK1 = minSupCounter(tpList, dataset);
                                if (min_supK1 < min_sup_cnt) { // 最小支持度计数不够
                                    notFreqLList.add(tpList); // 加入非频繁项集
                                    System.out.println("getK1FreqItemSetList: 支持度计数 " + min_supK1 + " " +
                                            tpList + " 不满足最小支持度计数  " + min_sup_cnt + "\n");
                                } else { // 加入结果集
                                    Map<ArrayList<String>, Integer> k1LMap = new HashMap<>();
                                    k1LMap.put(tpList, min_supK1);
                                    res.add(k1LMap);
                                    System.out.println("getK1FreqItemSetList:支持度计数 " + min_supK1 + " " +
                                            tpList + " 满足 最小支持度计数 " + min_sup_cnt + "\n");
                                }// end if 第二阶段-剪枝
                            } // end if
                        }// end if 前k-1个相同, 就连接
                    }// end for 为k-1项集提取项集
                }// end for 为寻k-1相同的项集而遍历k项集
            } // end for 提取项集
        } // end for 遍历k项集
        System.out.println("getK1FreqItemSetList 最终k+1频繁项集: " + res + "\n");
        return  res;
    }

    /**
     * 为项集进行支持度计数
     * @param k1List 需要计数的k+1项集
     * @param dataset 事务数据集
     * @return 支持度
     */
    private int minSupCounter(ArrayList<String> k1List, ArrayList<ArrayList<String>> dataset) {
        int min_sup_cnt = 0;
        System.out.println();
        for (ArrayList<String> list : dataset){ //遍历数据集的每个事务
            int i = 0;
            for (i = 0; i < k1List.size(); i++) { // 查看k+1项集是否在list中
                if (list.indexOf(k1List.get(i)) == -1) {
                    System.out.println("minSupCounter:"+k1List+" 不属于事务:"+list);
                    break;
                }
            }
            if (i == k1List.size()) {
                min_sup_cnt++;
                System.out.println("minSupCounter: 计数+1 "+k1List+" in "+list);
            }
        }
        System.out.println("minSupCounter计数:" + min_sup_cnt + " " + k1List + "\n");
        return min_sup_cnt;
    }

    /**
     * 递归求强关联规则
     * @param X 关联规则前件
     * @param freqList 频繁项集集合
     * @param dataset 事务数据集
     * @param min_conf 最小置信度
     * @param sup_cnt 频繁项集的支持度计数
     * @return 强关联规则
     */
    private Map<ArrayList<String>, ArrayList<String>> getRule(ArrayList<String> X,
                                                              ArrayList<String> freqList,
                                                              ArrayList<ArrayList<String>> dataset,
                                                              double min_conf,
                                                              int sup_cnt){
        Map<ArrayList<String>, ArrayList<String>> rules = new HashMap<>(); // 规则map
        Map<ArrayList<String>, ArrayList<String>> rule = new HashMap<>(); // 一条规则
        ArrayList<String> Y = new ArrayList<>(); // 后件
        double pXY = 0d;
        double pX = 0d;

        // 0. X 是freqList的真子集时, 否则 直接 2.
        if (X.size() < freqList.size()) {
            // 1.1 若 X 是 notConfLList 中元素的子集, return 空规则集合
            for (ArrayList<String> ncList : notConfLList) {
                if (ncList.equals(X)) {
                    System.out.println("getRule: X " + X + " 是低置信度前件 " + ncList + " 的子集");
                    return rule;
                }
            }

            // 1.2 获得Y
            Y = new ArrayList<>(freqList);
            for (String item : X) { // 移除频繁项集中X的部分
                Y.remove(item);
            }
            Collections.sort(Y); // 保证字典序
            System.out.println("getRule: Y " + Y);

            // 1.2.1 获得pXY, pY(notConfLList中没有则计算) 并放入pXYX中
            if (pXYX.get(freqList) == null) {
                pXY = (double) sup_cnt / dataset.size();
                pXYX.put(freqList, pXY);
            } else {
                pXY = pXYX.get(freqList);
            }
            if (pXYX.get(X) == null) {
                int cnt = minSupCounter(X, dataset);
                pX = (double) cnt / dataset.size();
                pXYX.put(X, pX);
            } else {
                pX = pXYX.get(X);
            }

            // 1.2.2.1 若 pXY / pX < min_conf, 为低置信度规则, 记录, 然后return 空规则集, 否则 1.2.2.2
            if ((min_conf - (pXY / pX)) > 0.0001) { // 在误差小于0.0001 的前提下, 不满足最小置信度
                // 低置信度前件 X 放入notConfLList 中, 以便于排除后续低置信度规则
                System.out.println("getRule:" + X + " -> " + Y + " 置信度: " + (pXY / pX) + " 不满足最低置信度 " + min_conf + "\n");
                notConfLList.add(X);
                return rule;
            } else {
            // 1.2.2.2 X => Y 放入 rules中
                rules.put(X, Y);
                System.out.println("getRule:" + X + " => " + Y + " 置信度: " + (pXY / pX) + " 满足最低置信度 " + min_conf + "\n");
            }
        }

        // 2.若X的size>1 遍历X
        if (X.size() > 1) {
            for (int i = 0; i < X.size(); i++) {
                // 2.1 X = X - X[i]
                ArrayList<String> X1 = new ArrayList<>(X);
                X1.remove(i);
                System.out.println("getRule: X-1 " + X1);
                rules.putAll(getRule(X1, freqList, dataset, min_conf, sup_cnt));
            }
        }
        return rules;
    }
}
