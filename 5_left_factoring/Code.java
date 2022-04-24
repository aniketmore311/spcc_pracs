import java.io.*;
import java.util.*;

public class Code{
    public static void main(String[] args) throws IOException {
        //List<String> input = readLines(new BufferedReader(new InputStreamReader(System.in)));
        List<String> input = readLines(new BufferedReader(new FileReader("input.txt")));
        Map<String,List<String>> productions = parseInput(input);
        Eliminator el = new Eliminator(productions);
        el.eliminate();
        el.printOutput();
    }
    public static Map<String, List<String>> parseInput(List<String> input){
        Map<String, List<String>> productions = new HashMap<>();
        for(String line: input){
            String left = line.split("->")[0].trim();
            String rulesStr = line.split("->")[1];
            String[] rulesArr = rulesStr.split("\\|");
            for(String rule: rulesArr){
                if(!productions.containsKey(left)){
                    productions.put(left,new ArrayList<String>());
                }
                productions.get(left).add(rule.trim());
            }
        }
        return productions;
    }
    
    public static List<String> readLines(BufferedReader br) throws IOException{
        List<String> input = new ArrayList<String>();
        String line = br.readLine();
        while(line != null){
            input.add(line.trim());
            line = br.readLine();
        }
        return input;
    }

}

class Eliminator{
    public Map<String,List<String>> inputProductions;
    public Map<String,List<String>> outputProductions = new HashMap<>();
    
    Eliminator(Map<String,List<String>> productions){
        this.inputProductions = productions;
    }
    
    public void eliminate(){
        for(String left: inputProductions.keySet()){
            List<String> rules = inputProductions.get(left);
            // to impl
            List<List<String>> duplicatesList = getDuplicatesList(left);
            // if just single value means no duplicate add to output
            for(List<String> duplicates: duplicatesList){
                if(duplicates.size() == 1){
                    addToOutput(left, duplicates.get(0));
                }
            }
            for(List<String> duplicates: duplicatesList){
                // to impl
                if(duplicates.size() > 1){
                    Map<String,List<String>> newProductions = removeFactoring(left, duplicates);
                    for(String newLeft: newProductions.keySet()){
                        for(String newRule: newProductions.get(newLeft)){
                            addToOutput(newLeft, newRule);
                        }
                    }   
                }
                 
            }
            
        }        
    }
    
    public List<List<String>> getDuplicatesList(String left){
        List<String> rules = inputProductions.get(left);
        List<List<String>> ans = new ArrayList<List<String>>();
        int len = rules.size();
        boolean[] visited = new boolean[len];
        for(int i=0;i<len;i++){
            if(visited[i]) continue;
            visited[i] = true;
            String rule = rules.get(i);
            String repeatingPart = rule.substring(0,1);
            List<String> localAns = new ArrayList<String>();
            localAns.add(rule);
            for(int j=i+1;j<len;j++){
                if(rules.get(j).startsWith(repeatingPart)){
                    //System.out.println(i);
                    //System.out.println(j);
                    //System.out.println(repeatingPart);
                    visited[j] = true;
                    localAns.add(rules.get(j));
                }
            }
            ans.add(localAns);
        }
        return ans;
    }
    
    public Map<String,List<String>> removeFactoring(String left, List<String> duplicates){
        Map<String, List<String>> newProductions = new HashMap<String, List<String>>();
        String repeatingPart = duplicates.get(0).substring(0,1);
        // A -> aA'
        // A' -> b1|b2|b3
        newProductions.put(left, new ArrayList<String>());
        newProductions.put(left+"'", new ArrayList<String>());
        newProductions.get(left).add(repeatingPart + left + "'");
        for(String duplicate: duplicates){
            String nonRepeatingPart = duplicate.substring(1,duplicate.length());
            newProductions.get(left+"'").add(nonRepeatingPart);
        }
        return newProductions;
    }
    
    public void addToOutput(String left, String rule){
        if(!outputProductions.containsKey(left)){
            outputProductions.put(left, new ArrayList<String>());
        }
        outputProductions.get(left).add(rule);
    }
    
    public void printOutput(){
        for(String left: outputProductions.keySet()){
            List<String> rules = outputProductions.get(left);
            for(String rule: rules){
                System.out.println(left +" -> " + rule);
            }
        }
    }

    
}
