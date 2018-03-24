package com.shi.pitt.Hadoop.mapper;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/16.
 */
public class SpectrumMapper extends Mapper<Text,Text,IntegerPrecursorMassKey,Text> {


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
    }

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {

        // check the validity of the input
        if (key.toString().length() == 0 || value.toString().length() == 0)
            return;

        Spectrum spectrum = Utils.parseSpectrumFromString(value.toString());
        double mass = spectrum.getMass();
        context.getCounter("Spectrua","TotalNumber").increment(1);

        IntegerPrecursorMassKey massKey = new IntegerPrecursorMassKey(mass);
        context.write(massKey,value);


    }



}
