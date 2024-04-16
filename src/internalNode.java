package ce326.hw2;
import java.lang.Double;

public class internalNode extends leafNode{
   leafNode[] children = null;
   internalNode parent;
   double alpha = -Double.MAX_VALUE;
   double beta = Double.MAX_VALUE;
   
   public internalNode(){}

   public internalNode(leafNode[] children){
        this.children = children;
    }

    public double getAlpha(){
        return this.alpha;
    }

    public double getBeta(){
        return this.beta;
    }

    public void setAlpha(double alpha){
        this.alpha = alpha;
    }

    public void setBeta(double beta){
        this.beta = beta;
    }

    public double setMinMax(String type, internalNode node){
        double value=-100;
        if(type.equals("min")){
            minimizer minNode = new minimizer();
            value = minNode.minValueChild(node.children);
        }
        else if(type.equals("max")){
            maximizer maxNode = new maximizer();
            value = maxNode.maxValueChild(node.children);
        }
        return value;
    }
    
    public void setChildrenSize(int size){
        this.children = new leafNode[size];
    }
    
    public void setParent(internalNode parent){
        this.parent = parent;
    }
    public internalNode getInParent(){
        return this.parent;
    }
    public int getChildrenSize(){
        return children.length;
    }

    public void insertChild(int pos, leafNode X){
        this.children[pos] = X;
    }

    public leafNode getChild(int pos){
        return this.children[pos];
    }
}