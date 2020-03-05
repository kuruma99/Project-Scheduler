package Trie;


import Util.NodeInterface;


public class TrieNode<T> implements NodeInterface<T> {
	public T data;
	TrieNode<T>[] t=new TrieNode[128];
	boolean isWord;
	int level=0;
	char character;
    public T getValue() {
    	
        return data;
    }
    TrieNode(T data){
    	isWord=false;
		this.data=data;
	}

}