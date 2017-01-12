package hu.berlin.user;
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

    private HashMap<String, HashMap<String, Object>> map;

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

    public void setBooleanForKey(boolean b, String key, String identifier) {
        this.serviceMap(identifier).put(key, b);
    }

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

    public void setStringForKey(String content, String key, String identifier) {
        this.serviceMap(identifier).put(key, content);
    }

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

    public void setIntForKey(int i, String key, String identifier) {
        this.serviceMap(identifier).put(key, i);
    }

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
