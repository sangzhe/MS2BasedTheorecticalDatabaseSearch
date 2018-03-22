package com.shi.pitt.Hadoop.key;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/22.
 */
public class PrecursorMassKey implements WritableComparable {

    private final double PrecursorMass;
    private final String PrecursorMassKey;

    public PrecursorMassKey(double precursorMass, String precursorMassKey) {
        PrecursorMass = precursorMass;
        PrecursorMassKey = precursorMassKey;
    }

    public double getPrecursorMass() {
        return PrecursorMass;
    }

    @Override
    public String toString() {
        return PrecursorMassKey ;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && toString().equals(obj.toString());
    }



    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(PrecursorMass);
        dataOutput.writeUTF(PrecursorMassKey);

    }

    public void readFields(DataInput dataInput) throws IOException {
        dataInput.readDouble();
        dataInput.readUTF();
    }
}
