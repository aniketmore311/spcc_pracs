import java.util.*;
import java.io.*;

public class Code{
    public static void main(String[] args)throws IOException {
        // List<String> input = readLines(new BufferedReader(new InputStreamReader(System.in)));
         List<String> input = readLines(new BufferedReader(new FileReader("input.txt")));
        Map<String,List<String>> productions = new HashMap<String, List<String>>();
        for(String line: input){
            String left = line.split("->")[0].trim();
            productions.put(left, new ArrayList<String>());
            String rulesStr = line.split("->")[1];
            String[] rulesArr = rulesStr.split("\\|");
            //System.out.println(left);
            //System.out.println(rulesArr.length);
            for(String rule: rulesArr){
                productions.get(left).add(rule.trim());
            }
        }
        
        /*
        for(String part: input.get(0).split("->")[1].split("\\|")){
            System.out.println(part);
        }
        System.out.println(input.get(0).split("->")[1].split("\\|").length);
        */
        Eliminator el = new Eliminator(productions);
        el.eliminate();
        el.printOutput();
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
    Map<String,List<String>> inputProductions;
    Map<String,List<String>> outputProductions;
    Map<String,List<String>> usedNonRecProductions;
    Eliminator(Map<String,List<String>> inputProductions){
        this.inputProductions = inputProductions;
        this.outputProductions = new HashMap<String, List<String>>(); 
        this.usedNonRecProductions = new HashMap<String, List<String>>();  
    }
    public void eliminate(){
        for(String left : inputProductions.keySet()){
            for(String rule : inputProductions.get(left)){
                if(isRecursive(left, rule)){
                    // eliminate recursion from rule and add new rules to output
                    Map<String,List<String>> newProductions = eliminateRule(left,rule);
                    for(String newLeft: newProductions.keySet()){
                        List<String> newRules = newProductions.get(newLeft);
                        for(String newRule: newRules){
                            addRuleToOutput(newLeft, newRule);
                        }
                    }
                }else{
                    // add rule to output
                    addRuleToOutput(left,rule);
                }
            }
        }
    }
    
    public Map<String,List<String>> eliminateRule(String left, String rule){
        String nonRecRule = getNonRecursiveRule(left);
        //A -> Aa|b
        //A -> bA'
        //A' -> aA'|#
        //String recPart = left;
        String nonRecPart = rule.substring(1,rule.length());
        String rule1 = nonRecRule + left + "'";
        String rule2 = nonRecPart + left + "'";
        String rule3 = "#";
        Map<String, List<String>> newProductions = new HashMap<String,List<String>>();
        newProductions.put(left,new ArrayList<String>());
        newProductions.put(left+"'", new ArrayList<String>());
        newProductions.get(left).add(rule1);
        newProductions.get(left + "'").add(rule2);
        newProductions.get(left + "'").add(rule3);
        return newProductions;
    }
    
    public boolean isRecursive(String left, String rule){
        if(rule.startsWith(left)){
            return true;
        }else{
            return false;
        }
    }
    
    public String getNonRecursiveRule(String left){
        List<String> rules = inputProductions.get(left);
        for(String rule: rules){
            if(!isRecursive(left, rule)){
                if(!usedNonRecProductions.containsKey(left)){
                    usedNonRecProductions.put(left,new ArrayList<String>());
                }
                usedNonRecProductions.get(left).add(rule);
                return rule;
            }
        }
        return "";
    }
    
    public void addRuleToOutput(String left, String rule){
        if(outputProductions.containsKey(left)){
            outputProductions.get(left).add(rule);
        }else{
            List<String> newRules = new ArrayList<String>();
            newRules.add(rule);
            outputProductions.put(left,newRules);
        }
        
    }
    
    public void printOutput(){
        /*
        for(String left: outputProductions.keySet()){
            System.out.print(left+" -> ");
            int index = 0;
            for(String rule: outputProductions.get(left)){
                if(index == outputProductions.get(left).size() -1){
                    System.out.print(rule);
                }else{
                    System.out.print(rule + " | ");
                }
                System.out.print("\n");
                index++;
            }
        }
        */
        for(String left: outputProductions.keySet()){
            List<String> used = usedNonRecProductions.get(left);
            List<String> rules = outputProductions.get(left);
            //System.out.print(left + " -> ");
            //int index = 0;
            for(String rule: rules){
                if(used == null){
                    System.out.println(left +" -> " + rule);
                }else{
                    if(!used.contains(rule)){
                        System.out.println(left + " -> " + rule);
                    }
                }
                //index++;
            }
            //System.out.print("\n");
            
            //System.out.print("\n");
            //System.out.println(outputProductions.get(left));
        }
    }
}
