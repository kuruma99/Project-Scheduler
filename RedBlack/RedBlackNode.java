package RedBlack;

import Util.RBNodeInterface;

import java.util.ArrayList;
import java.util.List;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
	T key;
	int colour=0;
	public List<E> objects;
	public RedBlackNode <T,E> right=null;
	public RedBlackNode <T,E> left=null;
	public RedBlackNode <T,E> parent=null;
	RedBlackNode(T key,E value){
		this.key=key;
		//this.objects=new ArrayList<E>();
		if(value!=null) {
			this.objects=new ArrayList<E>();
		objects.add(value);}
	}
    public E getValue() {
        if(objects==null||objects.size()==0) {
        	return null;
        }
        else {
        	return objects.get(0);
        }
    }

    public List<E> getValues() {
        return objects;
    }
}
