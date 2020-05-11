package timing.ukulele.persistence.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局唯一ID (idWorker)
 * 只有当插入对象ID 为空，才自动填充
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseIdWorkerModel extends BaseModel {
    @TableId(value = "id_", type = IdType.ID_WORKER)
    private Long id;
}
