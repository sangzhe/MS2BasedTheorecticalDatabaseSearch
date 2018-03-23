package com.shi.pitt.Hadoop.key;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/22.
 */
public class IntegerPrecursorMassKey implements WritableComparable<IntegerPrecursorMassKey> {

    private final double ActualPrecursorMass;
    private final int PrecursorMass;
    private final String PrecursorMassKey;




    public IntegerPrecursorMassKey(double precursorMass) {
        ActualPrecursorMass = precursorMass;
        PrecursorMass = (int)precursorMass;
        PrecursorMassKey = Integer.toString(PrecursorMass);
    }


    public double getActualPrecursorMass(){return ActualPrecursorMass;}

    public int getPrecursorMass() {
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

    public int compareTo(IntegerPrecursorMassKey o) {
        return Double.compare(getPrecursorMass(),o.getPrecursorMass());
    }


    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(PrecursorMass);
        dataOutput.writeDouble(ActualPrecursorMass);
        dataOutput.writeUTF(PrecursorMassKey);

    }

    public void readFields(DataInput dataInput) throws IOException {
        dataInput.readInt();
        dataInput.readDouble();
        dataInput.readUTF();
    }
}
