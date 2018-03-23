package com.shi.pitt.Hadoop.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class PrecursorMassPartitioner extends Partitioner<Text,Text> {
    public int getPartition(Text integerPrecursorMassKey, Text text, int numberOfReducers) {

        String key = integerPrecursorMassKey.toString();
        int hash = Integer.parseInt(key) % numberOfReducers;
        return  hash;
    }
}
