package hu.berlin.user;
import hu.berlin.dialog.clause.Predicates.PredicateConstants;
import java.util.HashMap;

/**
 * Created by Duc on 10.01.17.
 */
public class UserProfile {

    class UserProfileException extends Exception {
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

    private HashMap<String, Object> getServiceMap(String identifier) {
        HashMap <String, Object> serviceMap = this.map.get(identifier);

        if (serviceMap == null) {
            serviceMap = new HashMap<String, Object>();
            this.map.put(identifier, serviceMap);
        }

        return serviceMap;
    }

    public void setIntForPredicate(int i, PredicateConstants predicate) {
        this.setIntForKey(i, predicate.name(), kUserProfilePredicateIdentifier);
    }

    public int getIntForPredicate(PredicateConstants predicate) {
        try {
            return this.getIntForKey(predicate.name(), kUserProfilePredicateIdentifier);
        } catch (UserProfileException e) {
            e.printStackTrace();
            System.exit(-1);
            return 0;
        }
    }

    public void setValueForPredicate(boolean value, PredicateConstants predicate) {
        this.setBooleanForKey(value, predicate.name(), kUserProfilePredicateIdentifier);
    }

    public boolean getValueForPredicate(PredicateConstants predicate) {
        try {
            if (this.doesValueAtPredicateExist(predicate)) {
                return this.getBooleanForKey(predicate.name(), kUserProfilePredicateIdentifier);
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            System.exit(-1);
            return false;
        }
    }

    public boolean doesValueAtPredicateExist(PredicateConstants predicate) {
        Object o = this.getServiceMap(kUserProfilePredicateIdentifier).get(predicate.name());
        return o!=null;
    }

    // Profile implementation
    private void setBooleanForKey(boolean b, String key, String identifier) {
        this.getServiceMap(identifier).put(key, b);
    }

    private boolean getBooleanForKey(String key, String identifier) throws UserProfileException {
        Object o = this.getServiceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } else if (o instanceof Boolean) {
            return (Boolean)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to boolean!");
        }
    }

    private void setStringForKey(String content, String key, String identifier) {
        this.getServiceMap(identifier).put(key, content);
    }

    private String getStringForKey(String key, String identifier) throws UserProfileException {
        Object o = this.getServiceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } else if (o instanceof String) {
            return (String)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to String!");
        }
    }

    private void setIntForKey(int i, String key, String identifier) {
        this.getServiceMap(identifier).put(key, i);
    }

    private int getIntForKey(String key, String identifier) throws UserProfileException {
        Object o = this.getServiceMap(identifier).get(key);
        if (o == null) {
            throw new UserProfileException("Object at key " + key + " is null!");
        } if (o instanceof Integer) {
            return (Integer) o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to int!");
        }
    }

}
