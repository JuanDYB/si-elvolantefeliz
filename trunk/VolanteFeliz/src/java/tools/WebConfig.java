package tools;

/**
 *
 * @author Juan Díez-Yanguas Barber
 */
public class WebConfig {
    private int maxLoginAttempt;
    private int lockTime;
    private int IVA;

    public WebConfig(String maxLoginAttempt, String lockTime, String IVA) {
        this.maxLoginAttempt = Integer.valueOf(maxLoginAttempt);
        this.lockTime = Integer.valueOf(lockTime);
        this.IVA = Integer.valueOf(IVA);
    }

    public int getLockTime() {
        return lockTime;
    }

    public int getMaxLoginAttempt() {
        return maxLoginAttempt;
    }

    public int getIVA() {
        return IVA;
    }
    
}
