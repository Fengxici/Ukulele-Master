package timing.ukulele.persistence.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseModel implements Serializable {
    @TableId(value = "id_", type = IdType.ID_WORKER)
    private Long id;
    @TableField("deleted_")
    private Boolean deleted;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}