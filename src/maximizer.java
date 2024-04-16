package ce326.hw2;
public class maximizer extends internalNode{
    
    public maximizer(){}

    public double maxValueChild(leafNode[] children){
        double maxValue = -Double.MAX_VALUE;
        int i;
        for(i=0; i<children.length; i++){
            if(children[i].getValue() > maxValue){
                maxValue = children[i].getValue();
            }
        }
        return maxValue;
    }
}