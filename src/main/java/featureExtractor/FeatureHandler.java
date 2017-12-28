package featureExtractor;

import entity.Feature;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by qiujiarong on 28/12/2017.
 */
public class FeatureHandler {
    public static TreeSet<String> featureExtractor() throws Exception{
        ArrayList<Pair<String,Integer>> columns=CSVHandler.loadColumns();
        TreeSet<String> features= new TreeSet<String>();
        for(Pair<String,Integer> pair:columns){
            String column = pair.getKey();
            column=column.replaceAll("[A-Z]","_$0");
            column=column.toLowerCase();
            column=column.replaceAll("[0-9]+","_");
            if(column.split("_").length==1)continue;
            for(String s :column.split("_")){
                if(s.length()==0)continue;
                features.add(s);
            }
        }
        features.comparator();
        return features;
    }

    private static Vector<Double> getFeature(String column,Map<String,Integer> feature2id){
        Vector<Double> vec = new Vector<Double>();
        for(int i=0;i<feature2id.size();i++)
            vec.add(i,0.0);
        for(String key:feature2id.keySet()){
            if(!column.contains(key))continue;
            int idx=feature2id.get(key);
            vec.set(idx,1.0);
        }
        return vec;
    }
    public static Vector<Feature> getFeature(){
        ArrayList<Pair<String,Integer>> columns=CSVHandler.loadColumns();
        Map<String,Integer> feature2id=CSVHandler.loadFeatures();
        Vector<Feature> features = new Vector<Feature>();
        for(Pair<String,Integer> pair:columns){
            features.add(new Feature(getFeature(pair.getKey(),feature2id),pair.getValue(),pair.getKey()));
        }
        return features;
    }
    public static void main(String[] args) throws Exception {
        CSVHandler.writeFeatures(FeatureHandler.featureExtractor());

    }
}
