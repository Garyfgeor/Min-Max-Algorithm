package ce326.hw2;
public class leafNode{
    private double value;
    private String type;
    private internalNode parent;
    private int pruned=1;

    public leafNode(){}

    public leafNode(double value, String type){
        this.value = value;
        this.type = type;
    }

    public void setNotPruned(int pruned){
        this.pruned = pruned;
    }
    public int isPruned(){
        return this.pruned;
    }

    public double getValue(){
        return this.value;
    }

    public String getType(){
        return this.type;
    }

    public void setValue(double value){
        this.value = value;
    }
    public void setParent(internalNode parent){
        this.parent = parent;
    }
    public internalNode getParent(){
        return this.parent;
    }
    public void setType(String type){
        this.type = type;
    }
} 