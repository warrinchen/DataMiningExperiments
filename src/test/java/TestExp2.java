import com.alibaba.fastjson.JSON;
import com.exp.entity.Exp2Data;
import com.exp.exp2.DataMiningExp2;
import com.exp.util.DataReader;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestExp2 {
    @Test
    public void testReadTxt2(){
        DataReader dataReader = new DataReader();
        ArrayList<ArrayList<String>> data =  dataReader.readTxt2("d:/data/exp2dataset.txt");
        System.out.println(data);
        for (ArrayList<String> elem : data) {
            for (String item : elem){
                System.out.print(item+"-");
            }
            System.out.println();
        }
    }

    @Test
    public void testDME2(){
        DataMiningExp2 dataMiningExp2 = new DataMiningExp2();
        Map<String, Double> info = new HashMap<>();
        info.put("min_sup", 0.3d);
        info.put("min_conf", 0.5d);
        Exp2Data exp2Data = dataMiningExp2.AprioriAlgm("D:/data/exp2dataset.txt", info);

        System.out.println(JSON.toJSON(exp2Data));

    }

    @Test
    public void testFun(){
        String uri = "/forward/exp1.html";
        String subUri=uri.substring(1);
        System.out.println(subUri);
        String page = subUri.substring(subUri.indexOf("/") + 1);
        System.out.println(page);
    }

}
