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

    public static ArrayList<Pair<String,Integer>> loadColumns(){
        String filename="src/main/resources/ColumnName.csv";
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
                columns.add(new Pair<String, Integer>(column,label));
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    public static Map<String,Integer> loadFeatures(){
        String filename="src/main/resources/Features.csv";
        File file = new File(filename);
        FileReader fReader = null;
        Map<String,Integer> feature2id = new HashMap<String, Integer>();
        try {
            fReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fReader);
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
        Writer writer = null;
        try {
            writer = new FileWriter(fw);
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
        FileReader fReader = null;
        try {
            fReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fReader);
            String line=null;
            while ((line=bf.readLine()) != null){
                dict.add(line);
//                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dict;
    }

    public static void main(String[] args) throws Exception {
//        textRegularization();
        loadDict();
    }
}
