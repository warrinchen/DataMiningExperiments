import com.exp.exp3.DataMiningExp3;
import com.exp.util.DataReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestExp3 {
    Map<Integer, ArrayList<ArrayList<Number>>> map;

    void initt() {
        map = new HashMap<>();
        ArrayList<ArrayList<Number>> c1 = new ArrayList<>();
        ArrayList<Number> item = new ArrayList<>();
        item.add(1);
        item.add(2);
        ArrayList<Number> item1 = new ArrayList<>();
        item1.add(3);
        item1.add(4);
        c1.add(item);
        c1.add(item1);

        ArrayList<ArrayList<Number>> c2 = new ArrayList<>();
        ArrayList<Number> item2 = new ArrayList<>();
        item2.add(5);
        item2.add(6);
        ArrayList<Number> item3 = new ArrayList<>();
        item3.add(7);
        item3.add(8);
        c2.add(item2);
        c2.add(item3);

        map.put(1, c1);
        map.put(2, c2);
    }
    void ad(Map<Integer, ArrayList<ArrayList<Number>>> map) {
        ArrayList<ArrayList<Number>> c1 = map.get(1);
        ArrayList<Number> c1It = c1.get(0);

        ArrayList<ArrayList<Number>> c2 = map.get(2);
        ArrayList<Number> c2It = c1.get(0);

        c1.remove(0);
        c2.add(c1It);
    }
    @Test
    public void testMap() {
        initt();
        System.out.println(map);
        ad(map);
        System.out.println(map);
    }

    @Test
    public void testFun() {
        ArrayList<Integer> il = new ArrayList<>();
        for (Integer i : il) {
            System.out.println(i);
        }
    }

    @Test
    public void testExp3ByCourseData() {
        String path = "d:/data/exp3Coursedata.csv";
        Map<Integer, ArrayList<Double>> centers = new HashMap<>();
        ArrayList<Double> c1 = new ArrayList<>();
        ArrayList<Double> c2 = new ArrayList<>();
        c1.add(20d);
        c1.add(60d);
        c2.add(80d);
        c2.add(80d);

        centers.put(0, c1);
        centers.put(1, c2);

        DataMiningExp3 dataMiningExp3 = new DataMiningExp3();
        dataMiningExp3.kMeansAlgo(path, centers, 1e-2);

    }

    @Test
    public void testExp3ByExpData() {
        String path = "d:/data/exp3dataset.csv";
        Map<Integer, ArrayList<Double>> centers = new HashMap<>();
        ArrayList<Double> c1 = new ArrayList<>();
        ArrayList<Double> c2 = new ArrayList<>();
        ArrayList<Double> c3 = new ArrayList<>();
        c1.add(27d);
        c1.add(6d);
        c1.add(232.61d);
        c2.add(3d);
        c2.add(5d);
        c2.add(1507.11d);
        c3.add(4d);
        c3.add(16d);
        c3.add(817.62d);

//        c1.add(16d);
//        c1.add(10d);
//        c1.add(1910d);
//        c2.add(8d);
//        c2.add(11d);
//        c2.add(1195d);
//        c3.add(15d);
//        c3.add(7d);
//        c3.add(428d);

        centers.put(0, c1);
        centers.put(1, c2);
        centers.put(2, c3);

        DataMiningExp3 dataMiningExp3 = new DataMiningExp3();
        dataMiningExp3.kMeansAlgo(path, centers, 1e-2);

    }
}
