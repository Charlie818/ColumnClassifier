package helper;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import static java.lang.Math.max;

public class Helper {
    public static Map<String,Double> wordCost;
    public static int maxWordLength;

    private static Pair<Double, Integer> bestMatch(int idx,
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
            if(Helper.wordCost.containsKey(word))
                first=c + wordCost.get(word);
            else
                continue;
            Pair<Double,Integer> tmp=new Pair<Double, Integer>(first,second);
            if(tmp.getKey()<ret.getKey())ret=tmp;
        }
        return ret;
    }


    public static String[] splitWordsBySpecialCharacters(String line){
        return line.split("[_.\\-]");
    }

    // https://stackoverflow.com/questions/8870261/how-to-split-text-without-spaces-into-list-of-words#
    // Input: a nonstop string of consecutive words.
    // Output: a list of splitted words.
    public static ArrayList<String> splitWordsByDict(String line) {

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

        return out;
    }

    public static ArrayList<String> getWordListFromAppDescriptions(String packageName){
        File file = new File("src/main/res/app_descriptions/"+packageName);
        ArrayList<String> words= new ArrayList<String>();
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fReader);
            String line;
            while ((line=bf.readLine()) != null){
                line = line.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                words.addAll(Arrays.asList(line.split("\\s+")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

}
