package timing.ukulele.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fengxici
 */
@Data
public abstract class BaseModel<T> implements Serializable, IdModel<T> {
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    @TableField("deleted_")
    private Boolean deleted;
}