package timing.ukulele.redisson.lock.handler.release;

import timing.ukulele.redisson.lock.model.LockInfo;

/**
 * 获取锁超时的处理逻辑接口
 *
 * @author fengxici*/
public interface ReleaseTimeoutHandler {

    void handle(LockInfo lockInfo);
}
