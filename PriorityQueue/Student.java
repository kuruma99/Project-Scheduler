package PriorityQueue;

public class Student implements Comparable<Student> {
    private String name;
    private Integer marks;

    public Student(String trim, int parseInt) {
    	this.name=trim;
    	this.marks=parseInt;
    }

    public int compareTo(Student student) {
        if(marks<student.marks) {
        	return -1;
        }
        else if(marks>student.marks){
        	return 1;
        }
        else{
        	return 0;
        }
    }

    public String getName() {
        return name;
    }
    public String toString() {
    	String s="Student{name='"+getName()+"', marks="+Integer.toString(marks)+"}";
    	return s;
    }
}
