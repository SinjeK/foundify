package hu.berlin.user;

/**
 * Created by Duc on 10.01.17.
 */
public interface Profile {

    class ProfileException extends Exception {
        ProfileException(String message) {
            super(message);
        }
    }

    void setBooleanForKey(boolean b, String key, String identifier);
    boolean getBooleanForKey(String key, String identifier) throws ProfileException;

    void setStringForKey(String content, String key, String identifier);
    String getStringForKey(String key, String identifier) throws ProfileException;

    void setIntForKey(int i, String key, String identifier);
    int getIntForKey(String key, String identifier) throws ProfileException;
}
