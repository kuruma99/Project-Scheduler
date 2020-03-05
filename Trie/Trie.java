package Trie;
import java.util.Queue; 
import java.util.ArrayList;
import java.util.LinkedList; 
public class Trie<T> implements TrieInterface<T> {
	 int k=0;
	TrieNode<T>root=new TrieNode<T>(null);
	int maxlevel=0;
	public boolean insert(String word, T value) {
		// TODO Auto-generated method stub
		if(search(word)==null||search(word).getValue()==null) {
			int a;
			char c;
			TrieNode<T> n=root;
			n.level=0;
			n.character=' ';
			for(int i=0;i<word.length();i++) {
				if(i+1>maxlevel) {
					maxlevel=i+1;
				}
				a=word.charAt(i);
				c=word.charAt(i);
				if(n.t[a]==null) {
					n.t[a]=new TrieNode<T>(null);
					n=n.t[a];
					n.level=i+1;
					n.character=c;
					
				}
				else {
					n=n.t[a];
				}
			}
			n.data=value;
			n.isWord=true;
			return true;
		}
		else {
			return false;
		}
	}

	
	public TrieNode<T> search(String word) {
		if(root==null||word==null) {
			return null;
		}
		// TODO Auto-generated method stub
		int a;
		TrieNode<T> n=root;
		for(int i=0;i<word.length();i++) {
			a=word.charAt(i);
			if(n.t[a]==null) {
				return null;
			}
			else {
				n=n.t[a];
			}
		}
		if(n.getValue()==null) {
			return null;
		}
		else {
		return n;
		}
	
	}

	
	public TrieNode<T> startsWith(String prefix) {
		// TODO Auto-generated method stub
		int a;
		TrieNode<T> n=root;
		for(int i=0;i<prefix.length();i++) {
			a=prefix.charAt(i);
			if(n.t[a]==null) {
				return null;
			}
			else {
				n=n.t[a];
			}
		}
		return n;
	}

	
	public void printTrie(TrieNode trieNode) {
		// TODO Auto-generated method stub
		
		if(trieNode!=null) {
			if(trieNode.getValue()!=null) {
				System.out.println(trieNode.getValue());
			}
			for(int i=0;i<128;i++) {
				
				printTrie(trieNode.t[i]);
			}
		}
	}


	public boolean delete(String word) {
		// TODO Auto-generated method stub
		TrieNode<T> prevword=root;int a;
		TrieNode<T> n=root;
		int prev_val=word.charAt(0),wordi=-1 ;
		
		if(search(word)==null) {
			System.out.println("ERROR DELETING");
			return false;
			
		}
		else {
			int count2=0;
			for(int i=0;i<word.length();i++) {
				
				a=word.charAt(i);
				for(int j=0;j<128;j++) {
					if(n.t[j]==null) {
						count2++;
					}
				}
				//System.out.println(count2);
				if(count2!=127) {
					prevword=n;
					prev_val=a;
					//wordi=i;
					count2=0;
					}
				else {
					count2=0;
				}
				n=n.t[a];
				}
			}
			
		int count=0;
			for(int i=0;i<128;i++ ) {
				if(n.t[i]==null) {
					count++;
				}
			}
			if(count==128) {
				prevword.t[prev_val]=null;
			}
			else {
				n.data=null;
			}if(k==0) {
			System.out.println("DELETED");}
			return true;
		}
	
	
	public void print() {
		// TODO Auto-generated method stub
		Queue<TrieNode<T>>q=new LinkedList<TrieNode<T>>();
		String s="Level ",fin;
		
		q.add(root);
		int level=1;
		int plevel=0;
		ArrayList<Character> sort=new ArrayList<Character>();
		System.out.println("-------------");
		System.out.println("Printing Trie");
		/*while(q.size()!=0) {
			TrieNode<T> t=q.remove();
			if(t.level==level) {
				if(t.character!=' ') {
					String p=Integer.toString(level);
					if(plevel==0) {
						s=s+p+" ";
						plevel=1;
					}
					//s=s+t.character+",";
					sort.add(t.character);
				}
			}
			if(t.level==level+1) {
				for(int i=0;i<sort.size();i++) {
					
					for(int j=i;j<sort.size();j++) {
						int b;
						int a=sort.get(i);
						char l=sort.get(i);
						b=sort.get(j);
						
						if(b<a) {
							char m=sort.get(j);
							sort.remove(j);
							sort.add(j,l);
							sort.remove(i);
							sort.add(i,m);
						}
					}
				}
				for(int i=0;i<sort.size();i++) {
					s=s+sort.get(i)+",";
				}
				sort.clear();
				fin=s.substring(0,s.length()-1);
				System.out.println(fin);
				s="Level ";
				level++;
				plevel=0;
				if(t.character!=' ') {
					String p=Integer.toString(level);
					if(plevel==0) {
						s=s+p+" ";
						plevel=1;
					}
					sort.add(t.character);
				}
			}
			for(int i=0;i<128;i++) {
				if(t.t[i]!=null) {
					q.add(t.t[i]);
				}
			}
		}
		fin=s.substring(0,s.length()-1);*/
		for(int i=1;i<maxlevel+1;i++) {
			printLevel(i);
		}
		String last="Level "+ Integer.toString(maxlevel+1)+": ";
		System.out.println(last);
		System.out.println("-------------");
	}

	
	public void printLevel(int level) {
		// TODO Auto-generated method stub
		Queue<TrieNode<T>>q=new LinkedList<TrieNode<T>>();
		String levels=Integer.toString(level);
		String s="Level "+levels+": ",fin;
		ArrayList<Character> sort=new ArrayList<Character>();
		q.add(root);
		while(q.size()!=0) {
			TrieNode<T> t=q.remove();
			if(t.level==level) {
				if(t.character!=' ') {
				sort.add(t.character);}
			}
			if(t.level==level+1) {
				break;
			}
			for(int i=0;i<128;i++) {
				if(t.t[i]!=null) {
					q.add(t.t[i]);
				}
			}
		}
		for(int i=0;i<sort.size();i++) {
			
			for(int j=i;j<sort.size();j++) {
				int b;
				int a=sort.get(i);
				char l=sort.get(i);
				b=sort.get(j);
				
				if(b<a) {
					char m=sort.get(j);
					sort.remove(j);
					sort.add(j,l);
					sort.remove(i);
					sort.add(i,m);
				}
			}
		}
		for(int i=0;i<sort.size();i++) {
			s=s+sort.get(i)+",";
		}
		fin=s.substring(0, s.length()-1);
		System.out.println(fin);
	}

}
