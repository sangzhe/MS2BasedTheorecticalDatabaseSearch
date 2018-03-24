package com.shi.pitt.MS2Search;

import java.util.List;
import java.util.UUID;

/**
 * Created by sangzhe on 2018/3/2.
 */
public class Spectrum {
    private String spectrumId;
    private double Mass;
    private List<Double> reporterMass;
    private List<Double> fragmentsMass;

    public Spectrum(){
        this.spectrumId = UUID.randomUUID().toString();
    }

    public Spectrum(String spectrumId, double mass, List<Double> reporterMass, List<Double> fragmentsMass) {
        this.spectrumId = spectrumId;
        this.Mass = mass;
        this.reporterMass = reporterMass;
        this.fragmentsMass = fragmentsMass;
    }


    public void setMass(double mass) {
        Mass = mass;
    }

    public void setReporterMass(List<Double> reporterMass) {
        this.reporterMass = reporterMass;
    }

    public void setFragmentsMass(List<Double> fragmentsMass) {
        this.fragmentsMass = fragmentsMass;
    }

    public String getSpectrumId() {
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
        result = spectrumId.hashCode();
        temp = Double.doubleToLongBits(Mass);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
