import org.junit.Test;

import java.util.*;

public class TestSort {
    @Test
    public void testFunc(){
        System.out.println(Math.ceil(-0.4));
        System.out.println(Math.floor(2.4));
        System.out.println(Math.round(2.4));
    }
    @Test
    public void testSort(){
        ArrayList<Map<ArrayList<String>, Integer>> list = new ArrayList<>();
        Map<ArrayList<String>, Integer> map = new HashMap<>();
        ArrayList<String> sublist = new ArrayList<>();
        sublist.add("aa");
        map.put(sublist, 1);
        list.add(map);
        System.out.println(list);

        map.put(sublist, 2);
        System.out.println(list);

        list.add(map);
        System.out.println(list);
    }
}
