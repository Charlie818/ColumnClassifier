package classifier;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

class Vector {
    private final ArrayList<Double> value;

    public ArrayList<Double> asList(){
        return value;
    }

    public Vector(ImmutableList<Double> params) {
        value = new ArrayList<Double>();
        value.addAll(params);
    }

    public Vector(ArrayList<Double> params) {
        value = new ArrayList<Double>();
        value.addAll(params);
    }

    public Vector add(Vector other) {
        ArrayList<Double> sum = new ArrayList<Double>(other.asList().size());

        for (int i=0; i< other.asList().size(); i++){
            sum.set(i, other.asList().get(i) + asList().get(i));
        }
        return new Vector(sum);
    }


}