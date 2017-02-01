package hu.berlin.user;
import hu.berlin.dialog.clause.predicate.Predicate;
import hu.berlin.dialog.clause.predicate.Predicate.Instance;
import java.util.HashMap;

/**
 * Created by Duc on 10.01.17.
 */
public class UserProfile implements Profile, Instance {

    class UserProfileException extends ProfileException {
        public UserProfileException(String message) {
            super(message);
        }
    }

    final private static String kUserProfilePredicateIdentifier = "predicateIdentifier";

    private HashMap<String, HashMap<String, Object>> map;
    private HashMap<String, Boolean> predicateMap;

    public UserProfile() {
        super();
        this.map = new HashMap<String, HashMap<String, Object>>();
    }

    private HashMap<String, Object> serviceMap(String identifier) {
        HashMap <String, Object> serviceMap = this.map.get(identifier);

        if (serviceMap == null) {
            serviceMap = new HashMap<String, Object>();
            this.map.put(identifier, serviceMap);
        }

        return serviceMap;
    }

    // Instance implementation
    private HashMap<String, Boolean> getPredicateMap() {
        HashMap map = this.serviceMap(kUserProfilePredicateIdentifier);
        return map;
    }

    @Override
    public void setValueForPredicate(Predicate p, boolean val) {
        this.getPredicateMap().put(p.getIdentifier(), val);
    }

    @Override
    public boolean getValueForPredicate(Predicate p) {
        return this.getPredicateMap().get(p.getIdentifier());
    }

    @Override
    public boolean predicateWasSet(Predicate p) {
        return this.getPredicateMap().get(p.getIdentifier()) != null;
    }

    // Profile implementation
    @Override
    public void setBooleanForKey(boolean b, String key, String identifier) {
        this.serviceMap(identifier).put(key, b);
    }

    @Override
    public boolean getBooleanForKey(String key, String identifier) throws UserProfileException {
        Object o = this.serviceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } else if (o instanceof Boolean) {
            return (Boolean)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to boolean!");
        }
    }

    @Override
    public void setStringForKey(String content, String key, String identifier) {
        this.serviceMap(identifier).put(key, content);
    }

    @Override
    public String getStringForKey(String key, String identifier) throws UserProfileException {
        Object o = this.serviceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } else if (o instanceof String) {
            return (String)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to String!");
        }
    }

    @Override
    public void setIntForKey(int i, String key, String identifier) {
        this.serviceMap(identifier).put(key, i);
    }

    @Override
    public int getIntForKey(String key, String identifier) throws UserProfileException {
        Object o = this.serviceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } if (o instanceof Integer) {
            return (Integer) o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to int!");
        }
    }

}
