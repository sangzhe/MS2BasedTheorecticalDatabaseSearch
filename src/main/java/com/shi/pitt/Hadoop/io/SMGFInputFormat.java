package com.shi.pitt.Hadoop.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;

/**
 * Created by sangzhe on 2018/3/21.
 */
public class SMGFInputFormat extends FileInputFormat {

    private CompressionCodecFactory compressionCodecs = null;
    private long start;
    private long end;
    private long pos;
    private LineReader input;
    private FSDataInputStream realFile;
    private Text key = new Text();
    private Text value = new Text();
    private Text buffer = new Text();
    private Text endLine = new Text("\n");
    private Text startSpectrum = new Text("BEGIN IONS");
    private Text endSpectrum = new Text("END IONS");

    public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new SMGFReader();
    }


    public class SMGFReader extends RecordReader{

        public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            FileSplit split = (FileSplit)inputSplit;
            Configuration configuration = taskAttemptContext.getConfiguration();

            // get start and end location
            start = split.getStart();
            end = start + split.getLength();

            //get the location where split lies
            final Path file = split.getPath();
            compressionCodecs = new CompressionCodecFactory(configuration);
            boolean skipFirstLine = false;
            final CompressionCodec codec = compressionCodecs.getCodec(file);



            // initilize the HDFS instance
            FileSystem fs = file.getFileSystem(configuration);
            // open the file and seek to the start of the split
            realFile = fs.open(split.getPath());
            if (codec != null) {
                CompressionInputStream inputStream = codec.createInputStream(realFile);
                input = new LineReader(inputStream);
                end = Long.MAX_VALUE;
            } else {
                if (start != 0) {
                    skipFirstLine = true;
                    --start;
                    realFile.seek(start);
                }
                input = new LineReader(realFile);
            }
            // not at the beginning so go to first line
            if (skipFirstLine) {  // skip first line and re-establish "start".
                start += input.readLine(buffer);
            }

            key.set(split.getPath().getName());
            value.clear();

            pos = 0;

        }

        public boolean nextKeyValue() throws IOException, InterruptedException {
            int newSize = 0;
            while (pos < start) {
                newSize = input.readLine(buffer);
                // we are done
                if (newSize == 0) {
                    key = null;
                    value = null;
                    return false;
                }
                pos = realFile.getPos();
            }

            newSize = input.readLine(buffer);
            while (newSize > 0) {
                if (startSpectrum.equals(buffer)) {
                    break;
                }
                newSize = input.readLine(buffer);
            }

            if (newSize == 0) {
                key = null;
                value = null;
                return false;
            }

            value.clear();

            while (newSize > 0) {
                value.append(buffer.getBytes(), 0, buffer.getLength());
                value.append(endLine.getBytes(), 0, endLine.getLength());
                if (endSpectrum.equals(buffer)) {
                    break;
                }
                newSize = input.readLine(buffer);
            }

            if (newSize == 0) {
                key = null;
                value = null;
                return false;
            } else {
                return true;
            }
        }

        public Object getCurrentKey() throws IOException, InterruptedException {
            return key;
        }

        public Object getCurrentValue() throws IOException, InterruptedException {
            return value;
        }

        public float getProgress() throws IOException, InterruptedException {
            long totalBytes = end - start;
            long totalHandled = pos - start;
            return ((float) totalHandled) / totalBytes;
        }

        public void close() throws IOException {
            if (input != null) {
                input.close();
            }
        }
    }

}
