package timing.ukulele.persistence.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库ID自增
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAutoIdModel extends BaseModel {
    @TableId(value = "id_", type = IdType.AUTO)
    private Long id;
}
