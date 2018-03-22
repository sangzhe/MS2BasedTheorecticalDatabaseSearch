package com.shi.pitt.MS2Search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sangzhe on 2018/3/2.
 */
public class Utils {

    public static boolean compareMass(double Mass1,double Mass2,int tolerance){
        double bigger =Mass1;
        if(Mass1 < Mass2){
            bigger = Mass2;
        }
        double diff = Math.pow(10,6) * (Mass1-Mass2)/bigger;

        if (Math.abs(diff)<=tolerance)
            return true;
        return false;
    }


    public static boolean compareSpectrum(List<Double> spectrum1, List<Double> spectrum2, int tolerance, float matchRatio){
        int HitNumber=0;
        int BaseNumber = spectrum1.size();
        for(double fragment1:spectrum1){
            for(double fragment2:spectrum2){
                if(compareMass(fragment1,fragment2,tolerance))
                    HitNumber++;
            }
        }
        return HitNumber>matchRatio*BaseNumber;
//        return HitNumber>=2;
    }

    public static Spectrum parseSpectrumFromString(String spectrumStr){
        String[] fields = spectrumStr.split("|");
        Long spectrumId = Long.parseLong(fields[0]);
        Double mass = Double.parseDouble(fields[1]);
        List<Double> reporters = new ArrayList<Double>();
        for(String r:fields[2].split(",")){
            reporters.add(Double.parseDouble(r));
        }
        List<Double> fragments = new ArrayList<Double>();
        for(String f:fields[3].split(",")){
            fragments.add(Double.parseDouble(f));
        }
        return new Spectrum(spectrumId,mass,reporters,fragments);
    }

    public static Map<Long,List<Long>> readClusterInfoFromString(String str){
        return null;
    }
}
