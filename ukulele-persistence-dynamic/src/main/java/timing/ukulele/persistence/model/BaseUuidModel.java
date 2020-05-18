package timing.ukulele.persistence.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局唯一ID (UUID)
 * 只有当插入对象ID 为空，才自动填充
 * @author fengxici
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUuidModel extends BaseModel {
    @TableId(value = "id_", type = IdType.UUID)
    private Long id;
}
