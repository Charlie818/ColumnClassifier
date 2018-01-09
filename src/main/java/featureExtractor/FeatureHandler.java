package featureExtractor;

import entity.Feature;
import helper.Helper;
import javafx.util.Pair;

import java.util.*;


/**
 * Created by qiujiarong on 28/12/2017.
 */
public class FeatureHandler {
    private Helper helper;

    FeatureHandler(){
        helper = new Helper();

    }

    public TreeSet<String> featureExtractor(){
        ArrayList<Pair<String,Integer>> trainingData = Loader.trainingData;
        TreeSet<String> features= new TreeSet<String>();
        for(Pair<String,Integer> pair:trainingData){
            String instance = pair.getKey();
            instance = instance.replaceAll("[A-Z]","_$0");
            instance = instance.toLowerCase();
            instance = instance.replaceAll("[0-9]+","_");

            System.out.println("after:"+instance);
            System.out.println("-----");
            System.out.println(Arrays.toString(instance.split("[_.\\-]")));
            for (String s : instance.split("[_.\\-]")) {
                if (s.length() == 0) continue;
                if(helper.splitWords(s).size()>1) {
                    features.addAll(helper.splitWords(s));

                    System.out.println(helper.splitWords(s));
                }
                else{
                    features.add(s);
                    System.out.println(s);
                }
                System.out.println("features.size():"+features.size());
            }
            System.out.println("*******");

        }

        features.comparator();

        return features;
    }

    private static Vector<Double> getFeature(String column,Map<String,Integer> feature2id){
        Vector<Double> vec = new Vector<Double>();
        for(int i=0;i<feature2id.size();i++)
            vec.add(i,0.0);
        boolean flag=false;
        for(String key:feature2id.keySet()){
            if(!column.contains(key))
                continue;
            int idx=feature2id.get(key);
            flag=true;
            vec.set(idx,1.0);
        }
        if(!flag)System.out.println("unmatched column "+column);
        return vec;
    }

    public static Vector<Feature> getFeature(boolean train){
        ArrayList<Pair<String,Integer>> columns;

        if(train)
            columns = Loader.trainingData;
        else
            columns = Loader.testingData;

        Map<String,Integer> feature2id = Loader.loadFeatures();
        Vector<Feature> features = new Vector<Feature>();

        for(Pair<String,Integer> pair:columns){
            features.add(new Feature(getFeature(pair.getKey(),
                    feature2id),
                    pair.getValue(),
                    pair.getKey()));
        }

        return features;
    }


    public static void main(String[] args) {
        Loader.load();
        Loader.writeFeatures(new FeatureHandler().featureExtractor());
    }
}
