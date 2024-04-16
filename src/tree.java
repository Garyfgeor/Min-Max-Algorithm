//package ce326.hw2;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.IOException;

public class tree{
    private internalNode root;

    public tree(String JSONstr){
        JSONObject JSONobj = new JSONObject(JSONstr);
        readJson(root, JSONobj, 0);
    }

    public internalNode getRoot(){
        return this.root;
    }

    public void readJson(internalNode parent, JSONObject JSONobj,int check){
        String type = null;
        type = JSONobj.getString("type");

        if(check==0&&type.equals("leaf")) {
            internalNode curr = new internalNode();
            root = curr;
            parent = root;
            curr.setParent(curr);
            curr.setType(type);
            double value = JSONobj.getDouble("value");
            curr.setValue(value);
            return;
        }

        if(root == null){
            internalNode curr = new internalNode();
            root = curr;
            parent = root;
            curr.setParent(curr);
            curr.setType(type);
            curr.setValue(Double.MAX_VALUE);
        }
       
        JSONArray JSONarr = JSONobj.getJSONArray("children");
        
        for(int i=0; i<JSONarr.length(); i++){ 
            
            type = JSONarr.getJSONObject(i).getString("type");

            JSONObject newObj1 =  JSONarr.getJSONObject(i);
            
            if(type.equals("min")){
                internalNode curr = new internalNode();
                curr = new minimizer();
                curr.setValue(-Double.MAX_VALUE);
                curr.setType(type);
                curr.setParent(parent);
                if(i==0){
                    parent.setChildrenSize(JSONarr.length());
                }
                parent.insertChild(i, curr);
                readJson(curr, newObj1, 1);
            }
            else if(type.equals("max")){
                internalNode curr = new internalNode();
                curr = new maximizer();
                curr.setValue(Double.MAX_VALUE);
                curr.setType(type);
                curr.setParent(parent);
                if(i==0){
                    parent.setChildrenSize(JSONarr.length());
                }
                parent.insertChild(i, curr);
                readJson(curr, newObj1, 1);
            }
            else if(type.equals("leaf")){
                leafNode curr = new leafNode();
                double value = JSONarr.getJSONObject(i).getDouble("value");
                curr.setType(type);
                curr.setValue(value);
                curr.setParent(parent);
                if(i==0){
                    parent.setChildrenSize(JSONarr.length());
                }
                parent.insertChild(i, curr);
            }
        }
        return;
    }

    public void printTree(leafNode node){
        System.out.println(node.getValue());
        if (!(node instanceof internalNode)){ 
            return; 
        }
        internalNode curr1 = (internalNode)node;
        if (node instanceof internalNode){ 
           for(int i=0; i<curr1.getChildrenSize();i++){ 
                printTree(curr1.getChild(i));
            }
        }
    }

    public double minMax(){
        minMax_help(root);
        double rootValue = root.getValue();
        return rootValue;

    }
    public void minMax_help(leafNode node){
        int i;
        if (!(node instanceof internalNode)){
            if (node.getType().equals("leaf")){
                internalNode curr = node.getParent();
                double min, max;
                if(curr.getType().equals("min")){
                    min = curr.setMinMax(curr.getType(), curr);
                    curr.setValue(min);
                }
                else if(curr.getType().equals("max")){
                    max = curr.setMinMax(curr.getType(), curr);
                    curr.setValue(max);
                } 
                return; 
            }
        }
        internalNode curr1 = (internalNode)node;
        if(curr1.children != null){
            for(i=0; i<curr1.getChildrenSize(); i++){
                minMax_help(curr1.children[i]);
            }
            double min, max;
            if(curr1.getType().equals("min")){
                min = curr1.setMinMax(curr1.getType(), curr1);
                curr1.setValue(min);
            }
            else if(curr1.getType().equals("max")){
                max = curr1.setMinMax(curr1.getType(), curr1);
                curr1.setValue(max);
            } 
        }
        
        return;
    }
    
