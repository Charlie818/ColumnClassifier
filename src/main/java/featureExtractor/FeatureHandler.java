package featureExtractor;

import au.com.bytecode.opencsv.CSVReader;
import com.medallia.word2vec.Searcher;
import com.medallia.word2vec.Word2VecModel;
import com.medallia.word2vec.util.Common;
import entity.Feature;
import helper.Helper;
import helper.Stemmer;
import javafx.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * Created by qiujiarong on 28/12/2017.
 */
public class FeatureHandler {

    public static void generateFeatures(String filePath) throws IOException{
        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        List<String[]> instances = csvReader.readAll();
        String packageName;
        String tableName;
        String columnName;
        for(String[] instance : instances){
            packageName = instance[0];
            tableName = instance[1].replaceAll("[0-9]+","");
            columnName = instance[2].replaceAll("[0-9]+","");
            System.out.println(packageName+"|"+tableName+"|"+columnName);
            TreeSet<String> wordList = new TreeSet<String>();

            String[] splits = Helper.splitWordsBySpecialCharacters(columnName);
            String lastWord = "";
            for (String split: splits){
                for(String word: Helper.splitWordsByDict(split)){
                    wordList.add(new Stemmer().stem(word));
                    lastWord = new Stemmer().stem(word);
                }

            }
            System.out.println("Feat1:"+Arrays.toString(wordList.toArray()));
            System.out.println("Feat2:"+lastWord);
            System.out.println("-------");
//            myTreeSet.toArray(new String[myTreeSet.size()]);

        }


    }

    public TreeSet<String> featureExtractor(){
        ArrayList<Pair<String, Integer>> trainingData = Loader.trainingData;
        TreeSet<String> features= new TreeSet<String>();
        for(Pair<String,Integer> pair:trainingData){
            String instance = pair.getKey();

            System.out.println(instance);
            instance = instance.replaceAll("[0-9]+","");
            TreeSet<String> wordList = new TreeSet<String>();
            for (String split : Helper.splitWordsBySpecialCharacters(instance)){
                ArrayList<String> furtherSplits = Helper.splitWordsByDict(split);
                if (furtherSplits.size() == 0){
                    wordList.add(new Stemmer().stem(split));
                }
                for (String furtherSplit: furtherSplits){
                    wordList.add(new Stemmer().stem(furtherSplit));
                }


            }
            System.out.println(Arrays.toString(wordList.toArray()));

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


    public static void main(String[] args) throws IOException{
        String filename = "src/main/res/word2vec.c.output.model.txt";
        Word2VecModel word2VecModel = Word2VecModel.fromTextFile(new File(filename));
        try {
            System.out.println(word2VecModel.forSearch().cosineDistance("three", "five"));
        } catch (Searcher.UnknownWordException e) {
            e.printStackTrace();
        }

//        Loader.load();
//        try {
//            FeatureHandler.generateFeatures("src/main/res/dataset.csv");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
