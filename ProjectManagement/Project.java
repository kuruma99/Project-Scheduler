package ProjectManagement;
import java.util.ArrayList;
import RedBlack.*;

public class Project implements Comparable<Project>{
	String name;
	int budget,priority,count=0;
	ArrayList<Job> jobs2;
	
	Project(String name,int budget,int priority){
		this.name=name;
		this.budget=budget;
		this.priority=priority;
		jobs2=new ArrayList<Job>();
		
	}

	@Override
	public int compareTo(Project p) {
		// TODO Auto-generated method stub
		if(priority<p.priority) {
			return -1;
		}
		else if(priority>p.priority) {
			return 1;
		}
		else {
			if(count<p.count) {
				return 1;
			}
			else {
				return -1;
			}
		}
	}
}
