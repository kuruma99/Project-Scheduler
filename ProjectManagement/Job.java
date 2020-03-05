package ProjectManagement;

public class Job implements Comparable<Job>, JobReport_{
	String name;
	int priority;
	int runtime;
	String user;
	String Project;
	int endtime=0;
	int count=0;
	String JobStatus;
	int atime=0;
	Job(String name,int runtime,String user,String Project){
		this.name=name;
		this.runtime=runtime;
		this.user=user;
		this.Project=Project;
		JobStatus="REQUESTED";
	}
    public int compareTo(Job job) {
        if(priority<job.priority) {
        	return -1;
        }
        else if(priority>job.priority){
        	return 1;
        }
        else {
        	if(count<job.count) {
        		return 1;
        	}
        	else {
        		return -1;
        	}
        }
    }
    public String toString() {
    	return name;
    }
    public String user() { return user; }

    public String project_name()  { return Project; }

    public int budget()  { return runtime; }

    public int arrival_time()  { return atime; }

    public int completion_time() { return endtime; }
}