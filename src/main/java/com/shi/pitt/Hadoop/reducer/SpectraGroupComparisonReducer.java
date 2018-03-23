package com.shi.pitt.Hadoop.reducer;

import com.shi.pitt.MS2Search.Cluster;
import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class SpectraGroupComparisonReducer extends Reducer<Text,Text,Text,Text> {
    private List<Spectrum> allSpectra = new ArrayList<Spectrum>();
    private List<Cluster> clusters = new ArrayList<Cluster>();
    private int fragmentTolerance;
    private int massTolerance;
    private float matchRatio;

    public void reduce(Text integerPrecursorMassKey, Iterator<Text> iterator, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {

        //read all spectra from string
        while(iterator.hasNext()){
            allSpectra.addAll(Utils.parseSpectraFromString(iterator.next().toString()));
        }
        clusterSpectra(allSpectra);

        for(Cluster cluster:clusters){
            findMatchWithinCluster(cluster,outputCollector);
        }

    }

    private void clusterSpectra(List<Spectrum> spectra){
        // group all spectra more precisely by their mass. eg. use 20ppm
        for(Spectrum spectrum:allSpectra){

            boolean isAdd = false;
            if(clusters.size()==0){
                Cluster cluster = new Cluster(spectrum,massTolerance);
                clusters.add(cluster);
                continue;
            }

            int clusterIndex = clusters.size()-1;
            Cluster cluster  = clusters.get(clusterIndex);
            if(!cluster.addToCluster(spectrum)){
                //create a new cluster
                Cluster nextCluster = new Cluster(spectrum,massTolerance);
                clusters.add(nextCluster);
            }
        }



    }

    private void findMatchWithinCluster(Cluster cluster,OutputCollector<Text, Text> outputCollector){
        Map<Long,Integer> matchedIds= new HashMap<Long,Integer>();
        List<Spectrum> spectra = cluster.getSpectra();
        for(Spectrum spectrum:spectra){
            matchedIds.put(spectrum.getSpectrumId(),0);
        }
        for(int i =0;i<spectra.size();i++){
            Spectrum spectrum = spectra.get(i);
            long Id = spectrum.getSpectrumId();
            List<Double> reporters = spectrum.getReporterMass();
            for(int j = i+1;j<spectra.size();j++){
                Spectrum other = spectra.get(j);
                long otherId = other.getSpectrumId();
                List<Double> otherFragments = other.getFragmentsMass();
                if(Utils.compareSpectrum(reporters,otherFragments,fragmentTolerance,matchRatio)){
                    matchedIds.put(Id,matchedIds.get(Id)+1);
                }
            }
        }
        try {
            for (HashMap.Entry<Long, Integer> entry : matchedIds.entrySet()) {
                outputCollector.collect(new Text(entry.getKey().toString()), new Text(entry.getValue().toString()));
            }
        }catch (IOException e){
            e.printStackTrace();
        }



    }

    public void close() throws IOException {

    }

    public void configure(JobConf jobConf) {
        fragmentTolerance = Integer.parseInt(jobConf.get("Fragment.Tolerance"));
        massTolerance = Integer.parseInt(jobConf.get("Mass.Tolerance"));
        matchRatio = Float.parseFloat(jobConf.get("Fragment.MatchRatio"));

    }
}
