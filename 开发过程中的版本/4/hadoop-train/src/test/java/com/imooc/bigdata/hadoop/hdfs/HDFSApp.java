package com.imooc.bigdata.hadoop.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.annotation.ExceptionProxy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

//使用Java API操作HDFS文件系统

public class HDFSApp {

    public static final String HDFS_PATH = "hdfs://hadoop000:8020";

    FileSystem fileSystem = null;
    Configuration configuration = null;

    @Before
    public void setUp() throws Exception {
        System.out.println("------setUp-------");
        configuration = new Configuration() ;
        configuration.set("dfs.replication","1");
        /**
         * 构造一个访问指定HDFS系统的客户端对象
         * 第一个参数：HDFS的URI
         * 第二个参数：客户端指定的配置参数
         * 第三个参数：客户端的身份，说白了就是用户名
         */
        fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, "hadoop");
    }

    //创建HDFS目录
    @Test
    public void mkdir() throws Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }

    //查看HDFS内容
    @Test
    public void text() throws Exception{
        FSDataInputStream in = fileSystem.open(new Path("/NOTICE.txt"));
        IOUtils.copyBytes(in,System.out,1024);
    }

    //创建文件并写入
    @Test
    public void create() throws Exception{
        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/b.txt"));
        out.writeUTF("hello pk");
        out.flush();
        out.close();
    }

    //重命名
    @Test
    public void rename() throws Exception{
        Path oldPath = new Path("/hdfsapi/test/b.txt");
        Path newPath = new Path("/hdfsapi/test/c.txt");
        boolean result = fileSystem.rename(oldPath,newPath);
        System.out.println(result);
    }

    //复制本地文件到HDFS文件系统
    @Test
    public void copyFromLocalFile() throws Exception{
        Path src = new Path("D://hello.txt");
        Path dst = new Path("/hdfsapi/test/");
        fileSystem.copyFromLocalFile(src,dst);
    }

    //复制大文件到HDFS文件系统(带进度)
    @Test
    public void copyFromLocalBigFile() throws Exception{

        InputStream in = new BufferedInputStream(new FileInputStream(new File("H://jdk-8u221-linux-x64.tar.gz")));

        FSDataOutputStream out = fileSystem.create(new Path("/hdfsapi/test/jdk.tgz"),
                new Progressable() {
                    @Override
                    public void progress() {
                        System.out.print(".");
                    }
                });

        IOUtils.copyBytes(in, out ,4096);
    }

    //从HDFS复制文件到本地
    @Test
    public void copyToLocalFile() throws Exception{
        Path hdfsPath = new Path("/hdfsapi/test/hello.txt");
        Path localPath = new Path("D://");
        fileSystem.copyToLocalFile(false, hdfsPath,localPath,true);
    }

    //列出文件夹下的所有文件
    @Test
    public void listFiles() throws Exception{
        FileStatus[] statuses = fileSystem.listStatus(new Path("/hdfsapi/test/"));

        for(FileStatus file:statuses){
            String isDir = file.isDirectory() ? "文件夹":"文件";
            String permission = file.getPermission().toString();
            short replication = file.getReplication();
            long length = file.getLen();
            String path = file.getPath().toString();

            System.out.println(isDir + "\t" + replication + "\t" + length + "\t" + path);
        }

    }

    //查看文件块信息
    @Test
    public void getFileBlockLocations() throws Exception{

        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/hdfsapi/test/jdk.tgz"));

        BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus,0,fileStatus.getLen());

        for(BlockLocation block:blocks){

            for(String name:block.getNames()){
                System.out.println(name+" : "+block.getOffset()+" : "+block.getLength());
            }
        }
    }


    //删除文件
    @Test
    public void delete() throws Exception{
        boolean result = fileSystem.delete(new Path("/hdfsapi/test/jdk.tgz"),true);
        System.out.println(result);
    }


    @After
    public void tearDown(){
        configuration = null;
        fileSystem = null;
        System.out.println("------tearDown-------");
    }

}
