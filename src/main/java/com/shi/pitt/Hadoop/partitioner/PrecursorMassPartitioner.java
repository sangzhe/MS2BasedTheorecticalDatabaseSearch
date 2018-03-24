package com.shi.pitt.Hadoop.partitioner;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class PrecursorMassPartitioner extends Partitioner<IntegerPrecursorMassKey,Text> {
    public int getPartition(IntegerPrecursorMassKey integerPrecursorMassKey, Text text, int numberOfReducers) {

        int hash = integerPrecursorMassKey.getPrecursorMass() % numberOfReducers;
        return  hash;
    }
}
