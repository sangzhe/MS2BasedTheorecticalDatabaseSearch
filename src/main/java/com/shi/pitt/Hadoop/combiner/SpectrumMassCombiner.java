package com.shi.pitt.Hadoop.combiner;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by sangzhe on 2018/3/22.
 */
public class SpectrumMassCombiner extends Reducer<IntegerPrecursorMassKey,Text,IntegerPrecursorMassKey,Text> {

    private Text valueoutput = new Text();

    @Override
    protected void reduce(IntegerPrecursorMassKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        valueoutput.clear();
        Iterator<Text> iterator = values.iterator();
        while(iterator.hasNext()){
            Text value = iterator.next();
            valueoutput.append(value.getBytes(),0,value.getLength());
        }
        context.write(key,valueoutput);
    }



}
