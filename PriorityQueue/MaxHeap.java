package PriorityQueue;
import java.util.ArrayList;

public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {

	public ArrayList<Pair<T,Integer>>heap=new ArrayList<Pair<T,Integer>>();
	//System.out.println(heap.size());
	int priority=0;
    public void insert(T element) {
    	Pair<T,Integer> p=new Pair(element,priority);
    	priority++;
    	heap.add(p);
    	int k=heap.size()-1;
    	//System.out.println(k);
    	while(k!=0) {
    		int i=(k-1)/2;
    		if(heap.get(i).a.compareTo(heap.get(k).a)<0) {
    			Pair<T,Integer>value=heap.get(k);
    			heap.remove(k);
    			heap.add(k,heap.get(i));
    			heap.remove(i);
    			heap.add(i,value);
    			k=i;
    			//System.out.println(k);
    			//heap.set(k, heap.get(i));
    			//heap.set(i, value);
    		}
    		else {
    			break;
    		}
    	}
    	
    	
    }
   

    public void delete(int l) {
    	heap.set(l,heap.get(heap.size()-1));
    	heap.remove(heap.size()-1);
    	heapify(l);
    	
    }
    public int max(int l) {
    	if(heap.get(2*l+1).a.compareTo(heap.get(2*l+2).a)<0) {
    		return (2*l+2);
    	}
    	else if(heap.get(2*l+1).a.compareTo(heap.get(2*l+2).a)>0){
    		return (2*l+1);
    	}
    	else {
    		if(heap.get(2*l+1).b<heap.get(2*l+2).b) {
    			return (2*l+1);
    		}
    		else {
    			return (2*l+2);
    		}
    	}
    }
    public void heapify(int l) {
    	int x=(l-1)/2;
    	if(l<heap.size() & l>=0) {
    	if(l==0||heap.get(l).a.compareTo(heap.get(x).a)<=0) {
    	while(2*l+1<heap.size()) {
    		Pair<T,Integer> curr=heap.get(l);
    		if(2*l+2==heap.size()) {
    			
    			if(heap.get(l).a.compareTo(heap.get((int) (2*l+1)).a)<0) {
    				heap.set(l, heap.get((int) (2*l+1)));
    				heap.set(2*l+1, curr);
    			}
    			else if(heap.get(l).a.compareTo(heap.get((int) (2*l+1)).a)==0) {
    				if(heap.get(l).b > heap.get(2*l+1).b) {
    					heap.set(l, heap.get((int) (2*l+1)));
        				heap.set(2*l+1, curr);
    				}
    				else {
    					break;
    				}
    			}
    			else {
    				break;
    			}
    		}
    		else if(2*l+2<heap.size()){
    			
    			
    			//System.out.println(max(l));
    			int child =max(l);
    			Pair<T,Integer> p=heap.get(l);
    			if(heap.get(l).a.compareTo(heap.get(child).a)<0) {
    				
    				heap.set(l, heap.get(child));
    				heap.set(child, p);
    				l=child;
    				//System.out.println(heap.get(child));
    			}
    			else if(heap.get(l).a.compareTo(heap.get(child).a)==0) {
    				if(heap.get(l).b > heap.get(child).b) {
    					System.out.println("BB");
    					heap.set(l, heap.get(child));
    					heap.set(child, p);
        				l=child;
    				}
    				else {
    					break;
    				}
    			}
    			else {
    				break;
    			}
    		}
    	}
    	}
    	else {
    		int k=l;
        	while(k!=0) {
        		int i=(k-1)/2;
        		if(heap.get(i).a.compareTo(heap.get(k).a)<0) {
        			Pair<T,Integer>value=heap.get(k);
        			heap.remove(k);
        			heap.add(k,heap.get(i));
        			heap.remove(i);
        			heap.add(i,value);
        			k=i;
        			//System.out.println(k);
        			//heap.set(k, heap.get(i));
        			//heap.set(i, value);
        		}
        		else {
        			break;
        		}
        	}
    	}
    	}
    }
    public T extractMax() {
    	if(heap.size()!=0) {
        Pair<T,Integer>value=heap.get(0);
        
        delete(0);
        return value.a;}
    	else {
    		return null;
    	}
    }
    public boolean contains(T t) {
    	for(int i=0;i<heap.size();i++) {
    		if(heap.get(i).a.toString().compareTo(t.toString())==0) {
    			return true;
    		}
    	}
    	return false;
    }
}
