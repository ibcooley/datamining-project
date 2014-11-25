package AlgorithmObjects.Shared;

import java.util.List;

/**
 * Created by craig on 11/14/14.
 */
public class Association {

    private List<String> A;
    private List<String> B;
    private double support;
    private double confidence;
    private double correlation;

    public Association(List<String> A, List<String> B, double support, double confidence, double correlation) {
        setA(A);
        setB(B);
        setSupport(support);
        setConfidence(confidence);
        setCorrelation(correlation);
    }

    public List<String> getA() {
        return A;
    }

    public void setA(List<String> a) {
        A = a;
    }

    public List<String> getB() {
        return B;
    }

    public void setB(List<String> b) {
        B = b;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }
}
