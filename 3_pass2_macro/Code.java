import java.io.*;
import java.util.*;

public class Code{
    public static void main(String[] args) throws IOException, FileNotFoundException{
       List<String> MNT = readFile("MNT.txt");
       List<String> MDT = readFile("MDT.txt");
       List<String> input = readFile("input.txt");
       MacroProcessor mp = new MacroProcessor();
       mp.secondPass(MDT, MNT, input);
       System.out.println("\noutput");
       mp.printOutput();
       System.out.println("\nALA");
       mp.printALA();
    }
    public static List<String> readFile(String fileName) throws IOException, FileNotFoundException{
        List<String> file = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        while(line != null){
            file.add(line);
            line = br.readLine();
        }
        return file;
    }
}

class MacroProcessor{
    public String[] ALA = new String[100];
    public int ALAC = 0;
    public List<String> finalOp = new ArrayList<String>();
    public List<String> MDT;
    public List<String> MNT;
    public List<String> input;
    public void secondPass(List<String> MDT, List<String> MNT, List<String> input){
        this.MDT = MDT;
        this.MNT = MNT;
        this.input = input;
        //scan input for macro
        //if macro is found generate its ALA using MNT and MDT
        //replace lines in MDT using ALA and add them to final output
        for(String line: input){
            String name = line.split(" ")[1];
            if(isMacro(name)){
                int MDTIndex = getMDTIndex(name);
                String MDTEntry = MDT.get(MDTIndex);
                // generate ALA
                // add label to ala
                ALA[ALAC++] = line.split(" ")[0];
                // add other args to ala
                String argListStr = line.split(" ")[2];
                for(String arg: argListStr.split(",")){
                    ALA[ALAC++] = arg;
                }
                //for each line in mdt replace args and add line to output
                int currLineIndex = MDTIndex+1;
                String MDTLine = MDT.get(currLineIndex++);
                while(!MDTLine.equals("MEND")){
                    for(int i=0;i<ALAC;i++){
                        MDTLine = MDTLine.replace("#"+i,ALA[i]);
                    }
                    finalOp.add(MDTLine);
                    MDTLine = MDT.get(currLineIndex++);
                }
            }else{
                finalOp.add(line);
            }
        }
    }
    
    public void printALA(){
        for(int i=0;i<ALAC;i++){
            System.out.println(i + " " + ALA[i]);
        }
    }
    
    public void printOutput(){
        int index = 0;
        for(String line: finalOp){
            System.out.println(index + " " + line);
            index++;
        }
    }
    
    public boolean isMacro(String name){
        boolean isMacro = false;
        for(String line: MNT){
            if(line.split(" ")[0].equals(name)){
                isMacro = true;
                break;
            }
        }
        return isMacro;
    }
    public int getMDTIndex(String candidate){
        for(String line: MNT){
            String name = line.split(" ")[0];
            int index = Integer.parseInt(line.split(" ")[1]);
            if(name.equals(candidate)){
                return index;
            }
        }
        return -1;
    }
}
