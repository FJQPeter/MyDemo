package com.instant.datastorage.store.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.instant.datastorage.entity.MysqlDataEntity;
import com.instant.datastorage.entity.MysqlField;
import com.instant.datastorage.queue.MsgForMysqlQueue;


public class MysqlStorage implements Runnable{
    private static final Logger logger = Logger.getLogger(MysqlStorage.class);
    private final long THREAD_SLEEP_TIME = 300;
    
	public void run() {
			long start=0L;
			MsgForMysqlQueue dataQueue = MsgForMysqlQueue.getInstance();
			try{
				while (true) {
					List<MysqlDataEntity> data = dataQueue.getAllData();
					if(data.size() > 0) {
				    	//jdbc执行批量insert
				    	Connection conn = DBCPUtil.getConnection();;
						PreparedStatement prep = null;
				    	try {
				    		/**设置不自动提交，以便于在出现异常的时候数据库回滚**/
				            conn.setAutoCommit(false);
			    			for (MysqlDataEntity entity : data) {
			    				
				    			String sql = appendInsertSql(entity);
				    			prep = conn.prepareStatement(sql);
				    			int i= 0;
				    			for(Map.Entry<String, MysqlField> entry: entity.getFileds().entrySet()) {
				    				i++;
				    				MysqlField field = entry.getValue();
				    				if("STRING".equals(field.getType())) {
				    				    prep.setString(i, String.valueOf(field.getValue()));
				    				}else if("NUMBER".equals(field.getType())) {
				    					prep.setInt(i, (Integer)field.getValue());
				    				}else if("DATE".equals(field.getType())) {
				    					prep.setTimestamp(i, (Timestamp)field.getValue());
				    				}
				    			}
				    			
				    			prep.addBatch();  //把SQL语句加入到批命令中
							}
			    			
			    			/*l采用PreparedStatement.addBatch()实现批处理
			    			•优点：发送的是预编译后的SQL语句，执行效率高。
			    			•缺点：只能应用在SQL语句相同，但参数不同的批处理中。因此此种形式的批处理经常用于在同一个表中批量插入数据，或批量更新表的数据。*/
			    			//当需要向数据库发送一批SQL语句执行时，应避免向数据库一条条的发送执行，而应采用JDBC的批处理机制，以提升执行效率。
				    		 int[] arr = prep.executeBatch();  //executeBatch()方法：执行批处理命令
				             conn.commit();
				             logger.info("成功了插入了" + arr.length + "行");
			    		
				    	}catch (Exception e) {
				    		e.printStackTrace();
				    		logger.info(e);
						}finally {
							conn.close();
						}
					}
					
					//本次处理花费的时间
					long cost = System.currentTimeMillis() - start;
					if (cost < THREAD_SLEEP_TIME) {
						Thread.sleep(THREAD_SLEEP_TIME);
				    }
             }
		 }catch (Exception e){
	            e.printStackTrace();
	     }
	}

	/**
	 * 拼接生成公共的sql语句
	 * @param entity
	 * @return
	 */
	private String appendInsertSql(MysqlDataEntity entity) {
		//多线程操作字符串缓冲区 下操作大量数据 = StringBuffer
		StringBuffer sb = new StringBuffer();
		StringBuffer fieldsBuffer  = new StringBuffer();
		StringBuffer valuesBuffer = new StringBuffer();
		int i = 0;
		for(Map.Entry<String, MysqlField> entry: entity.getFileds().entrySet()) {
			fieldsBuffer.append(i == 0 ? "" : ",").append(entry.getKey());
			valuesBuffer.append(i == 0 ? "" : ",").append("?");
			i++;
		}
		
		sb.append("INSERT INTO ");
		sb.append(entity.getTable_name());
		sb.append("(");
		sb.append(fieldsBuffer);
		sb.append(") VALUES (");
		sb.append(valuesBuffer);
		sb.append(")");
		
		return sb.toString();
	}	
}
