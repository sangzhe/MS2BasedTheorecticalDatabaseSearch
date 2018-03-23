package com.shi.pitt.Hadoop.mapper;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/16.
 */
public class SpectrumMapper extends Mapper<Text,Text,Text,Text> {


    public void map(Text key, Text value, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {

        // check the validity of the input
        if (key.toString().length() == 0 || value.toString().length() == 0)
            return;

        Spectrum spectrum = Utils.parseSpectrumFromString(value.toString());
        double mass = spectrum.getMass();
        reporter.getCounter("Spectrua","TotalNumber").increment(1);

        IntegerPrecursorMassKey massKey = new IntegerPrecursorMassKey(mass);
        outputCollector.collect(new Text(massKey.toString()),value);

        String PrecursorCounter = massKey.toString();

        reporter.getCounter("PrecursorMassInterger",PrecursorCounter).increment(1);

    }

    public void close() throws IOException {

    }

    public void configure(JobConf jobConf) {

    }
}
