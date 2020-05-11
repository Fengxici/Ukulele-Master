package timing.ukulele.persistence.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户输入ID
 * <p>该类型可以通过自己注册自动填充插件进行填充</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseInputIdModel extends BaseModel {
    @TableId(value = "id_", type = IdType.INPUT)
    private Long id;
}
