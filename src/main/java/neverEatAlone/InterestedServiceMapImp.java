package neverEatAlone;

import java.util.Collection;
import java.util.HashMap;

public class InterestedServiceMapImp implements InterestedService {
    private HashMap<String, Interested> interestedMap;

    public InterestedServiceMapImp() {
        interestedMap = new HashMap<>();
    }

    @Override
    public void addInterested(Interested interested) {
        interestedMap.put(interested.getId(), interested);
    }

    @Override
    public Collection<Interested> getInterested() {
        return interestedMap.values();
    }

    @Override
    public Interested getInterested(String id) {
        return interestedMap.get(id);
    }

    @Override
    public Interested editInterested(Interested forEdit) throws UserException {
        try {
            if (forEdit.getId() == null)
                throw new UserException("ID cannot be blank");

            Interested toEdit = interestedMap.get(forEdit.getId());

            if (toEdit == null)
                throw new UserException("Interested not found");

            if (forEdit.getUserId() != null) {
                toEdit.setUserId(forEdit.getUserId());
            }
            if (forEdit.getRestId() != null) {
                toEdit.setRestId(forEdit.getRestId());
            }
            if (forEdit.getId() != null) {
                toEdit.setId(forEdit.getId());
            }

            return toEdit;
        } catch (Exception ex) {
            throw new UserException(ex.getMessage());
        }
    }

    @Override
    public void deleteInterested(String id) {
        interestedMap.remove(id);
    }

    @Override
    public boolean InterestedExist(String id) {
        return interestedMap.containsKey(id);
    }

}