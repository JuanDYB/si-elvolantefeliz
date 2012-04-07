package tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class WebConfig {
    private int maxLoginAttempt;
    private int lockTime;

    public WebConfig(String maxLoginAttempt, String lockTime) {
        this.maxLoginAttempt = Integer.valueOf(maxLoginAttempt);
        this.lockTime = Integer.valueOf(lockTime);
    }

    public int getLockTime() {
        return lockTime;
    }

    public int getMaxLoginAttempt() {
        return maxLoginAttempt;
    }
    
}
