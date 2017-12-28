package entity;

import java.util.Vector;

/**
 * Created by qiujiarong on 28/12/2017.
 */
public class Feature {
    Vector<Double> vec;
    int label;
    String text;

    public Feature(Vector<Double> vec, int label, String text) {
        this.vec = vec;
        this.label = label;
        this.text = text;
    }

    public Feature(Vector<Double> vec, int label) {
        this.vec = vec;
        this.label = label;
    }

    public Vector<Double> getVec() {
        return vec;
    }

    public int getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }
}
