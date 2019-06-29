package timing.ukulele.curator.model;

/**
 * Content :锁基本信息
 */
public class LockInfo {

    private LockType type;
    private String name;
    private long waitTime;

    public LockInfo() {
    }

    public LockInfo(LockType type, String name, long waitTime) {
        this.type = type;
        this.name = name;
        this.waitTime = waitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }
}
