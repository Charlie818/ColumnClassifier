package lda;

import au.com.bytecode.opencsv.CSVReader;
import helper.Helper;
import helper.Stemmer;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * a set of documents
 * Each app denotes one document.
 *
 * @author hankcs
 */
public class Corpus
{
    List<int[]> documentList;
    Vocabulary vocabulary;
    Helper helper;

    public Corpus()
    {
        documentList = new LinkedList<int[]>();
        vocabulary = new Vocabulary();
        helper = new Helper();
    }

    public int[] addDocument(List<String> document)
    {
        int[] doc = new int[document.size()];
        int i = 0;
        for (String word : document)
        {
            doc[i++] = vocabulary.getId(word, true);
        }
        documentList.add(doc);
        return doc;
    }

    public int[][] toArray()
    {
        return documentList.toArray(new int[0][]);
    }

    public int getVocabularySize()
    {
        return vocabulary.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        for (int[] doc : documentList)
        {
            sb.append(Arrays.toString(doc)).append("\n");
        }
        sb.append(vocabulary);
        return sb.toString();
    }

    /**
     * Load documents from app databases
     *
     * @param filePath is the dataset file, which contains all app database namings.
     * @return a corpus
     * @throws IOException
     */

    public static Corpus load(String filePath) throws IOException
    {
        Corpus corpus = new Corpus();
        File file = new File(filePath);
        CSVReader csvReader = new CSVReader(new FileReader(file));
        List<String[]> instances = csvReader.readAll();
        String lastPackageName = "";
        String packageName;
        String tableName;
        String columnName;
        TreeSet<String> wordList = new TreeSet<String>();

        // A document here consists of
        // all the column names and table names.

        for(String[] instance : instances){
            packageName = instance[0];
            tableName = instance[1].replaceAll("[0-9]+","");
            columnName = instance[2].replaceAll("[0-9]+","");

            if(!packageName.equals(lastPackageName) && !lastPackageName.isEmpty()){
                System.out.println(Arrays.toString(wordList.toArray()));
//                corpus.addDocument(wordList);
                wordList.clear();
            }

            String[] splits = Helper.splitWordsBySpecialCharacters(columnName);
            for (String split: splits){
                for(String word: Helper.splitWordsByDict(split)){
                    wordList.add(new Stemmer().stem(word));
                }

            }
            for(String word: Helper.splitWordsByDict(tableName)){
                wordList.add(new Stemmer().stem(word));
            }

            lastPackageName = packageName;

//            myTreeSet.toArray(new String[myTreeSet.size()]);

        }
        if (corpus.getVocabularySize() == 0) return null;

        return corpus;
    }

//    public static Corpus load(String folderPath) throws IOException
//    {
//        Corpus corpus = new Corpus();
//        File folder = new File(folderPath);
//        for (File file : folder.listFiles())
//        {
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//            String line;
//            List<String> wordList = new LinkedList<String>();
//            while ((line = br.readLine()) != null)
//            {
//                String[] words = line.split(" ");
//                for (String word : words)
//                {
//                    if (word.trim().length() < 2) continue;
//                    wordList.add(word);
//                }
//            }
//            br.close();
//            corpus.addDocument(wordList);
//        }
//        if (corpus.getVocabularySize() == 0) return null;
//
//        return corpus;
//    }

    public Vocabulary getVocabulary()
    {
        return vocabulary;
    }

    public int[][] getDocument()
    {
        return toArray();
    }

    public static int[] loadDocument(String path, Vocabulary vocabulary) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        List<Integer> wordList = new LinkedList<Integer>();
        while ((line = br.readLine()) != null)
        {
            String[] words = line.split(" ");
            for (String word : words)
            {
                if (word.trim().length() < 2) continue;
                Integer id = vocabulary.getId(word);
                if (id != null)
                    wordList.add(id);
            }
        }
        br.close();
        int[] result = new int[wordList.size()];
        int i = 0;
        for (Integer integer : wordList)
        {
            result[i++] = integer;
        }
        return result;
    }
}