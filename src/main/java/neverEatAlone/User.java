package neverEatAlone;

public class User {
	
	private String id;
    private String firstName;
    private String lastName;
    private String email;
	private String fbId;
	private String photoUrl;

    public User(String id, String fbId, String firstName, String lastName, String email, String photoUrl) {
        super();
        this.id = id;
        this.fbId = fbId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getFbId() {
    	return fbId;
    }
    
    public void setFbId(String fbId) {
    	this.fbId = fbId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhotoUrl() {
    	return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
    	this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(getEmail()).toString();
    }

}
