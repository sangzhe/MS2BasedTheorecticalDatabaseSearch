package com.shi.pitt.Hadoop.io;

import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/16.
 */
public class SpectrumWritable implements Writable {
    private Spectrum spectrum;
    private static final byte[] EMPTY_BYTES = new byte[0];
    private byte[] bytes;
    private int length;

    public void write(DataOutput dataOutput) throws IOException {
        if (spectrum == null) {
            throw new IOException("Inner multiple object is null");
        }
        byte[] bytes = spectrum.toString().getBytes();
        int length = bytes.length;
        dataOutput.writeInt(length);
        dataOutput.write(bytes);

    }

    public void readFields(DataInput dataInput) throws IOException {
        int length = dataInput.readInt();
        byte[] bytes = new byte[length];
        dataInput.readFully(bytes);

        String spectrumStr = new String(bytes);

        spectrum = Utils.parseSpectrumFromString(spectrumStr);
    }



}
