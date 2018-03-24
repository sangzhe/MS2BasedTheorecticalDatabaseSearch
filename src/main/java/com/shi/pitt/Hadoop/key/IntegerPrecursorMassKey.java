package com.shi.pitt.Hadoop.key;


import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/22.
 */
public class IntegerPrecursorMassKey implements WritableComparable<IntegerPrecursorMassKey> {

    private final DoubleWritable ActualPrecursorMass;
    private final IntWritable PrecursorMass;
    private final Text PrecursorMassKey;


    public IntegerPrecursorMassKey() {
        ActualPrecursorMass = new DoubleWritable();
        PrecursorMass = new IntWritable();
        PrecursorMassKey = new Text();
    }

    public IntegerPrecursorMassKey(double precursorMass) {
        ActualPrecursorMass = new DoubleWritable(precursorMass);
        PrecursorMass = new IntWritable((int)precursorMass);
        PrecursorMassKey = new Text(Integer.toString((int)precursorMass));
    }
    public void set(double precursorMass){
        ActualPrecursorMass.set(precursorMass);
        PrecursorMass.set((int)precursorMass);
        PrecursorMassKey.set(Integer.toString((int)precursorMass));
    }
    public double getActualPrecursorMass(){return ActualPrecursorMass.get();}

    public int getPrecursorMass() {
        return PrecursorMass.get();
    }

    @Override
    public String toString() {
        return PrecursorMassKey.toString() ;
    }



    public void write(DataOutput dataOutput) throws IOException {
        PrecursorMass.write(dataOutput);
        ActualPrecursorMass.write(dataOutput);
        PrecursorMassKey.write(dataOutput);

    }

    public void readFields(DataInput dataInput) throws IOException {
        PrecursorMass.readFields(dataInput);
        ActualPrecursorMass.readFields(dataInput);
        PrecursorMassKey.readFields(dataInput);
    }

    public int compareTo(IntegerPrecursorMassKey o) {
        return Double.compare(getPrecursorMass(),o.getPrecursorMass());
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(getPrecursorMass()).hashCode();
    }
}
