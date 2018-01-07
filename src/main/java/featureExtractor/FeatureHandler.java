package featureExtractor;

import entity.Feature;
import javafx.util.Pair;

import java.util.*;

import static java.lang.Math.log;
import static java.lang.Math.max;

/**
 * Created by qiujiarong on 28/12/2017.
 */
public class FeatureHandler {

    private Map<String,Double> wordCost;
    ArrayList<String> words;
    int maxWordLength;

    public FeatureHandler(){
        Map<String,Double> wordCost= new HashMap<String, Double>();
        ArrayList<String> words= CSVHandler.loadDict();
        int maxWordLength=0;
        for(int i=0;i<words.size();i++){
            String word = words.get(i);
            wordCost.put(word,log((i+1)*log(words.size())));
            if(word.length()>maxWordLength)maxWordLength=word.length();
        }
        this.maxWordLength=maxWordLength;
        this.words=words;
        this.wordCost=wordCost;

    }


    public TreeSet<String> featureExtractor() throws Exception{
        ArrayList<Pair<String,Integer>> columns=CSVHandler.loadTrain();
//        ArrayList<Pair<String,Integer>> columns=CSVHandler.loadColumns();
        TreeSet<String> features= new TreeSet<String>();
        System.out.println(columns.size());
        for(Pair<String,Integer> pair:columns){
            String column = pair.getKey();
            column=column.replaceAll("[A-Z]","_$0");
            column=column.toLowerCase();
            column=column.replaceAll("[0-9]+","_");

            for (String s : column.split("_|\\.|-")) {
                if (s.length() == 0) continue;
                if(inferSpace(s).size()>1)
                    features.addAll(inferSpace(s));
                else
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
        boolean flag=false;
        for(String key:feature2id.keySet()){
            if(!column.contains(key))continue;
            int idx=feature2id.get(key);
            flag=true;
            vec.set(idx,1.0);
        }
        if(!flag)System.out.println("unmatched column "+column);
        return vec;
    }

    public static Vector<Feature> getFeature(boolean train){
        ArrayList<Pair<String,Integer>> columns = new ArrayList<Pair<String, Integer>>();
        if(train)
            columns=CSVHandler.loadTrain();
        else
            columns=CSVHandler.loadTest();
        Map<String,Integer> feature2id=CSVHandler.loadFeatures();
        Vector<Feature> features = new Vector<Feature>();
        for(Pair<String,Integer> pair:columns){
            features.add(new Feature(getFeature(pair.getKey(),feature2id),pair.getValue(),pair.getKey()));
        }
        return features;
    }


    private Pair<Double, Integer> bestMatch(int idx,ArrayList<Double> cost,String line){
        int p=max(0,idx-this.maxWordLength);
        int q=idx;
        ArrayList<Double> candidates0= new ArrayList<Double>();
        for(int k=p;k<q;k++)
            candidates0.add(cost.get(k));
        Collections.reverse(candidates0);
        ArrayList<Pair<Integer,Double>> candidates = new ArrayList<Pair<Integer, Double>>();
        for(int i=0;i<candidates0.size();i++)
            candidates.add(new Pair<Integer, Double>(i, candidates0.get(i)));

        Pair<Double,Integer> ret= new Pair<Double, Integer>(Double.POSITIVE_INFINITY,Integer.MAX_VALUE);
        for(Pair<Integer,Double> pair : candidates){
            int k=pair.getKey();
            double c = pair.getValue();
            String word=line.substring(idx-k-1,idx);
            double first=0.0;
            int second=k+1;
            if(this.wordCost.containsKey(word))first=c+wordCost.get(word);
            else continue;
            Pair<Double,Integer> tmp=new Pair<Double, Integer>(first,second);
            if(tmp.getKey()<ret.getKey())ret=tmp;
        }
//        System.out.println(line+" "+ret.getKey()+" "+ret.getValue());
        return ret;
    }

    //https://stackoverflow.com/questions/8870261/how-to-split-text-without-spaces-into-list-of-words#
    public ArrayList<String> inferSpace(String line) {

        ArrayList<Double> cost = new ArrayList<Double>();
        cost.add(0.0);
        for (int i = 1; i < line.length() + 1; i++) {
            Pair<Double, Integer> pair = bestMatch(i, cost, line);
            cost.add(pair.getKey());
        }

        ArrayList<String> out = new ArrayList<String>();
        int i = line.length();
        while (i > 0) {
            Pair<Double, Integer> pair = bestMatch(i, cost, line);
            if(pair.getKey()==Double.POSITIVE_INFINITY)break;
            assert pair.getKey() == cost.get(i);
            out.add(line.substring(i - pair.getValue(), i));
            i -= pair.getValue();
        }

//        for (String string : out)
//            System.out.println(string);
        return out;
    }


    public static void main(String[] args) throws Exception {
        FeatureHandler featureHandler = new FeatureHandler();
        CSVHandler.writeFeatures(featureHandler.featureExtractor());

    }
}
