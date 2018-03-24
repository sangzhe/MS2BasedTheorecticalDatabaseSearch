package com.shi.pitt.MS2Search;

import java.util.*;

/**
 * Created by sangzhe on 2018/3/23.
 */
public class Cluster {
    private String clusterId;
    private double mass;
    private List<Spectrum> spectra;
    private Set<String> spectraIds;
    private double absErrorRange;

    public Cluster(Spectrum spectrum,double tolerance){
        this.clusterId = UUID.randomUUID().toString();
        this.spectra = new ArrayList<Spectrum>();
        this.spectraIds = new HashSet<String>();
        mass = spectrum.getMass();
        spectra.add(spectrum);
        spectraIds.add(spectrum.getSpectrumId());
        calculateTolerance(tolerance);
    }

    private void calculateTolerance(double tolerance){
        absErrorRange  = tolerance*this.mass;
    }
    public boolean addToCluster(Spectrum spectrum){
        if(Math.abs(mass-spectrum.getMass())<absErrorRange){
            spectra.add(spectrum);
            spectraIds.add(spectrum.getSpectrumId());
            return true;
        }
        return false;

    }

    public String getClusterId() {
        return clusterId;
    }

    public double getMass() {
        return mass;
    }

    public List<Spectrum> getSpectra() {
        return spectra;
    }

    public Set<String> getSpectraIds() {
        return spectraIds;
    }
}
