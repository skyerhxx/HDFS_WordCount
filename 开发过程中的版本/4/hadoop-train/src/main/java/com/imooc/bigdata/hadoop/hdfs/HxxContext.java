package com.imooc.bigdata.hadoop.hdfs;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义上下文，其实就是缓存
 */
public class HxxContext {

    private Map<Object,Object> cacheMap = new HashMap<Object, Object>();

    public Map<Object,Object> getCacheMap(){
        return cacheMap;
    }

    //写数据到缓存中去
    //key是单词  value是次数
    public void write(Object key, Object value){
        cacheMap.put(key,value);
    }


    //从缓存中获取值
    //key 单词     return 单词对应的词频
    public Object get(Object key){
        return cacheMap.get(key);

    }


}
