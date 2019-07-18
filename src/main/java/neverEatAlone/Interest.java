package neverEatAlone;

public class Interest {
	
		private String id;
		private String userId;
		private String restId;
	    

	    public Interest(String id, String userId, String restId) {
	        super();
	        this.id = id;
	        this.userId = userId;
	        this.restId = restId;
	        
	    }
	    
	    public String getId() {
	    	return id;
	    }
	    
	    public void setId(String id) {
	    	this.id = id;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }
	    
	    public String getRestId() {
	        return restId;
	    }

	    public void setRestId(String restId) {
	        this.restId = restId;
	    }
	    
	    @Override
	    public String toString() {
	        return new StringBuffer().append(getUserId()).toString();
	    }

}
