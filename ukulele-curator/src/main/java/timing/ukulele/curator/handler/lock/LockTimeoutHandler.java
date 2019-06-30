package timing.ukulele.curator.handler.lock;

import org.aspectj.lang.JoinPoint;
import timing.ukulele.curator.lock.Lock;
import timing.ukulele.curator.model.LockInfo;

/**
 * 获取锁超时的处理逻辑接口
 **/
public interface LockTimeoutHandler {

    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}
