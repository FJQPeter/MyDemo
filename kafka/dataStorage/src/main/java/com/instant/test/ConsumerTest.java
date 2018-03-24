package com.instant.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.KafkaMessage;
import com.instant.datastorage.queue.MessageDataQueue;
import com.instant.datastorage.util.JSONUtil;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerTest {
	/*	Config.init();
		KafkaConsumerManager kaManager= new KafkaConsumerManager();
		 Thread thread1 = new Thread(kaManager);
		 thread1.start();
		 MessageConsumer consumer = new MessageConsumer();
		 Thread thread = new Thread(consumer);
		 thread.start();*/
	 private final ConsumerConnector consumer;
	 private final String topic;
	 private ExecutorService executor;
	
	public  ConsumerTest(String a_zookeeper,String a_groupId,String a_topic){
		consumer = (ConsumerConnector) Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper,a_groupId));
		this.topic= a_topic;
	}
	
	public void shutdown(){
		if(consumer != null){
			consumer.shutdown();
		}if(executor != null){
			executor.shutdown();
		}
	}
	
	public void run(int numThreads){
		Map<String,Integer> topicCountMap = new HashMap<String,Integer> ();
		topicCountMap.put(topic, new Integer(numThreads));
		Map<String , List<KafkaStream<byte[],byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[],byte[]>> streams = consumerMap.get(topic);
		
		executor= Executors.newFixedThreadPool(numThreads);  //固定大小线程池
		
		int threadNum = 0 ;
		for (final KafkaStream stream : streams) {
			executor.submit(new ConsumerMsgTask(stream,threadNum));
			threadNum++;
		}
	}
	
	
	public static void main(String[] arg) {	
	String[] args = {"192.168.119.128:2181","group-1","test","1"};
	String zookeeper = args[0];
	String groupId = args[1];
	String topic = args[2];
	int threads = Integer.parseInt(args[3]);
	
	ConsumerTest  demo = new ConsumerTest(zookeeper,groupId,topic);
	demo.run(threads);
	
	try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
  public class  ConsumerMsgTask implements Runnable{
		private KafkaStream m_stream;
		private int m_threadNumber;
		
		public ConsumerMsgTask(KafkaStream stream, int m_threadNumber){
			this.m_stream = stream;
			this.m_threadNumber = m_threadNumber;
		}
		
		public void run(){
			MessageDataQueue dataQueue = MessageDataQueue.getInstantce();
			ConsumerIterator<byte[],byte[]> it = m_stream.iterator();
			while(it.hasNext()){
				System.out.println("Thread" + m_threadNumber + ":" 
					+new String(it.next().message()));
				System.out.println("shut down thread" + m_threadNumber);
				KafkaMessage msg = new KafkaMessage();
				try {
					String json = new String(it.next().message());
					Map<String, Object> dataMap = JSONUtil.jsonStrToMap(json);
					msg.setTopic(topic);
					msg.setContent(json);
					msg.setData(dataMap);
					dataQueue.put(msg);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			List dataList = dataQueue.getAllData();
			for (Object object : dataList) {
				System.out.println(object.toString());
		}
	}
 }
	
    private static ConsumerConfig createConsumerConfig(String a_zookeeper,String a_groupId){
        Properties props = new Properties();
        props.put("zookeeper.connect",a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms","200");
        props.put("auto.commit.interval.ms","1000");
        return new ConsumerConfig(props);
    }
	
}
