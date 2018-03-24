package com.shi.pitt.Hadoop.mapper;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/24.
 */
public class TheoreticalSpectrumGeneratorMapper extends Mapper<Text,Text,DoubleWritable,Text> {
    @Override
    //input key:file split
    //      value:amino acid sequence
    //output key:Precursor mass as integer
    //      value: theretical spectrum in mgf format
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {

    }
}
