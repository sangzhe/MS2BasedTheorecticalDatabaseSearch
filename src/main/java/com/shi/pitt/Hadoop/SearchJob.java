package com.shi.pitt.Hadoop;

import com.shi.pitt.Hadoop.combiner.SpectrumMassCombiner;
import com.shi.pitt.Hadoop.io.SMGFInputFormat;
import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import com.shi.pitt.Hadoop.mapper.SpectrumMapper;
import com.shi.pitt.Hadoop.partitioner.PrecursorMassPartitioner;
import com.shi.pitt.Hadoop.reducer.SpectraGroupComparisonReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class SearchJob extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int exitcode = ToolRunner.run(new SearchJob(), args);
        System.exit(exitcode);
    }

    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.printf("Usage: %s [generic options] <job name> <job configuration file> <output directory> <input directory> \n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Configuration configuration = getConf();

        // load custom configurations for the job
        configuration.addResource(args[0]);

        Job job = new Job(configuration);
        job.setJarByClass(getClass());

        job.setNumReduceTasks(30);

        configuration.set("Fragment.Tolerance","20");
        configuration.set("Fragment.MatchRatio","0.5");
        configuration.set("Mass.Tolerance","20");


        // configure input and output path
        FileInputFormat.addInputPath(job, new Path(args[1]));

        Path outputDir = new Path(args[2]);
        FileSystem fileSystem = outputDir.getFileSystem(configuration);
        FileOutputFormat.setOutputPath(job, outputDir);

        // input format
        job.setInputFormatClass(SMGFInputFormat.class);
        // output format
        job.setOutputFormatClass(TextOutputFormat.class);

        // set mapper, reducer and partitioner
        job.setMapperClass(SpectrumMapper.class);

        job.setCombinerClass(SpectrumMassCombiner.class);

        job.setPartitionerClass(PrecursorMassPartitioner.class);

        job.setReducerClass(SpectraGroupComparisonReducer.class);


        // set output class
        job.setMapOutputKeyClass(IntegerPrecursorMassKey.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);

        boolean completion = job.waitForCompletion(true);

//        if (completion) {
//            // output counters for the next job
//            String counterFileName = args[2];
//            HadoopUtilities.saveCounters(fileSystem, counterFileName, job);
//        }

        return completion ? 0 : 1;

    }
}
