import com.exp.exp1.DataMiningExp1;
import com.exp.util.*;

import java.util.ArrayList;

public class TestExp1 {

    public static void main(String[] args) {
        // 实验1 朴素贝叶斯分类
        ArrayList<String> testData = new ArrayList<>();
        testData.add("青绿");
        testData.add("稍蜷");
        testData.add("浊响");
        testData.add("清晰");
        testData.add("凹陷");
        testData.add("硬滑");
        DataMiningExp1 exp1 = new DataMiningExp1();
        exp1.analysisExp1("D:/data/dataset.txt", testData);
    }
}
