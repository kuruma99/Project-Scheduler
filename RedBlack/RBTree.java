package RedBlack;

import java.util.ArrayList;

public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {

	public RedBlackNode<T,E> root=null;
	
	private RedBlackNode<T,E> insert(RedBlackNode<T,E> node,T key,E value){
		if(node==null) {
			//System.out.println(root);
			node=new RedBlackNode<T,E>(key,value);
			//System.out.println(node.colour);
			return node;
		}
		else {
			//System.out.println("aa");
			if(key.compareTo(node.key)<0) {
				RedBlackNode<T,E> child=insert(node.left,key,value);
				node.left=child;
				child.parent=node;
				
			}
			else if(key.compareTo(node.key)>0){
				RedBlackNode<T,E> child=insert(node.right,key,value);
				node.right=child;
				child.parent=node;
				
			}
	//System.out.println(node);
		return node;}
	}
    @Override
    public void insert(T key, E value) {
    	/*RedBlackNode<T,E> node=new RedBlackNode<T,E>(key,value),curr=root;
    	if(search(key)==null) {
    	if(root==null) {
    		root=node;
    		root.parent=null;
    		root.colour=1;
    	}
    	else {
    		node.colour=0;
    		while(-1<0) {
    			if(key.compareTo(curr.key)<0) {
    				if(curr.left==null) {
    					curr.left=node;
    					node.parent=curr;
    					break;
    				}
    				else {
    					curr=curr.left;
    				}
    			}
    			if(key.compareTo(curr.key)>0) {
    				if(curr.right==null) {
    					curr.right=node;
    					node.parent=curr;
    					break;
    				}
    				else {
    					curr=curr.right;
    				}
    			}
    		}
    	}*/
    	if(search(key).key==null) {
    		root=insert(root,key,value);
    		RedBlackNode<T,E> node=search(key);
    		restructure(node);
    	
    	
    	}
    	else {
    		
    		RedBlackNode<T,E> node=search(key);
    		if(node.objects!=null) {
    		node.objects.add(value);}
    		else {
    			node.objects=new ArrayList<E>();
    			node.objects.add(value);
    		}
    	}
    	
    }
    private void restructure(RedBlackNode<T,E> node) {
    	if(node==null) {
    	}
    	else if(node.parent==null) {
    		node.colour=1;
    		root=node;
    	}
    	else if(node.parent.parent==null) {
    		node.colour=0;
    		node.parent.colour=1;
    		root=node.parent;
    	}
    	else {
    		RedBlackNode<T,E> uncle=null;
    		if(node.parent.parent.left==node.parent) {
    			uncle=node.parent.parent.right;
    		}else if(node.parent.parent.right==node.parent) {
    			uncle=node.parent.parent.left;
    		}
    		if(node.parent.colour==0) {
    			if(uncle!=null&&uncle.colour==0) {
    				node.parent.colour=1;
    				uncle.colour=1;
    				node.parent.parent.colour=0;
    				restructure(node.parent.parent);
    			}
    			else {
    				if(node.parent.parent.left==node.parent) {
    					if(node.parent.left==node) {
    						node.parent.colour=1;
    						node.parent.parent.colour=0;
    						rotateright(node.parent.parent);
    					}
    					else if(node.parent.right==node) {
    						node.colour=1;
    						node.parent.parent.colour=0;
    						rotateleft(node.parent);
    						rotateright(node.parent);
    					}
    				}
    				else if(node.parent.parent.right==node.parent) {
    						if(node.parent.right==node) {
    							node.parent.colour=1;
    							node.parent.parent.colour=0;
    							rotateleft(node.parent.parent);
    						}
    						else if(node.parent.left==node) {
    							node.colour=1;
    							node.parent.parent.colour=0;
    							rotateright(node.parent);
    							rotateleft(node.parent);
    						}
    				}
    			}
    		}
    		root.colour=1;
    	}
    }
    private void rotateleft(RedBlackNode<T,E> node) {
    	RedBlackNode<T,E> right=node.right,c=right.left,parent=node.parent;
    	int z;
    	if(parent!=null) {
    	if(parent.left==node) {
    		z=1;
    	}
    	else {
    		z=0;
    	}
    	right.left=node;
    	node.parent=right;
    	right.parent=parent;
    	node.right=c;
    	if(c!=null) {
    		c.parent=node;
    	}
    	
    		if(z==1) {
    			parent.left=right;
    		}
    		else {
    			parent.right=right;
    		}
    	}
    	else {
    	right.left=node;
    	node.parent=right;
    	right.parent=parent;
    	node.right=c;
    	if(c!=null) {
    		c.parent=node;
    	}
    		root=right;
    	}
    }
    private void rotateright(RedBlackNode<T,E> node) {
    	RedBlackNode<T,E> left=node.left,c=left.right,parent=node.parent;
    	int z;
    	if(parent!=null) {
    	if(parent.left==node) {
    		z=1;
    	}
    	else {
    		z=0;
    	}
    	left.right=node;
    	node.parent=left;
    	left.parent=parent;
    	node.left=c;
    	if(c!=null) {
    		c.parent=node;
    	}
    	
    		if(z==1) {
    			parent.left=left;
    		}
    		else {
    			parent.right=left;
    		}
    	}
    	else {
    		left.right=node;
        	node.parent=left;
        	left.parent=parent;
        	node.left=c;
        	if(c!=null) {
        		c.parent=node;
        	}
    		root=left;
    	}
    }
    @Override
    public RedBlackNode<T, E> search(T key) {
    	if(root==null) {
    		return new RedBlackNode<T,E>(null,null);
    	}
    		
    	RedBlackNode<T,E> curr=root;
    	while(true) {
    		if(curr==null) {
    			return new RedBlackNode<T,E>(null,null);
    		}
    		if(key.compareTo(curr.key)==0) {
    			return curr;
    		}else {
    			if(key.compareTo(curr.key)<0) {
    				curr=curr.left;
    			}
    			else {
    				curr=curr.right;
    			}
    		}
        
    	}
    }
}