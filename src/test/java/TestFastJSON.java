import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFastJSON {
    @Test
    public void testFastJSON() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        List<Map<String, String>> d = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "1");
        d.add(map);
        System.out.println(JSON.toJSON(d));
    }
}
