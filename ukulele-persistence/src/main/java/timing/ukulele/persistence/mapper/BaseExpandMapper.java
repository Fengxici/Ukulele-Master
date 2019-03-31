package timing.ukulele.persistence.mapper;

import com.github.pagehelper.Page;

import java.util.Map;

public interface BaseExpandMapper {
    /**
     * 条件分页查询
     *
     * @param params 查询条件的Map
     * @return 分页对象
     */
    Page<Integer> query(Map<String, Object> params);
}
