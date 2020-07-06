package com.imooc.bigdata.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用HDFS API完成wordcount统计
 *
 */
public class HDFS_WordCount1 {
    public static void main(String[] args) throws Exception{
        // 1)读取HDFS上的文件 ==> HDFS API
        Path input = new Path("/hdfsapi/test/hello.txt");

        //获取要操作的HDFS文件系统
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop000:8020"),new Configuration(),"hadoop");

        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(input,false);

        HxxMapper mapper = new WordCountMapper();
        HxxContext context = new HxxContext();

        while(iterator.hasNext()){
            LocatedFileStatus file = iterator.next();
            FSDataInputStream in = fs.open(file.getPath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while((line = reader.readLine()) != null){
                // 2)词频处理
                //将结果写到Cache中去
                mapper.map(line, context);
            }

            reader.close();
            in.close();
        }

        // 3)将结果缓存起来  Map



        Map<Object, Object> contextMap = context.getCacheMap();


        // 4)将结果输出到HDFS ==> HDFS API
        Path output = new Path("/hdfsapi/output/");

        FSDataOutputStream out = fs.create(new Path(output,new Path("wc.out")));

        //将第三步缓存中的内容输出到out中去
        Set<Map.Entry<Object, Object>> entries = contextMap.entrySet();

        for(Map.Entry<Object,Object> entry:entries){
            out.write((entry.getKey().toString() + "\t" + entry.getValue() + "\n").getBytes());
        }
        out.close();
        fs.close();

        System.out.println("hxx的HDFS API统计词频运行成功......");
    }
}
