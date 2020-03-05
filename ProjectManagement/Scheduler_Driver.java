package ProjectManagement;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import RedBlack.*;
import Trie.*;


import PriorityQueue.MaxHeap;
import RedBlack.RBTree;
import Trie.Trie;

public class Scheduler_Driver extends Thread implements SchedulerInterface {


    public static void main(String[] args) throws IOException {
//

        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }
        //long a =System.currentTimeMillis();
        scheduler_driver.execute(file);
        //long b=System.currentTimeMillis();
        //System.out.println(b-a);
    }

    public void execute(File commandFile) throws IOException {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }
                String project_name, user_name;
                Integer start_time, end_time;

                long qstart_time, qend_time;

                switch (cmd[0]) {
                    case "PROJECT":
                        handle_project(cmd);
                        break;
                    case "JOB":
                        handle_job(cmd);
                        break;
                    case "USER":
                        handle_user(cmd[1]);
                        break;
                    case "QUERY":
                        handle_query(cmd[1]);
                        break;
                    case "": // HANDLE EMPTY LINE
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":
                    case "NEW_USER":
                    case "NEW_PROJECTUSER":
                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                        qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Top query");
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Flush query");
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    default:
                        System.err.println("Unknown command: " + cmd[0]);
                }

            }


            run_to_completion();
            print_stats();

        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
    }

    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
               // System.out.println(res);
                System.out.println("Project query");
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                /*for(int i=0;i<res.size();i++) {
                	System.out.println(res.get(i).user());
                }*/
                break;
            case "NEW_USER":
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                System.out.println("User query");
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));

                break;
            case "NEW_PROJECTUSER":
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                System.out.println("Project User query");
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                System.out.println("Priority query");
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }

        return res;
    }
    Trie<Project> projects=new Trie<Project>();
    MaxHeap<Project> pheap=new MaxHeap<>();
    List<Job> temc=new ArrayList<Job>();
    RBTree<String,Job> teml=new RBTree<String,Job>();
    MaxHeap<Job> jobs=new MaxHeap<Job>();
    RBTree<String,User> users=new RBTree<String,User>();
    RBTree<String,Job> u2=new RBTree<String,Job>();
    List<Job>comp=new ArrayList<Job>();//left=new ArrayList<Job>();
    RBTree<String,Job> compl=new RBTree<String,Job>();
   // RBTree<String,Job> p2=new RBTree<String,Job> ();
    int endtime=0;


    @Override
    public ArrayList<UserReport_> timed_top_consumer(int top) {
    	ArrayList<UserReport_> a=new ArrayList<>();
    	MaxHeap<User> b=new MaxHeap<>();
    	ArrayList<RedBlackNode<String,User>> q=new ArrayList<>();
    	q.add(users.root);
    	for(int iq=0;iq<q.size();iq++) {
    		RedBlackNode<String,User> n=q.get(iq);
    		if(n!=null) {
            	if(n.left!=null) {
            	q.add(n.left);}
            	if(n.right!=null) {
            	q.add(n.right);}
            	User u=n.getValue();
            	//System.out.println(u.name);
            	
            	b.insert(u);  	
    	}
    	
    	}
    	int min=b.heap.size();
    	if(top<min) {
    		min=top;
    	}
    	for(int i=0;i<min;i++) {
    		User e=b.extractMax();
    		a.add(e);
    	}
    	
    	return a;
    }



    @Override
    public void timed_flush(int waittime) {
    	MaxHeap<Job> temp=new MaxHeap<Job>();
    	int wait;
    	for(int i=0;i<jobs.heap.size();i++) {
    		Job j=jobs.heap.get(i).a;
    		wait=endtime-j.arrival_time();
    		if(wait>=waittime) {
    			jobs.delete(i);
    			temp.insert(j);
    			i--;
    		}
    	}
    	while(temp.heap.size()!=0) {
    		Job j=temp.extractMax();
    		Project p=projects.search(j.Project).getValue();
    		if(p.budget>=j.runtime) {
    			endtime=endtime+j.runtime;
    			j.endtime=endtime;
    			String user=j.user;
        		int k=users.search(user).getValue().consumed;
        		users.search(user).getValue().consumed=k+j.runtime;
        		users.search(user).getValue().latest_time=endtime;
        		j.JobStatus="COMPLETED";
    			comp.add(j);
    			temc.add(j);
    			compl.insert(j.Project,j);
    			p.budget=p.budget-j.runtime;
    			//System.out.println(j.name);
    			//System.out.println(j.atime);
    			
    		}
    		else {
    			teml.insert(j.Project,	j);
    		}
    	}
    }
    
    
    private ArrayList<JobReport_> handle_new_priority(String s) {
    	
        ArrayList<JobReport_> a=new ArrayList<JobReport_>();
        //long p=System.nanoTime();
        ArrayList<RedBlackNode<String,Job>> q=new ArrayList<>();
        //long b=System.nanoTime();
       q.add(teml.root);
       
        //System.out.println(b-p);
        for(int iq=0;iq<q.size();iq++) {
        	//System.out.println(q);
        	RedBlackNode<String,Job> n=q.get(iq);
        	if(n!=null) {
        	if(n.left!=null) {
        	q.add(n.left);}
        	if(n.right!=null) {
        	q.add(n.right);}
        	ArrayList<Job> k= (ArrayList<Job>) n.getValues();
        	
        	if(k!=null) {
        		for(int i=0;i<k.size();i++) {
        			Job j=k.get(i);
        			if(j.priority>=Integer.parseInt(s)) {
        				a.add(j);
        			}
        		}
        	}
        	}
        }
      
        //System.out.println(left);
        /*for(int i=0;i<teml.size();i++) {
        	Job j=teml.get(i);
        	if(j.priority>=Integer.parseInt(s)) {
        		a.add(j);
        	}
        }*/
        for(int i=0;i<jobs.heap.size();i++) {
        	Job j=jobs.heap.get(i).a;
        	int x=Integer.parseInt(s);
        	if(j.priority>=x) {
        		a.add(j);
        	}
        }
        //System.out.println(teml.size());
        //System.out.println(a);
        return a;
    }

    private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
    	ArrayList<JobReport_> a=new ArrayList<JobReport_>();
        ArrayList<Job> b = (ArrayList<Job>) u2.search(cmd[2]).getValues();
        //System.out.println(cmd[2]);
        for(int i=0;i<b.size();i++) {
        	Job j=b.get(i);
        	if(j.Project.compareTo(cmd[1])==0&&search(compl.search(cmd[1]).getValues(),j.name)!=null) {
        	if(j.arrival_time()>=Integer.parseInt(cmd[3])&&j.arrival_time()<=Integer.parseInt(cmd[4])) {
        		a.add(j);
        	}
        	}
        	
        }
        for(int i=0;i<b.size();i++) {
        	Job j=b.get(i);
        	if(j.Project.compareTo(cmd[1])==0&&search(compl.search(cmd[1]).getValues(),j.name)==null) {
        	if(j.arrival_time()>=Integer.parseInt(cmd[3])&&j.arrival_time()<=Integer.parseInt(cmd[4])) {
        		a.add(j);
        	}
        	}
        	
        }
        //System.out.println(a);
        return a;
    }

    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
    	ArrayList<JobReport_> a=new ArrayList<JobReport_>();
        ArrayList<Job> b = (ArrayList<Job>) u2.search(cmd[1]).getValues();
        if(b!=null) {
        for(int i=0;i<b.size();i++) {
        	Job j=b.get(i);
        	if(j.arrival_time()>=Integer.parseInt(cmd[2])&&j.arrival_time()<=Integer.parseInt(cmd[3])) {
        		a.add(j);
        	}
        }}
       // System.out.println(a);
        return a;
    }

    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
    	ArrayList<JobReport_> a=new ArrayList<JobReport_>();
       // ArrayList<Job> b = (ArrayList<Job>) p2.search(cmd[1]).getValues();
    	TrieNode<Project> t=projects.search(cmd[1]);
    	ArrayList<Job>b;
    	if(t!=null) {
    		b=t.data.jobs2;
    	}
    	else {
    		b=null;
    	}
    	int x;
    	if(b==null) {
    		x=0;
    	}
    	else {
    		x=b.size();
    	}
        for(int i=0;i<x;i++) {
        	Job j=b.get(i);
        	//System.out.println(j.name);
        	//System.out.println(j.atime);
        	if(j.arrival_time()>=Integer.parseInt(cmd[2])&&j.arrival_time()<=Integer.parseInt(cmd[3])) {
        		a.add(j);
        	}
        }
       //System.out.println(a);
        return a;
    }

    private Job search(List<Job> a,String name) {
    	if(a==null) {
    		return null;
    	}
    	else {
    	for(int i=0;i<a.size();i++) {
    		//System.out.println(a.get(i).name);
    		//System.out.println(name);
    		if(a.get(i).name.compareTo(name)==0) {
    			return a.get(i);
    		}
    	}
    	return null;}
    }


    public void schedule() {
            execute_a_job();
    }
    int rtc=0;
    public void run_to_completion() {
    	int w=0;
    	
    	while (jobs.heap.size()>0) {
    		/*for(int i=0;i< jobs.heap.size();i++) {
        		System.out.println(jobs.heap.get(i).a.name);
        	}*/
    		if(rtc==0) {
    			System.out.println("Running code");
    	    	System.out.println("Remaining jobs: "+Integer.toString(jobs.heap.size()));
    		}
    		Job a=jobs.extractMax();
    		w=0;
    		String s="Executing: "+a.name+" from: "+a.Project;
    		System.out.println(s);
    		
    		Project p=projects.search(a.Project).getValue();
    		if(a.runtime>p.budget) {
        		rtc=1;
        		//left.add(a);
        		//teml.add(a);
        		teml.insert(a.Project,a);
        		System.out.println("Un-sufficient budget.");
        		//continue;
        	}
    		else {
    			rtc=0;
    			w=1;
    			endtime=endtime+a.runtime;
        		a.endtime=endtime;
        		String user=a.user;
        		int k=users.search(user).getValue().consumed;
        		search(u2.search(a.user).getValues(),a.name).endtime=endtime;
        		search(projects.search(a.Project).data.jobs2,a.name).endtime=endtime;
        		users.search(user).getValue().consumed=k+a.runtime;
        		users.search(user).getValue().latest_time=endtime;
        		a.JobStatus="COMPLETED";
        		comp.add(a);
        		temc.add(a);
        		compl.insert(a.name, a);
        		p.budget=p.budget-a.runtime;
        		
        		String s1="Project: "+p.name+" budget remaining: "+Integer.toString(p.budget);
        		System.out.println(s1);
        		System.out.println("System execution completed");
    		}
    	}
    	
    	if(w==0) {
    			System.out.println("System execution completed");
    	}
    }

    public void print_stats() {
    	int counter=0;
    	System.out.println("--------------STATS---------------");
    	System.out.println("Total jobs done: "+Integer.toString(comp.size()));
    	for(int i=0;i<comp.size();i++) {
    		Job a=comp.get(i);
    		String s="Job{user='"+a.user+"', project='"+a.Project+"', jobstatus="+a.JobStatus+", execution_time="+a.runtime+", end_time="+a.endtime+", name='"+a.name+"'}";
    		System.out.println(s);
    	}
    	System.out.println("------------------------");
    	System.out.println("Unfinished jobs: ");
    	/*for(int i=0;i<left.size();i++) {
    		Job a=left.get(i);
    		if(search(teml.search(a.Project).objects,a.name)!=null) {
    			counter++;
    			String s="Job{user='"+a.user+"', project='"+a.Project+"', jobstatus="+a.JobStatus+", execution_time="+a.runtime+", end_time="+"null"+", name='"+a.name+"'}";
    			System.out.println(s);
    		}
    	}*/
    	//MaxHeap<Job> h=new MaxHeap<Job>();
    	while(pheap.heap.size()!=0) {
    	MaxHeap<Job> h=new MaxHeap<Job>();
    	Project p=pheap.extractMax();
    	RedBlackNode<String,Job> r=teml.search(p.name);
    	ArrayList<Job> ar=(ArrayList<Job>) r.getValues();
    	int x;
    	if(ar!=null) {
    		x=ar.size();
    	}
    	else {
    		x=0;
    	}
    	if(ar!=null) {
    		
    		for(int i=0;i<x;i++) {
    			h.insert(ar.get(i));
    		}
    	}
    	for(int i=0;i<x;i++) {
    		Job a=h.extractMax();
    		counter++;
			String s="Job{user='"+a.user+"', project='"+a.Project+"', jobstatus="+a.JobStatus+", execution_time="+a.runtime+", end_time="+"null"+", name='"+a.name+"'}";
			System.out.println(s);
    	}
    	}
    	System.out.println("Total unfinished jobs: "+Integer.toString(counter));
    	System.out.println("--------------STATS DONE---------------");
    }

    public void handle_add(String[] cmd) {
    	System.out.println("ADDING Budget");
    	Project p=projects.search(cmd[1]).getValue();
    	p.budget=p.budget+Integer.parseInt(cmd[2]);
    	/*for(int k=0;k<p.jobs2.size();k++) {
    		 Job t=search(teml,p.jobs2.get(k).name);
    		
    		 if(t!=null) {
    			 teml.remove(t);
    			 jobs.insert(t);
    		}
    	}*/
    	ArrayList<Job>a=(ArrayList<Job>) teml.search(cmd[1]).objects;
    	if(a!=null) {
    		for(int i=0;i<a.size();i++) {
    			jobs.insert(a.get(i));
    		}
    		teml.search(cmd[1]).objects=null;
    	}
    	
    }
    int hel=0;
    public void handle_empty_line() {
    	if(jobs.heap.size()>0) {
        	if(hel==0) {
        	System.out.println("Running code");
        	System.out.println("Remaining jobs: "+Integer.toString(jobs.heap.size()));}
        	Job a=jobs.extractMax();
        	String s="Executing: "+a.name+" from: "+a.Project;
        	System.out.println(s);
        	Project p=projects.search(a.Project).getValue();
        	if(a.runtime>p.budget) {
        		hel=1;
        		//left.add(a);
        		//teml.add(a);
        		teml.insert(a.Project,a);
        		System.out.println("Un-sufficient budget.");
        		handle_empty_line();
        	}
        	else {
        		hel=0;
        		endtime=endtime+a.runtime;
        		a.endtime=endtime;
        		String user=a.user;
        		search(u2.search(a.user).getValues(),a.name).endtime=endtime;
        		search(projects.search(a.Project).data.jobs2,a.name).endtime=endtime;
        		int k=users.search(user).getValue().consumed;
        		users.search(user).getValue().consumed=k+a.runtime;
        		users.search(user).getValue().latest_time=endtime;
        		a.JobStatus="COMPLETED";
        		comp.add(a);
        		temc.add(a);
        		compl.insert(a.Project,a);
        		p.budget=p.budget-a.runtime;
        		
        		String s1="Project: "+p.name+" budget remaining: "+Integer.toString(p.budget);
        		System.out.println(s1);
        		System.out.println("Execution cycle completed");
        	}
        	}
        	else {
        		if(hel==0) {
        		System.out.println("Running code");
        		System.out.println("Remaining jobs: 0");}
        		else {
        			hel=0;
        		}
        		System.out.println("Execution cycle completed");
        	}
    }
    private Job search(RedBlackNode<String,Job> curr,String key) {
    	ArrayList<Job> l=(ArrayList<Job>) curr.objects;
    	if(l!=null) {
    	for(int i=0;i<l.size();i++) {
    		if(l.get(i).name.compareTo(key)==0) {
    			return l.get(i);
    		}
    	}
    	if(curr.left!=null) {
    		Job left=search(curr.left,key);
    		if(left!=null) {
    			return left;
    		}
    	}
    	if(curr.right!=null) {
    		Job right=search(curr.right,key);
    		if(right!=null) {
    			return right;
    		}
    	}}
    	return null;
    }
    private Job search2(RBTree<String,Job> a,String key) {
    	return search(a.root,key);
    }

    public void handle_query(String key) {
    	System.out.println("Querying");
    	String s=key+": ";
    	//Job a=new Job(key,0,null,null);
    	if(search2(teml,key)!=null) {
    		s=s+"REQUESTED";
    	}
    	
    	else if(search(comp,key)!=null) {
    		s=s+"COMPLETED";
    	}
    	else if(jobs.contains(new Job(key,0,null,null))){
    		s=s+"NOT FINISHED";
    	}
    	else {
    		s=s+"NO SUCH JOB";
    	}
    	System.out.println(s);
    }
    int ucount=0;
    public void handle_user(String name) {
    	User a=new User(name);
    	a.count=ucount;
    	ucount++;
    	System.out.println("Creating user");
    	users.insert(name,a);
    }
    int count =0;
    public void handle_job(String[] cmd) {
    	System.out.println("Creating job");
    	if(projects.search(cmd[2])!=null&&users.search(cmd[3]).getValue()!=null){
    		Job a=new Job(cmd[1],Integer.parseInt(cmd[4]),cmd[3],cmd[2]);
    		a.count=count;
    		a.atime=endtime;
    		count++;
    		Project p=projects.search(cmd[2]).getValue();
    		a.priority=p.priority;
    		jobs.insert(a);
    		projects.search(cmd[2]).getValue().jobs2.add(a);
    		//p2.insert(cmd[2], a);
    		u2.insert(cmd[3], a);
    		
    	}
    	else {
    		if(projects.search(cmd[2])==null) {
    			System.out.println("No such project exists. "+cmd[2]);
    		}
    		if(users.search(cmd[3]).getValue()==null) {
    			System.out.println("No such user exists: "+cmd[3]);
    		}
    	}
    }
    int pcount=0;
    public void handle_project(String[] cmd) {
    	Project p=new Project(cmd[1],Integer.parseInt(cmd[3]),Integer.parseInt(cmd[2]));
    	p.count=pcount;
    	count++;
    	System.out.println("Creating project");
    	projects.insert(cmd[1], p);
    	pheap.insert(p);
    	teml.insert(cmd[1], null);
    }

    public void execute_a_job() {

    }
    public void timed_handle_user(String name){
    	User a=new User(name);
    	a.count=ucount;
    	ucount++;
    	//System.out.println("Creating user");
    	users.insert(name,a);
    }
    public void timed_handle_job(String[] cmd){
    	//System.out.println("Creating job");
    	if(projects.search(cmd[2])!=null&&users.search(cmd[3]).getValue()!=null){
    		Job a=new Job(cmd[1],Integer.parseInt(cmd[4]),cmd[3],cmd[2]);
    		a.count=count;
    		a.atime=endtime;
    		count++;
    		Project p=projects.search(cmd[2]).getValue();
    		a.priority=p.priority;
    		jobs.insert(a);
    		projects.search(cmd[2]).getValue().jobs2.add(a);
    		//p2.insert(cmd[2], a);
    		u2.insert(cmd[3], a);
    		
    	}
    	else {
    		if(projects.search(cmd[2])==null) {
    			//System.out.println("No such project exists. "+cmd[2]);
    		}
    		if(users.search(cmd[3]).getValue()==null) {
    			//System.out.println("No such user exists: "+cmd[3]);
    		}
    	}
    }
    public void timed_handle_project(String[] cmd){
    	Project p=new Project(cmd[1],Integer.parseInt(cmd[3]),Integer.parseInt(cmd[2]));
    	p.count=pcount;
    	count++;
    	//System.out.println("Creating project");
    	projects.insert(cmd[1], p);
    	pheap.insert(p);
    	teml.insert(cmd[1], null);
    }
    public void timed_run_to_completion(){
    	int w=0;
    	
    	while (jobs.heap.size()>0) {
    		/*for(int i=0;i< jobs.heap.size();i++) {
        		System.out.println(jobs.heap.get(i).a.name);
        	}*/
    		if(rtc==0) {
    			//System.out.println("Running code");
    	    	//System.out.println("Remaining jobs: "+Integer.toString(jobs.heap.size()));
    		}
    		Job a=jobs.extractMax();
    		w=0;
    		String s="Executing: "+a.name+" from: "+a.Project;
    		//System.out.println(s);
    		
    		Project p=projects.search(a.Project).getValue();
    		if(a.runtime>p.budget) {
        		rtc=1;
        		//left.add(a);
        		//teml.add(a);
        		teml.insert(a.Project,a);
        		//System.out.println("Un-sufficient budget.");
        		//continue;
        	}
    		else {
    			rtc=0;
    			w=1;
    			endtime=endtime+a.runtime;
        		a.endtime=endtime;
        		String user=a.user;
        		search(u2.search(a.user).getValues(),a.name).endtime=endtime;
        		search(projects.search(a.Project).data.jobs2,a.name).endtime=endtime;
        		int k=users.search(user).getValue().consumed;
        		users.search(user).getValue().consumed=k+a.runtime;
        		users.search(user).getValue().latest_time=endtime;
        		a.JobStatus="COMPLETED";
        		comp.add(a);
        		temc.add(a);
        		compl.insert(a.name, a);
        		p.budget=p.budget-a.runtime;
        		
        		String s1="Project: "+p.name+" budget remaining: "+Integer.toString(p.budget);
        		//System.out.println(s1);
        		//System.out.println("System execution completed");
    		}
    	}
    	
    	if(w==0) {
    			//System.out.println("System execution completed");
    	}
    }
}
