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

    void setBooleanForKey(boolean b, String key);
    boolean getBooleanForKey(String key) throws ProfileException;

    void setStringForKey(String content, String key);
    String getStringForKey(String key) throws ProfileException;

    void setIntForKey(int i, String key);
    int getIntForKey(String key) throws ProfileException;
}
