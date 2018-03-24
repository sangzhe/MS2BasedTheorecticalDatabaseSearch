package com.shi.pitt.Hadoop.reducer;

import com.shi.pitt.Hadoop.key.IntegerPrecursorMassKey;
import com.shi.pitt.MS2Search.Cluster;
import com.shi.pitt.MS2Search.Spectrum;
import com.shi.pitt.MS2Search.Utils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class SpectraGroupComparisonReducer extends Reducer<IntegerPrecursorMassKey,Text,Text,Text> {
    private List<Spectrum> allSpectra = new ArrayList<Spectrum>();
    private List<Cluster> clusters = new ArrayList<Cluster>();
    private int fragmentTolerance = 20;
    private int massTolerance = 20;
    private float matchRatio = (float)0.5;


    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
//        fragmentTolerance = Integer.parseInt(context.getConfiguration().get("Fragment.Tolerance"));
//        massTolerance = Integer.parseInt(context.getConfiguration().get("Mass.Tolerance"));
//        matchRatio = Float.parseFloat(context.getConfiguration().get("Fragment.MatchRatio"));
    }

    @Override
    protected void reduce(IntegerPrecursorMassKey key, Iterable<Text> value, Context context) throws IOException, InterruptedException {
        allSpectra.clear();
        clusters.clear();

        Iterator<Text> iterator = value.iterator();
        //read all spectra from string
        while(iterator.hasNext()){
            allSpectra.addAll(Utils.parseSpectraFromString(iterator.next().toString()));
        }
        clusterSpectra(allSpectra);


        try {
            for (Cluster cluster : clusters) {
                findMatchWithinCluster(cluster, context);
            }
        }catch (Exception e){
            e.printStackTrace();
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

    private void findMatchWithinCluster(Cluster cluster,Context context) throws Exception{
        Map<String,Integer> matchedIds= new HashMap<String,Integer>();
        List<Spectrum> spectra = cluster.getSpectra();
        if(spectra.size() == 1){
            context.write(new Text(spectra.get(0).getSpectrumId()), new Text("0"));
        }
        for(Spectrum spectrum:spectra){
            matchedIds.put(spectrum.getSpectrumId(),0);
        }
        for(int i =0;i<spectra.size();i++){
            Spectrum spectrum = spectra.get(i);
            String Id = spectrum.getSpectrumId();
            List<Double> reporters = spectrum.getReporterMass();
            for(int j = i+1;j<spectra.size();j++){
                Spectrum other = spectra.get(j);
                String otherId = other.getSpectrumId();
                List<Double> otherFragments = other.getFragmentsMass();
                if(Utils.compareSpectrum(reporters,otherFragments,fragmentTolerance,matchRatio)){
                    matchedIds.put(Id,matchedIds.get(Id)+1);
                }
            }
        }
        for (HashMap.Entry<String, Integer> entry : matchedIds.entrySet()) {
            context.write(new Text(entry.getKey()), new Text(entry.getValue().toString()));
        }



    }


}
