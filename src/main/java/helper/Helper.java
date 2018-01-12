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
    public static String regex_url = "/^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$/";
    public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};


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

    public static ArrayList<String> stopWordRemoval(String string){
        String[] words = string.split("\\s+");
        ArrayList<String> wordsList = new ArrayList<String>();

        for(String word : words)
        {
            String wordCompare = word.toLowerCase();
            if(!new HashSet<String>(Arrays.asList(stopwords)).contains(wordCompare)
                    && !word.isEmpty())
            {
                wordsList.add(word);
            }
        }
        return wordsList;
    }

    public static ArrayList<String> getWordListFromAppDescriptions(String packageName){
        File file = new File("src/main/res/app_descriptions/"+packageName);
        ArrayList<String> words= new ArrayList<String>();
        try {
            FileReader fReader = new FileReader(file);
            BufferedReader bf = new BufferedReader(fReader);
            String line;
            while ((line=bf.readLine()) != null){
                line = line
                        .replaceAll("[^a-zA-Z ]", "")
                        .replaceAll(regex_url, "")
                        .toLowerCase();

                words.addAll(stopWordRemoval(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

}
