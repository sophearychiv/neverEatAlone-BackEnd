package neverEatAlone;

import java.util.Collection;

public interface InterestedService {
	
    public void addInterested(Interest interested);

    public Collection<Interest> getInterested();

    public Interest getInterested(String id);

    public Interest editInterested(Interest interested) throws UserException;

    public void deleteInterested(String id);

    public boolean InterestedExist(String id);
}