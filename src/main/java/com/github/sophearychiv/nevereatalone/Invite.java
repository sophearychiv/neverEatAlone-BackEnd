package com.github.sophearychiv.nevereatalone;

public class Invite {
	private String inviteId;
	private String requesterFbId;
	private String receipientFbId;
	private String restYelpId;
	private String creationDate;
	private String mealStartDate;
	private String mealEndDate;
    

    public Invite(String inviteId, String requesterFbId, String receipientFbId, String restYelpId, String creationDate, String mealStartDate, String mealEndDate) {
        super();
        this.inviteId = inviteId;
        this.requesterFbId = requesterFbId;
        this.receipientFbId = receipientFbId;
        this.restYelpId = restYelpId;
        this.creationDate = creationDate;
        this.mealStartDate = mealStartDate;
        this.mealEndDate = mealEndDate;
        
    }
    
    public String getInviteId() {
    	return inviteId;
    }
    
    public void settInviteId(String inviteId) {
    	this.inviteId = inviteId;
    }

    public String getRequesterFbId() {
        return requesterFbId;
    }

    public void setRequesterFbId(String requesterFbId) {
        this.requesterFbId = requesterFbId;
    }
    
    public String getReceipientFbId() {
        return receipientFbId;
    }

    public void setReceipientFbId(String receipientFbId) {
        this.receipientFbId = receipientFbId;
    }
    
    public String getRestYelpId() {
        return restYelpId;
    }

    public void setRestYelpId(String restYelpId) {
        this.restYelpId = restYelpId;
    }
    
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getMealStartDate() {
        return mealStartDate;
    }

    public void setMealStartDate(String mealStartDate) {
        this.mealStartDate = mealStartDate;
    }
    
    public String getMealEndDate() {
        return mealEndDate;
    }

    public void setMealEndDate(String mealEndDate) {
        this.mealEndDate = mealStartDate;
    }
    
    @Override
    public String toString() {
        return new StringBuffer().append(getReceipientFbId()).toString();
    }
	

}
