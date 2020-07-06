package com.imooc.bigdata.hadoop.hdfs;

//自定义WordCount实现类

import sun.rmi.server.InactiveGroupException;

public class WordCountMapper implements HxxMapper{
    @Override
    public void map(String line, HxxContext context) {
        String[] words = line.split(" ");

        for(String word: words){
            Object value = context.get(word);
            if(value == null){  //没有出现过该单词
                context.write(word,1);
            }else{
                int v = Integer.parseInt(value.toString());
                context.write(word,v+1);  //取出单词对应的次数+1
            }
        }
    }
}
