package cn.WebLogWash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 *  JDK在java.text包中，提供了对显示对象格式化的接口、类及异常处理，
 *  这里我们只来谈一谈text包中的format类及其子类
 */
public class WebLogParser {

	private static SimpleDateFormat sd1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);

	private static SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static WebLogBean parser(String line) {
		WebLogBean webLogBean = new WebLogBean();
		String[] arr = line.split(" ");
		if (arr.length > 11) {
			webLogBean.setRemote_addr(arr[0]);
			webLogBean.setRemote_user(arr[1]);
			webLogBean.setTime_local(parseTime(arr[3].substring(1)));
			webLogBean.setRequest(arr[6]);
			webLogBean.setStatus(arr[8]);
			webLogBean.setBody_bytes_sent(arr[9]);
			webLogBean.setHttp_referer(arr[10]);

			if (arr.length > 12) {
				webLogBean.setHttp_user_agent(arr[11] + " " + arr[12]);
			} else {
				webLogBean.setHttp_user_agent(arr[11]);
			}
			if (Integer.parseInt(webLogBean.getStatus()) >= 400) {// 大于400，HTTP错误
				webLogBean.setValid(false);
			}
		} else {
			webLogBean.setValid(false);
		}
		return webLogBean;
	}

	 private static String parseTime(String dt) {

		String timeString = "";
		try {
			Date parse = sd1.parse(dt);
			timeString = sd2.format(parse);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timeString;
	}

	public static void main(String[] args) {
		String parseTime = parseTime("18/Sep/2013:06:49:48");
		System.out.println(parseTime);
	}

}
