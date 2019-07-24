package com.github.sophearychiv.nevereatalone;

import java.util.Collection;
import java.util.HashMap;

public class InterestServiceMapImp implements InterestedService {
    private HashMap<String, Interest> interestedMap;

    public InterestServiceMapImp() {
        interestedMap = new HashMap<>();
    }

    @Override
    public void addInterested(Interest interested) {
        interestedMap.put(interested.getId(), interested);
    }

    @Override
    public Collection<Interest> getInterested() {
        return interestedMap.values();
    }

    @Override
    public Interest getInterested(String id) {
        return interestedMap.get(id);
    }

    @Override
    public Interest editInterested(Interest forEdit) throws UserException {
        try {
            if (forEdit.getId() == null)
                throw new UserException("ID cannot be blank");

            Interest toEdit = interestedMap.get(forEdit.getId());

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