import java.util.*;
import java.io.*;

public class Code{
  public static void main(String[] args) throws IOException{
    MacroProccessor mp = new MacroProccessor();
    mp.firstPass(readFile(new BufferedReader(new InputStreamReader(System.in))));
    System.out.println("MNT");
    mp.printMNT();
    System.out.println("\nMDT");
    mp.printMDT();
    System.out.println("\nintemediate output");
    mp.printIntermediateOutput();
    System.out.println("\nALA");
    mp.printALA();
  }
  public static List<String> readFile(BufferedReader br) throws IOException{
    List<String> input = new ArrayList<String>();
    String line = br.readLine();
    while(line != null){
      input.add(line.trim());
      line = br.readLine();
    }
    return input;
  }
}

class MacroProccessor{
  public class MNTEntry{
    public String name;
    public int MDTIndex;
    MNTEntry(String name, int index){
      this.name = name;
      this.MDTIndex = index;
    }
  }
  
  public MNTEntry[] MNT = new MNTEntry[100];
  public int MNTC = 0;
  public String[] MDT = new String[100];
  public int MDTC = 0;
  public String[] ALA = new String[100];
  public int ALAC = 0;
  public List<String> intermediateOutput = new ArrayList<String>();
  
  public void printMNT(){
    for(int i=0;i<MNTC;i++){
        MNTEntry entry = MNT[i];
        System.out.println(i + " " + entry.name + " " + entry.MDTIndex);
    }
  }
  public void printMDT(){
    for(int i=0;i<MDTC;i++){
        String entry = MDT[i];
        System.out.println(i + " " + entry);
    }
  }
  public void printIntermediateOutput(){
    for(String line: intermediateOutput){
        System.out.println(line);
    }
  }
  
  public void printALA(){
    for(int i=0;i<ALAC;i++){
        System.out.println(i+" "+ALA[i]);
    }
  }
    
  public void firstPass(List<String> input){
    boolean isInMacro = false;
    boolean isFirstMacroLine = false;
    List<String> args = new ArrayList<String>();
    for(String line: input){
    	if(line.contains("MACRO")){
    		isInMacro = true;
    		isFirstMacroLine = true;
    		continue;
    	}
    	if(isInMacro){
    		if(line.contains("MEND")){
    			MDT[MDTC++] = line;
    			isInMacro = false;
    			args.clear();
    		}else{
    		    if(isFirstMacroLine){
    		        // find and fill args
    		        String[] tokens = line.split(" ");
    		        String label = tokens[0];
    		        String name = tokens[1];
    		        String argListStr = tokens[2];
    		        args.add(label);
    		        ALA[ALAC++] = "";
    		        for(String arg: argListStr.split(",")){
    		            args.add(arg);
    		            ALA[ALAC++] = "";
    		        }
    		        MNT[MNTC++] = new MNTEntry(name, MDTC);
    		        MDT[MDTC++] = line; 
    		        isFirstMacroLine = false;
    		    }else{
    		        // find and replace arguments
    		        String replacedLine = line;
                    int index = 0;
    		        for(String arg: args){
    		            replacedLine = replacedLine.replace(arg, "#"+index);
    		            index++;
    		        }
    		        MDT[MDTC++] = replacedLine;
    		    }
    		}
    	}else{
    		intermediateOutput.add(line);
    	}
    }
  }
  
}
