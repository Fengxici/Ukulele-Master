package timing.ukulele.web.controller;

import timing.ukulele.common.data.ResponseVO;

import java.util.List;

public abstract class CrudController<T> {

    /**
     * 查询
     */
    abstract protected ResponseVO<T> queryRecord(T query);

    abstract protected ResponseVO<T> queryRecordById(Long id);

    /**
     * 添加记录R除记录
     */
    abstract protected ResponseVO<T> deleteRecord(List<T> record);

    /**
     * 更新记录
     */
    abstract protected ResponseVO<T> updateRecord(T record);


}
