import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by clarkxia on 16/5/26.
 */
public class Test {
    public static void main(String[] args) {
        String str = "{\"hczd.register_data\":[\"AutoId\",\"Times\"],\"hczd.gps_data\":[\"AutoId\",\"Times\"],\"hczd.term_log\":[\"AutoId\",\"Times\"]}";
        Map<String, List<String>> json = JSONObject.fromObject(str);
        System.out.println(json.size());
        List<String> data = json.get("hczd.gps_data");
        for(String d:data) {
            System.out.println(d);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = sdf.parse("20160326");

            System.out.println(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
