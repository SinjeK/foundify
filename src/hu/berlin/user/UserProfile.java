package hu.berlin.user;
import java.util.HashMap;

/**
 * Created by Duc on 10.01.17.
 */
public class UserProfile implements Profile {

    class UserProfileException extends ProfileException {
        public UserProfileException(String message) {
            super(message);
        }
    }

    private HashMap<String, Object> map;

    public UserProfile() {
        super();
        this.map = new HashMap<String, Object>();
    }

    public void setBooleanForKey(boolean b, String key) {
        this.map.put(key, b);
    }

    public boolean getBooleanForKey(String key) throws UserProfileException {
        Object o;
        if ((o = this.map.get(key)) instanceof Boolean) {
            return (Boolean)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to boolean!");
        }
    }

    public void setStringForKey(String content, String key) {
        this.map.put(key, content);
    }

    public String getStringForKey(String key) throws UserProfileException {
        Object o;
        if ((o = this.map.get(key)) instanceof String) {
            return (String)o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to String!");
        }
    }

    public void setIntForKey(int i, String key) {
        this.map.put(key, i);
    }

    public int getIntForKey(String key) throws UserProfileException {
        Object o;
        if ((o = this.map.get(key)) instanceof Integer) {
            return (Integer) o;
        } else {
            throw new UserProfileException("Cannot convert object at key " + key + " to int!");
        }
    }

}
