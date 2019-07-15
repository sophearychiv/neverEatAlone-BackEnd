package neverEatAlone;

import java.util.Collection;

public interface InterestedService {
	
    public void addInterested(Interested interested);

    public Collection<Interested> getInterested();

    public Interested getInterested(String id);

    public Interested editInterested(Interested interested) throws UserException;

    public void deleteInterested(String id);

    public boolean InterestedExist(String id);
}