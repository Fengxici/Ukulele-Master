package timing.ukulele.web.controller;

import com.github.pagehelper.PageInfo;
import timing.ukulele.common.data.ResponseData;
import timing.ukulele.common.data.TableData;

import java.util.List;

public abstract class CrudController<T> {

    /**
     * 表格查询
     */
    abstract protected ResponseData<TableData<T>> queryRecord(T query);

    /**
     * 添加记录
     */
    abstract protected ResponseData<T> addRecord(T record);

    /**
     * 批量删除记录
     */
    abstract protected ResponseData<T> deleteRecord(List<T> record);

    /**
     * 更新记录
     */
    abstract protected ResponseData<T> updateRecord(T record);

    /**
     * 返回表格数据
     */
    protected ResponseData<TableData<T>> getTableData(Integer code, String message, PageInfo<T> pageInfo) {
        TableData<T> data = new TableData<>();
        data.setTotal(pageInfo.getTotal());
        data.setRows(pageInfo.getList());
        return new ResponseData<>(code, message, data);
    }

    /**
     * 返回表格数据
     */
//    protected ResponseData<TableData<T>> getTableData(Integer code, String message, IPage<T> pageInfo) {
//        TableData<T> data = new TableData<>();
//        data.setTotal(pageInfo.getTotal());
//        data.setRows(pageInfo.getRecords());
//        return new ResponseData<>(code, message, data);
//    }
}
