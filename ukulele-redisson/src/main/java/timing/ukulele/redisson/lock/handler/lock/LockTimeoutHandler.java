package timing.ukulele.redisson.lock.handler.lock;

import org.aspectj.lang.JoinPoint;
import timing.ukulele.redisson.lock.Lock;
import timing.ukulele.redisson.lock.model.LockInfo;

/**
 * 获取锁超时的处理逻辑接口
 *
 * @author fengxici*/
public interface LockTimeoutHandler {

    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}
