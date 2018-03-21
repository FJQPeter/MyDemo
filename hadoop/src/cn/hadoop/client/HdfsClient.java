package cn.hadoop.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by FangYan on 2017/10/7 0007.
 */
public class HdfsClient {
    FileSystem fileSystem =null;
    @Before
    public  void  init() throws IOException, URISyntaxException, InterruptedException {
         Configuration conf = new Configuration();
         //如果没有这一行，将会拿到localfileSystem，本地文件系统
         conf.set("fs.defaultFS","hdfs://hadoop001:9000");
//         conf.set("dfs.replication","5");
        //拿到文件系统客户端实例对象
//         fileSystem=FileSystem.get(new URI("hdfs://hadoop001:9000"),conf,"root");
         fileSystem = FileSystem.get(conf);

    }

    @Test
    public void testUpload() throws IOException {
        //hadoop会先去系统环境变量中查看HADOOP_USER_NAME
        String java_home = System.getenv("HADOOP_USER_NAME");
        System.out.println(java_home);
        fileSystem.copyFromLocalFile(new Path("d:/PIC/xixi.jpg"),
                new Path("/access.log.copy"));
        fileSystem.close();
    }

    @Test
    public  void download() throws IOException {
        //注意hadoop的bin目录得有windows运行文件
        fileSystem.copyToLocalFile(false,new Path("/access.log.copy"),new Path("d:/"),false);
        fileSystem.close();
    }
}
