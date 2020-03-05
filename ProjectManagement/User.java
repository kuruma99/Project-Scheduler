package ProjectManagement;


public class User implements Comparable<User>, UserReport_ {

	String name;
	int consumed=0;
	int latest_time=0;
	int count=0;

	User(String name){
		this.name=name;
		
	}
	
		public String user()    { return name; }

	   public int consumed() { return consumed; }
	
    public int compareTo(User user) {
        if(consumed<user.consumed) {
        	return -1;
        }
        else if(consumed>user.consumed) {
        	return 1;
        }
        else {
        	if(latest_time>user.latest_time) {
        		return -1;
        	}
        	else if(latest_time<user.latest_time){
        		return 1;
        	}
        	else {
        		if(count<user.count) {
        			return 1;
        		}
        		else {
        			return -1;
        		}
        	}
        }
    }
    public String toString() {
    	return name;
    }
}
