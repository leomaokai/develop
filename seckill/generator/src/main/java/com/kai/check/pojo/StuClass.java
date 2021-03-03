package com.kai.check.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_stu_class")
@ApiModel(value="StuClass对象", description="")
public class StuClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "学号")
    private String stuId;

    @ApiModelProperty(value = "班级id")
    private Integer classId;


}
