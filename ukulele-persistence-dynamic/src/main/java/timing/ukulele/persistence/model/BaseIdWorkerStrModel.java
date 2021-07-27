package timing.ukulele.persistence.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全字符串全局唯一ID (idWorker 的字符串表示)
 * 只有当插入对象ID 为空，才自动填充
 * @author fengxici
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseIdWorkerStrModel extends BaseModel<String> {
    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;
}
