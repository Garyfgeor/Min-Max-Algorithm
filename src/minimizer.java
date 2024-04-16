package ce326.hw2;
public class minimizer extends internalNode{
    
    public minimizer(){}

    public double minValueChild(leafNode[] children){
        double minValue = Double.MAX_VALUE;
        int i;
        for(i=0; i<children.length; i++){
            if(children[i].getValue() < minValue){
                minValue = children[i].getValue();
            }
        }
        return minValue;
    }
}