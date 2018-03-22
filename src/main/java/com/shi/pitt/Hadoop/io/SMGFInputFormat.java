package com.shi.pitt.Hadoop.io;

import com.shi.pitt.MS2Search.Utils;
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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.LineReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by sangzhe on 2018/3/21.
 */
public class SMGFInputFormat extends TextInputFormat {

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

    private Map<Long,List<Long>> ClusterInfo;

    public class SMGFReader extends RecordReader{
        private void loadClusterInfo(String clusterInfo){
            ClusterInfo = Utils.readClusterInfoFromString(clusterInfo);
        }

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

            key.clear();
            value.clear();

            pos = 0;


            loadClusterInfo(configuration.get("ClusterInfo"));

        }

        public boolean nextKeyValue() throws IOException, InterruptedException {
            return false;
        }

        public Object getCurrentKey() throws IOException, InterruptedException {
            return key;
        }

        public Object getCurrentValue() throws IOException, InterruptedException {
            return value;
        }

        public float getProgress() throws IOException, InterruptedException {
            return 0;
        }

        public void close() throws IOException {

        }
    }

}
