package ce326.hw2;
import java.io.*;
import java.util.Scanner;
import org.json.JSONException;
public class MinMax{
    static tree MMtree;
    static optimalTree MMtreeOpt;

    public MinMax(){}

    public static void main(String []args){
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String command  = null;
        String JSONstr = null;
        int flag=0;

        //print Menu of choices
        while(command  == null || command.substring(0, 2) != "-q"){
            System.out.println();
            System.out.println("-i <filename>   :  insert tree from file");
            System.out.println("-j [<filename>] :  print tree in the specified filename using JSON format");
            System.out.println("-d [<filename>] :  print tree in the specified filename using DOT format");
            System.out.println("-c              :  calculate tree using min-max algorithm");
            System.out.println("-p              :  calculate tree using min-max and alpha-beta pruning optimization");
            System.out.println("-q              :  quit this program");
            System.out.print("\n$> ");
            command = sc.nextLine();

            switch(command.substring(0, 2)){
                case ("-i"):
                    String filepath = command.substring(3, command.length());
                    StringBuilder strBuilder = new StringBuilder();
                    File file = new File(filepath);
                    try(Scanner scan = new Scanner(file)){
                        while(scan.hasNextLine()){
                            String str = scan.nextLine();
                            strBuilder.append(str);
                            strBuilder.append("\n");
                        }
                    }catch(FileNotFoundException ex){
                        if(!file.exists()) {
                            System.out.format("\nUnable to find '%s'\n\n\n",filepath);
                        }
                        else {
                            System.out.format("\nUnable to open '%s'\n\n\n",filepath);
                        }
                        break;
                    }

                    JSONstr = strBuilder.toString();
                    //kalw kataskeuasti dentrou
                    try{
                        MMtree = new tree(JSONstr);
                    }catch(JSONException ex){
                        System.out.println("\nInvalid format\n\n");
                        break;
                    }
                    System.out.println("\nOK\n\n");
                    break;

                case("-j"):
                    file = null;
                    filepath=null;
                    if(command.length() > 4){
                        filepath = command.substring(3, command.length());
                        file = new File(filepath);
                    }
                    else{
                        if(flag==0){
                            System.out.println("\n"+MMtree.toString()+"\n\n");
                            System.out.println("\nOK\n\n");
                        }
                        else{
                            System.out.println("\n"+MMtreeOpt.toString()+"\n\n");
                            System.out.println("\nOK\n\n");
                        }
                        break;
                    }
                    if(flag==0){
                        try{
                            MMtree.toFile(file);
                        }catch(IOException ex) {
                            if(file.exists()){
                                System.out.format("\nFile '%s' already exists\n\n\n", filepath);
                            }
                            else{
                                System.out.format("\nUnable to write '%s'\n\n\n",filepath);
                            }
                        }
                    }
                    else{
                        try{
                            if(MMtreeOpt==null){
                                MMtree.toFile(file);
                            }
                            else{
                                MMtreeOpt.toFile(file);
                            }
                        }catch(IOException ex){
                            if(file.exists()){
                                System.out.format("\nFile '%s' already exists\n\n\n", filepath);
                            }
                            else{
                                System.out.format("\nUnable to write '%s'\n\n\n",filepath);
                            }
                        }
                    }
                    break;

                case("-d"):
                    filepath = command.substring(3, command.length());
                    file = new File(filepath);
                
                    if(flag==0){
                        try{
                            MMtree.toDotFile(file);
                        } catch(IOException ex){
                            if(file.exists()){
                                System.out.format("\nFile '%s' already exists\n\n\n", filepath);
                            }
                            else{
                                System.out.format("\nUnable to write '%s'\n\n\n",filepath);
                            }
                        }
                    }
                    else{
                        try{
                            MMtreeOpt.toDotFile(file);
                        }catch(IOException ex){
                            if(file.exists()){
                                System.out.format("\nFile '%s' already exists\n\n\n", filepath);
                            }
                            else{
                                System.out.format("\nUnable to write '%s'\n\n\n",filepath);
                            }
                        }
                    }
                    
                    break;

                case("-c"):
                    flag=0;
                    if(MMtree.getRoot().children != null){
                        MMtree.minMax();
                        System.out.println();
                        for(int i = 0; i<MMtree.optimalPath().size()-1; i++){
                            System.out.format("%d, ", MMtree.optimalPath().get(i));
                        }
                        System.out.format("%d", MMtree.optimalPath().get(MMtree.optimalPath().size()-1));
                    }
                    System.out.println("\n\n");
                    break;

                case("-p"):
                    flag=1;
                    if(MMtree.getRoot().children != null){
                        MMtreeOpt = new optimalTree(JSONstr);
                        MMtreeOpt.minMax();
                        for(int i = 0; i<MMtreeOpt.optimalPath().size()-1; i++){
                            System.out.format("%d, ", MMtreeOpt.optimalPath().get(i));
                        }
                        System.out.format("%d", MMtreeOpt.optimalPath().get(MMtreeOpt.optimalPath().size()-1));
                        System.out.println("\n\n");
                    }
                    else{
                        System.out.print("\n[1,0] ");
                        System.out.println("\n\n");
                    }
                    break;

                case("-q"):
                    sc.close();
                    System.exit(0);
            }
        }
    }  
}
