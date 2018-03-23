package com.shi.pitt.Hadoop.combiner;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sangzhe on 2018/3/22.
 */
public class SpectrumMassCombiner extends Reducer<IntegerPrecursorMassKey,Text,IntegerPrecursorMassKey,Text> {

    private Text valueoutput = new Text();

    public void reduce(Text integerPrecursorMassKey, Iterator<Text> iterator, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {
        valueoutput.clear();
        while(iterator.hasNext()){
            Text value = iterator.next();
            valueoutput.append(value.getBytes(),0,value.getLength());
        }
        outputCollector.collect(integerPrecursorMassKey,valueoutput);
    }

    public void close() throws IOException {

    }

    public void configure(JobConf jobConf) {

    }
}
