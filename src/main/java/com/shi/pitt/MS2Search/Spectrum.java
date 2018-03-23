package com.shi.pitt.MS2Search;

import java.util.List;

/**
 * Created by sangzhe on 2018/3/2.
 */
public class Spectrum {
    private long spectrumId;
    private double Mass;
    private List<Double> reporterMass;
    private List<Double> fragmentsMass;

    public Spectrum(long spectrumId, double mass, List<Double> reporterMass, List<Double> fragmentsMass) {
        this.spectrumId = spectrumId;
        this.Mass = mass;
        this.reporterMass = reporterMass;
        this.fragmentsMass = fragmentsMass;
    }

    public long getSpectrumId() {
        return spectrumId;
    }

    public double getMass() {
        return Mass;
    }

    public List<Double> getReporterMass() {
        return reporterMass;
    }

    public List<Double> getFragmentsMass() {
        return fragmentsMass;
    }

    @Override
    public String toString() {
        return spectrumId + "|"+Mass +"|" + List2String(reporterMass)+"|"+List2String(fragmentsMass);
    }

    public String List2String(List<Double> list){
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<list.size()-1;i++){
            builder.append(list.get(0)+",");
        }
        builder.append(list.get(list.size()-1));
        return builder.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spectrum)) return false;

        Spectrum spectrum = (Spectrum) o;

        if (spectrumId != spectrum.spectrumId) return false;
        return Double.compare(spectrum.Mass, Mass) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (spectrumId ^ (spectrumId >>> 32));
        temp = Double.doubleToLongBits(Mass);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
