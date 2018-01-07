package entity;

import java.util.Vector;

/**
 * Created by qiujiarong on 28/12/2017.
 */
public class Feature {
    private Vector<Double> vec;
    private int label;
    private String text;

    public Feature(Vector<Double> vec, int label, String text) {
        this.vec = vec;
        this.label = label;
        this.text = text;
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
