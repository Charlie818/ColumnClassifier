package featureExtractor;

import au.com.bytecode.opencsv.CSVReader;
import com.google.common.collect.ImmutableList;
import com.medallia.word2vec.Searcher;
import entity.Feature;
import helper.Helper;
import javafx.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static featureExtractor.Loader.word2VecModel;


/**
 * Created by qiujiarong on 28/12/2017.
 */
public class FeatureHandler {


    private static ArrayList<Double> getWord2VecMean(int vecLength, TreeSet<String> wordList) {

        int n_words = wordList.size();

        ArrayList<Double> word2vec_mean = new ArrayList<Double>(vecLength);
        for (int i = 0; i < vecLength; i++) {
            word2vec_mean.add(0.0);
        }

        for (int i=0; i< vecLength; i++){
            for (String word: wordList){
                List<Double> vector;
                try {
                    vector = word2VecModel.forSearch().getRawVector(word);
                } catch (Searcher.UnknownWordException e) {
                    Double[] x = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                    vector = Arrays.asList(x);
                    e.printStackTrace();
                }
                word2vec_mean.set(i, word2vec_mean.get(i)+ vector.get(i));
            }
            word2vec_mean.set(i, word2vec_mean.get(i)/n_words);
        }

        return word2vec_mean;
    }

    public static void generateFeatures(String filePath) throws IOException, Searcher.UnknownWordException {
        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        List<String[]> instances = csvReader.readAll();
        String packageName;
        String tableName;
        String columnName;
        int vecLength = -1;
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
                    wordList.add(word);
                    lastWord = word;
                }

            }

            if (vecLength ==-1){
                vecLength = word2VecModel.forSearch().getRawVector(wordList.first()).size();
            }

            System.out.println("Feat1:"+Arrays.toString(wordList.toArray()));
            System.out.println("Feat2:"+lastWord);
            System.out.println("Feat3:"+Arrays.toString(getWord2VecMean(vecLength, wordList).toArray()) );

            System.out.println("-------");
//            myTreeSet.toArray(new String[myTreeSet.size()]);

        }


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


    public static void main(String[] args) throws IOException {


        Loader.load();
        try{
            FeatureHandler.generateFeatures("src/main/res/dataset.csv");
        }
        catch (Searcher.UnknownWordException exception){
            System.out.println(exception.getMessage());
        }

    }
}
