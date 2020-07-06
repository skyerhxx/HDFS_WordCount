package com.imooc.bigdata.hadoop.hdfs;


//自定义Mapper

public interface HxxMapper {

    //line是读取到的每一行的数据，context是上下文/缓存
    public void map(String line, HxxContext context);




}
