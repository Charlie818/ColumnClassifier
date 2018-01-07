package featureExtractor;
import java.io.*;
import java.util.*;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import javafx.util.Pair;

/**
 * Created by qiujiarong on 27/12/2017.
 */
public class CSVHandler {

    public static void textRegularization() throws Exception{
        String filename="src/main/resources/PocketFindingsTaxonomy.csv";
        String filename2="src/main/resources/ColumnName.csv";
        File file = new File(filename);
        File fw = new File(filename2);
        Writer writer = new FileWriter(fw);
        CSVWriter csvWriter = new CSVWriter(writer, ',',CSVWriter.NO_QUOTE_CHARACTER);
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        List<String[]> list = csvReader.readAll();
        ArrayList<String> semantics = new ArrayList<String>();
        ArrayList<String> others = new ArrayList<String>();
        for(String[] ss : list){
            String semantic=ss[0];
            String other=ss[1];
            semantics.add(semantic);
            others.add(other);
        }
        csvReader.close();
        for(String str:semantics){
            csvWriter.writeNext(new String[]{str,"1"});
        }
        for(String str:others){
            csvWriter.writeNext(new String[]{str,"0"});
        }
        csvWriter.close();
    }

    public static void trainTestSplit() throws Exception{
        String filename="src/main/resources/PocketFindingsTaxonomy2.csv";
        File file = new File(filename);
        FileReader fReader = new FileReader(file);
        CSVReader csvReader = new CSVReader(fReader);
        List<String[]> list = csvReader.readAll();
        ArrayList<Pair<String,Integer>> train = new ArrayList<Pair<String, Integer>>();
        ArrayList<Pair<String,Integer>> test = new ArrayList<Pair<String, Integer>>();
        int cnt=0;
        for(String[] ss : list){
            String packageName=ss[0];
            String tableName=ss[1];
            String columnName=ss[2];
            int label=0;
            try {
                if(Integer.parseInt(ss[3])==1)
                    label=1;
            }catch (Exception e){

            }

            if(cnt%5==0)test.add(new Pair<String, Integer>(packageName+"_"+tableName+"_"+columnName,label));
            else train.add(new Pair<String, Integer>(packageName+"_"+tableName+"_"+columnName,label));
            cnt++;
        }
        csvReader.close();
        String filename2="src/main/resources/ColumnName2.csv";
        File fw = new File(filename2);
        Writer writer = new FileWriter(fw);
        CSVWriter csvWriter = new CSVWriter(writer, ',',CSVWriter.NO_QUOTE_CHARACTER);
        for(Pair<String,Integer> pair:train){
            csvWriter.writeNext(new String[]{pair.getKey(),pair.getValue().toString(),"0"});
        }
        for(Pair<String,Integer> pair:test){
            csvWriter.writeNext(new String[]{pair.getKey(),pair.getValue().toString(),"1"});
        }
        csvWriter.close();
    }

    public static ArrayList<Pair<String,Integer>> loadColumns(){
        File file = new File("src/main/resources/ColumnName.csv");
        ArrayList<Pair<String,Integer>> columns= new ArrayList<Pair<String, Integer>>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            List<String[]> list = csvReader.readAll();
            for(String[] ss : list){
                String column= ss[0];
                int label =Integer.parseInt(ss[1]);
                columns.add(new Pair<String, Integer>(column,label));
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }


    public static ArrayList<Pair<String,Integer>> loadTrain(){
        String filename="src/main/resources/ColumnName2.csv";
        File file = new File(filename);
        FileReader fReader = null;
        ArrayList<Pair<String,Integer>> columns= new ArrayList<Pair<String, Integer>>();
        try {
            fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
            List<String[]> list = csvReader.readAll();
            for(String[] ss : list){
                String column=ss[0];
                int label =Integer.parseInt(ss[1]);
                int train = Integer.parseInt(ss[2]);
//                System.out.println(train);
                if(train==1)continue;
                columns.add(new Pair<String, Integer>(column,label));
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(columns.size());
        return columns;
    }
    public static ArrayList<Pair<String,Integer>> loadTest(){
        String filename="src/main/resources/ColumnName2.csv";
        File file = new File(filename);
        FileReader fReader = null;
        ArrayList<Pair<String,Integer>> columns= new ArrayList<Pair<String, Integer>>();
        try {
            fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
            List<String[]> list = csvReader.readAll();
            for(String[] ss : list){
                String column=ss[0];
                int label =Integer.parseInt(ss[1]);
                int train = Integer.parseInt(ss[2]);
                if(train==0)continue;
                columns.add(new Pair<String, Integer>(column,label));
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    public static Map<String,Integer> loadFeatures(){
        File file = new File("src/main/resources/Features.csv");
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
        String filename="src/main/resources/Features.csv";
        File fw = new File(filename);
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
        String filename="src/main/resources/WordsByFrequency.txt";
        File file = new File(filename);
        ArrayList<String> dict= new ArrayList<String>();
        FileReader fReader;
        try {
            fReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fReader);
            String line;
            while ((line=bf.readLine()) != null){
                dict.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    public static void main(String[] args) throws Exception{
//        textRegularization();
//        loadDict();
        trainTestSplit();
        loadTrain();
    }
}
