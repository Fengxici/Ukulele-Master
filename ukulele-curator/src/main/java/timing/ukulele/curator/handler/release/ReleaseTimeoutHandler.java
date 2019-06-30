package timing.ukulele.curator.handler.release;

import timing.ukulele.curator.model.LockInfo;

/**
 * 释放锁超时的处理逻辑接口
 **/
public interface ReleaseTimeoutHandler {
    void handle(LockInfo lockInfo);
}
