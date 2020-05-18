package timing.ukulele.curator.handler.release;

import timing.ukulele.curator.model.LockInfo;

/**
 * 释放锁超时的处理逻辑接口
 *
 * @author fengxici
 */
public interface ReleaseTimeoutHandler {
    /**
     * 锁释放处理
     */
    void handle(LockInfo lockInfo);
}
