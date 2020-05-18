package timing.ukulele.persistence.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;
import timing.ukulele.persistence.model.BaseModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 模式使用从库，增删改使用主库
 *
 * @param <T>
 * @author fengxici
 */
@DS("slave")
public class BaseService<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> {

    @DS("master")
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

    @DS("master")
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

    @DS("master")
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

    @DS("master")
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

    @DS("master")
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

    @DS("master")
    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @DS("master")
    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @DS("master")
    @Override
    public boolean remove(Wrapper<T> wrapper) {
        return super.remove(wrapper);
    }

    @DS("master")
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    @DS("master")
    @Override
    public boolean updateById(T entity) {
        entity.setUpdateTime(new Date());
        return super.updateById(entity);
    }

    @DS("master")
    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        entity.setUpdateTime(new Date());
        return super.update(entity, updateWrapper);
    }

    @DS("master")
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