    public String toDOTString(){
        return toDOTString_help(root);
    }
    public String toDOTString_help(leafNode root){
        StringBuffer str = new StringBuffer();
        StringBuffer dotString = preorderDOT(root, str);
        dotString.insert(0, "graph Tree{\n");
        dotString.append("}");
        String dot = dotString.toString();
        return dot;
    }

    public StringBuffer preorderDOT(leafNode node, StringBuffer str){
        if(node == null){
            return str;
        }
        
        int hash1=node.hashCode();
        internalNode curr2;
        if (node instanceof internalNode){
            curr2 = (internalNode)node;
            if(curr2.getInParent() != null && curr2 != root){
                int hash2 = curr2.getInParent().hashCode();
                str.append("    "+hash2+" -- "+hash1+"\n");
            }
    
            if(curr2 != null){
                str.append("    "+hash1+" [label=\"%f\"]\n");
                double val = curr2.getValue();
                String value = val+"";
                str.replace(str.length()-5, str.length()-3, value);
            }
        }
        else{
            if(node.getParent() != null && node != root){
                int hash2 = node.getParent().hashCode();
                str.append("    "+hash2+" -- "+hash1+"\n");
            }
    
            if(node != null){
                str.append("    "+hash1+" [label=\"%f\"]\n");
                double val = node.getValue();
                String value = val+"";
                str.replace(str.length()-5, str.length()-3, value);
            }
        }
        
        if (!(node instanceof internalNode)){
            return str;
        }
        internalNode curr1 = (internalNode)node;
        for(int i=0; i<curr1.getChildrenSize(); i++){
            preorderDOT(curr1.children[i], str);
        }
        return str;
        
    }

    public void toDotFile(File file) throws IOException{
        if (file.createNewFile()){
            System.out.println("\nFile created: " + file.getName()+"\n\n");
            FileWriter wr = new FileWriter(file);
            wr.write(toDOTString());
            wr.close();
            System.out.println("\nOK\n\n");
        }
        else{
            System.out.format("\nFile '%s' already exists\n\n", file);
        }
    }

    public ArrayList<Integer> optimalPath(){
        ArrayList<Integer> list = new ArrayList<>();
        return optimalPath_help(list, root);

    }
    public ArrayList<Integer> optimalPath_help(ArrayList<Integer> list, leafNode node){
        if (node instanceof internalNode){
            internalNode curr = (internalNode)node;
            if(curr.children != null){
                for(int i=0; i<curr.getChildrenSize(); i++){
                    if(curr.getValue() == curr.getChild(i).getValue()){
                        list.add(i); 
                        optimalPath_help(list, curr.getChild(i));
                    }
                }
            }
        }
        else{
            return list;
        }
        return list;
    }

    public int size(leafNode node){
        int numOfNodes=1;
        if (node instanceof internalNode){
            internalNode curr = (internalNode)node;
            for(int i=0; i<curr.getChildrenSize(); i++){
                numOfNodes = numOfNodes + size(curr.children[i]);
            }
        }
        else{
            return 1;
        }
        return numOfNodes;
    }

    public JSONObject treeToJSON(leafNode node){
        JSONObject nodeJson = new JSONObject();
        JSONArray children = new JSONArray();
        
        nodeJson.put("type", node.getType());
        if(node.getValue() != Double.MAX_VALUE && node.getValue() != -Double.MAX_VALUE){   
            nodeJson.put("value", node.getValue());
        }
        if (node instanceof internalNode){
            internalNode curr = (internalNode)node;
            if(curr.children != null){
                for (int i=0; i<curr.getChildrenSize(); i++){
                    nodeJson.put("children", children);
                    JSONObject child = treeToJSON(curr.getChild(i));
                    children.put(child);
                }
            }
        }
        return nodeJson;
    }
    
    public String toString(){
        JSONObject jsonTree = treeToJSON(root);
        String JSONstr = jsonTree.toString(2);
        return JSONstr;
    }
    
    public void toFile(File file) throws IOException {
        if(file.exists()) {
            throw new IOException();
        }
        else if(file != null){
            FileWriter wr = new FileWriter(file);
            wr.write(toString());
            wr.close();
            System.out.println("\nOK\n\n");
        }
    }
    
}