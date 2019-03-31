package timing.ukulele.persistence.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.lang.Nullable;
import timing.ukulele.persistence.mapper.BaseMapper;
import timing.ukulele.persistence.model.BaseModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@DS("slave")
public class BaseService<T extends BaseModel> extends ServiceImpl<BaseMapper<T>, T> {
    /**
     * 获取所有数据
     *
     * @return 所有数据 List
     */
    public List<T> getAll() {
        return baseMapper.selectList(new QueryWrapper<>());
//        return baseMapper.selectList(new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return null;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return null;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        });
    }

    /**
     * 根据对象中的属性之获取一条记录
     *
     * @param record
     * @return
     */
    public T queryOneByParam(final T record) {
        return getOne(new QueryWrapper<>(record));
//        return getOne(new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return record;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return null;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        });
    }

    /**
     * 根据where条件查询一条数据
     *
     * @param sqlSegment
     * @return
     */
    public T queryOneByParam(final Map<String, Object> sqlSegment) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(sqlSegment);
        return getOne(wrapper);
//        return getOne(new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return null;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return sqlSegment;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        });
    }

    /**
     * 根据对象属性获取列表
     *
     * @param record
     * @return
     */
    public List<T> listByParam(final T record) {

        Wrapper<T> wrapper = new QueryWrapper<>(record);
//        new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return record;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return null;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
        return list(wrapper);
    }

    /**
     * 根据where条件获取列表
     *
     * @param segment
     * @return
     */
    public List<T> listByParam(final Map<String, Object> segment) {
//        Wrapper<T> wrapper = new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return null;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return segment;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        };
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(segment);
        return list(wrapper);
    }

    /**
     * 获取分页
     *
     * @param record 分页条件
     * @return 分页数据 PageInfo
     */
    public IPage<T> pageInfo(final T record, @Nullable String[] asc, @Nullable String[] desc) {
        Page<T> page = new Page<>(record.getPageNum(), record.getPageSize());
        page.setAsc(asc);
        page.setDesc(desc);
        return page(page, new Wrapper<T>() {
            @Override
            public T getEntity() {
                return record;
            }

            @Override
            public MergeSegments getExpression() {
                return null;
            }

            @Override
            public String getCustomSqlSegment() {
                return null;
            }

            @Override
            public String getSqlSegment() {
                return null;
            }
        });
    }

    /**
     * 获取分页
     *
     * @param record  分页条件
     * @param segment 分页条件
     * @return 分页数据 PageInfo
     */
    public IPage<T> pageInfo(final T record, final Map<String, Object> segment, @Nullable String[] asc, @Nullable String[] desc) {
        Page<T> page = new Page<>(record.getPageNum(), record.getPageSize());
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(segment);
        return page(page, wrapper);
//        return page(page, new Wrapper<T>() {
//            @Override
//            public T getEntity() {
//                return null;
//            }
//
//            @Override
//            public MergeSegments getExpression() {
//                return null;
//            }
//
//            @Override
//            public String getCustomSqlSegment() {
//                return segment;
//            }
//
//            @Override
//            public String getSqlSegment() {
//                return null;
//            }
//        });
    }

    public QueryWrapper<T> getQueryWrapper() {
        return new QueryWrapper<>();
    }

    /**
     * 获取分页
     *
     * @param current 当前页
     * @param size    分页大小
     * @return 分页数据 PageInfo
     */
    public IPage<T> PageInfo(int current, int size, QueryWrapper<T> wrapper) {
        Page<T> page = new Page<>(current, size);
        return page(page, wrapper);
    }

    @DS("master")
    @Override
    public boolean save(T entity) {
        return super.save(entity);
    }

    @DS("master")
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @DS("master")
    @Override
    public boolean saveOrUpdate(T entity) {
        return super.saveOrUpdate(entity);
    }

    @DS("master")
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @DS("master")
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
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
        return super.updateById(entity);
    }

    @DS("master")
    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @DS("master")
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return super.updateBatchById(entityList, batchSize);
    }

    @Override
    public T getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    public Collection<T> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        return super.getOne(queryWrapper, throwEx);
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return super.getMap(queryWrapper);
    }

    @Override
    public int count(Wrapper<T> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return super.listMaps(queryWrapper);
    }

    @Override
    public List<Object> listObjs(Wrapper<T> queryWrapper) {
        return super.listObjs(queryWrapper);
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
        return super.pageMaps(page, queryWrapper);
    }
}



