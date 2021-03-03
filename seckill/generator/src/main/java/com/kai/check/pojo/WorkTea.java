package com.kai.check.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kai
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_work_tea")
@ApiModel(value="WorkTea对象", description="")
public class WorkTea implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "作业id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "作业描述")
    private String workDescribe;

    @ApiModelProperty(value = "班级id")
    private Integer classId;

    @ApiModelProperty(value = "老师id")
    private String teaId;

    @ApiModelProperty(value = "截至时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
