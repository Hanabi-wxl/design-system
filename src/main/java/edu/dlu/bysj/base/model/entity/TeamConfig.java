package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 答辩分组配置
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TeamConfig对象", description="答辩分组配置")
public class TeamConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "年级")
    private Integer grade;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "学生答辩分组规则（0：指导教师与学生同组 1 ：互评教师与学生同组 ，2 ：不与指导教师同组：3：随机均分）")
    private Integer type;

    @ApiModelProperty(value = "此条记录是否有效(1:有效，0-：无效)")
    @TableLogic
    private Integer status;


}
