package com.shi.pitt.Hadoop.mapper;

import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/16.
 */
public class SpectrumMapper  implements Mapper<Text,Text,Text,Text> {

    private String mapTaskId;
    private String inputFile;

    public void map(Text text, Text text2, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {
        Spectrum spectrum = Utils.parseSpectrumFromString(text2.toString());


        Text val = new Text(spectrum.toString());
        outputCollector.collect(new Text("1"),val);

    }

    public void close() throws IOException {

    }

    public void configure(JobConf jobConf) {

    }
}
