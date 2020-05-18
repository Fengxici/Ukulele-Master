package timing.ukulele.persistence.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;
import timing.ukulele.persistence.model.BaseModel;

import java.util.Collection;
import java.util.Date;

/**
 * @author fengxici
 */
public class BaseService<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> {

    @Override
    public boolean save(T entity) {
        Date now = new Date();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        if (entity.getDeleted() == null) {
            entity.setDeleted(Boolean.FALSE);
        }
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Date now = new Date();
        entityList.forEach(entity -> {
            entity.setUpdateTime(now);
            entity.setCreateTime(now);
            if (entity.getDeleted() == null) {
                entity.setDeleted(Boolean.FALSE);
            }
        });
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        Date now = new Date();
        entity.setUpdateTime(now);
        if (entity.getId() == null) {
            entity.setCreateTime(now);
            if (entity.getDeleted() == null) {
                entity.setDeleted(Boolean.FALSE);
            }
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Date now = new Date();
        entityList.forEach(entity -> {
            entity.setUpdateTime(now);
            if (entity.getId() == null) {
                entity.setCreateTime(now);
                if (entity.getDeleted() == null) {
                    entity.setDeleted(Boolean.FALSE);
                }
            }
        });
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Date now = new Date();
        entityList.forEach(entity -> {
            entity.setUpdateTime(now);
            if (entity.getId() == null) {
                entity.setCreateTime(now);
                if (entity.getDeleted() == null) {
                    entity.setDeleted(Boolean.FALSE);
                }
            }
        });
        return super.saveOrUpdateBatch(entityList, batchSize);
    }

    @Override
    public boolean updateById(T entity) {
        entity.setUpdateTime(new Date());
        return super.updateById(entity);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        entity.setUpdateTime(new Date());
        return super.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        Date now = new Date();
        entityList.forEach(entity -> entity.setUpdateTime(now));
        return super.updateBatchById(entityList, batchSize);
    }
}



