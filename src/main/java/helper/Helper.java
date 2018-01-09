package helper;

import featureExtractor.Loader;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.log;
import static java.lang.Math.max;

public class Helper {
    private Map<String,Double> wordCost;
    private int maxWordLength;

    public Helper(){
        Map<String,Double> wordCost= new HashMap<String, Double>();
        ArrayList<String> words= Loader.loadDict();
        int maxWordLength=0;
        for(int i=0;i<words.size();i++){
            String word = words.get(i);
            wordCost.put(word,log((i+1)*log(words.size())));
            if(word.length()>maxWordLength)maxWordLength=word.length();
        }
        this.maxWordLength=maxWordLength;
        this.wordCost=wordCost;

    }

    private Pair<Double, Integer> bestMatch(int idx,
                                            ArrayList<Double> cost,
                                            String line){
        int p=max(0,idx- maxWordLength);

        ArrayList<Double> candidates0= new ArrayList<Double>();
        for(int k = p; k< idx; k++)
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
            double first;
            int second=k+1;
            if(this.wordCost.containsKey(word))
                first=c + wordCost.get(word);
            else
                continue;
            Pair<Double,Integer> tmp=new Pair<Double, Integer>(first,second);
            if(tmp.getKey()<ret.getKey())ret=tmp;
        }
        return ret;
    }


    //https://stackoverflow.com/questions/8870261/how-to-split-text-without-spaces-into-list-of-words#
    public ArrayList<String> splitWords(String line) {
        System.out.println(line);
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
            assert pair.getKey().equals(cost.get(i));
            out.add(line.substring(i - pair.getValue(), i));
            i -= pair.getValue();
        }

        for (String string : out)
            System.out.println(string);
        return out;
    }
}
