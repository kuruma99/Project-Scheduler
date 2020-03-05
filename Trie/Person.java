package Trie;

public class Person {

	String name,pn;
    public Person(String name, String phone_number) {
    	this.name=name;
    	this.pn=phone_number;
    }

    public String getName() {
        return name;
    }
    public String toString() {
    	String s="";
    	s=s+"[Name: "+name+", Phone="+pn+"]";
    	return s;
    }
}
