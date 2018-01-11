package featureExtractor;
import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.medallia.word2vec.Word2VecModel;
import helper.Helper;
import javafx.util.Pair;
import org.apache.commons.lang3.math.NumberUtils;

import static java.lang.Math.log;

/**
 * Created by qiujiarong on 27/12/2017.
 */
public class Loader {

    static ArrayList<Pair<String,Integer>> trainingData;
    static ArrayList<Pair<String,Integer>> testingData;
    static Word2VecModel word2VecModel;

    private static void prepareData() throws Exception{
        File file = new File("src/main/res/dataset.csv");
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List<String[]> list = csvReader.readAll();
        ArrayList<Pair<String,Integer>> train = new ArrayList<Pair<String, Integer>>();
        ArrayList<Pair<String,Integer>> test = new ArrayList<Pair<String, Integer>>();
        int cnt=0;
        for(String[] ss : list){
            String packageName=ss[0];
            String tableName=ss[1];
            String columnName=ss[2];
            int label = NumberUtils.toInt(ss[3], 0);
            if(cnt % 5 == 0)
                test.add(new Pair<String, Integer>(packageName+"_"+tableName+"_"+columnName,label));
            else
                train.add(new Pair<String, Integer>(packageName+"_"+tableName+"_"+columnName,label));

            cnt++;
        }
        csvReader.close();

        CSVWriter csvWriter = new CSVWriter(new FileWriter(new File("src/main/res/processed_dataset.csv")), ',',CSVWriter.NO_QUOTE_CHARACTER);
        for(Pair<String,Integer> pair:train){
            csvWriter.writeNext(new String[]{pair.getKey(),pair.getValue().toString(),"1"});
        }

        for(Pair<String,Integer> pair:test){
            csvWriter.writeNext(new String[]{pair.getKey(),pair.getValue().toString(),"0"});
        }
        csvWriter.close();
    }

    public static void load() throws IOException{

        String filename = "src/main/res/word2vec.c.output.model.txt";
        word2VecModel = Word2VecModel.fromTextFile(new File(filename));


        Map<String,Double> wordCost= new HashMap<String, Double>();
        ArrayList<String> words= Loader.loadDict();
        int maxWordLength=0;
        for(int i=0;i<words.size();i++){
            String word = words.get(i);
            wordCost.put(word,log((i+1)*log(words.size())));
            if(word.length()>maxWordLength)maxWordLength=word.length();
        }
        Helper.maxWordLength =maxWordLength;
        Helper.wordCost =wordCost;

        trainingData= new ArrayList<Pair<String, Integer>>();
        testingData= new ArrayList<Pair<String, Integer>>();

        CSVReader csvReader = new CSVReader(new FileReader(new File("src/main/res/processed_dataset.csv")));
        List<String[]> list = csvReader.readAll();
        for(String[] ss : list){
            String column=ss[0];
            int label = NumberUtils.toInt(ss[1], -1);
            int train = NumberUtils.toInt(ss[2], -1);
            if(train == 1)
                trainingData.add(new Pair<String, Integer>(column,label));
            else if (train == 0)
                testingData.add(new Pair<String, Integer>(column,label));
        }
        csvReader.close();


    }


    public static Map<String,Integer> loadFeatures(){
        File file = new File("src/main/res/features.csv");
        Map<String,Integer> feature2id = new HashMap<String, Integer>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            List<String[]> list = csvReader.readAll();
            for(String[] ss : list){
                String feature=ss[0];
                int label =Integer.parseInt(ss[1]);
                feature2id.put(feature,label);
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feature2id;
    }

    public static void writeFeatures(Set<String> features){
        File fw = new File("src/main/res/features.csv");
        try {
            Writer writer = new FileWriter(fw);
            CSVWriter csvWriter = new CSVWriter(writer, ',',CSVWriter.NO_QUOTE_CHARACTER);
            int idx=0;
            for(String s:features){
                csvWriter.writeNext(new String[]{s,Integer.toString(idx)});
                idx++;
            }
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> loadDict(){
        File file = new File("src/main/res/enwiki_vocab_min200.txt");
        ArrayList<String> dict= new ArrayList<String>();
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fReader);
            String line;
            while ((line=bf.readLine()) != null){
                dict.add(line.split(" ")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    public static void main(String[] args) throws Exception{
        prepareData();
    }
}
