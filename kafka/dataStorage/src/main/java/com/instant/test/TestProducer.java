package com.instant.test;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class TestProducer {
		public static void main(String[] args) {
			long events =1;
			Random rnd = new Random();
			
			Properties properties = new Properties();
			properties.put("metadata.broker.list","192.168.119.128:9092");
			properties.put("serializer.class","kafka.serializer.StringEncoder");
			ProducerConfig producerConfig = new ProducerConfig(properties);
			Producer<String,String> producer = new Producer<String,String>(producerConfig);
			for (long nEvents=0; nEvents<events; nEvents++) {
				long runtime= new Date().getTime();
				String ip = "1"+rnd.nextInt(255);
		//login				
//		String msg ="{\"id\":\"8888161213943614\",\"type\":1,\"username\":\"王丹妮\",\"password\":\"9999\",\"ll_id\":null,\"login_time\":\"1499321168266\",\"wei_openid\":\"\"}";
				
	    //regist				
		String msg1="{\"id\":\"8888161213339455\",\"name\":\"xm\",\"username\":\"小明\",\"password\":\"123\",\"wei_openid\":\"1400\",\"regist_time\":\"1499321674328\",\"type\":\"1\"}";
				
		//attention		
		String msg2="{\"eb_id\":888,\"ebq_id\":212112,\"wei_openid\":\"曾琴\",\"nickname\":\"123\",\"sex\":1,\"city\":\"武汉\",\"country\":\"中国\",\"province\":\"湖北\",\"language\":\"英语\",\"headimgurl\":\"www.baidu.com\",\"attention_time\":\"1499321686598\",\"type\":2}";
				
		KeyedMessage<String,String> data1 = new KeyedMessage<String, String>("registLog",ip,msg1);
		KeyedMessage<String,String> data2 = new KeyedMessage<String, String>("attentionLog",ip,msg2);
		producer.send(data1);
		producer.send(data2);
	}
			producer.close();
  }
}
