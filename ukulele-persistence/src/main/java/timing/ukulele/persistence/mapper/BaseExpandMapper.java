package timing.ukulele.persistence.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * @author fengxici
 */
public interface BaseExpandMapper {
    /**
     * 条件分页查询
     *
     * @param params 查询条件的Map
     * @return 分页对象
     */
    IPage<Integer> query(Map<String, Object> params);
}
