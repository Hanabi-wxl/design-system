package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Log对象", description="日志表")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期")
    private LocalDateTime date;

    @ApiModelProperty(value = "是否学生")
    private Integer isStudent;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "日志类型（0：操作日志，1：错误日志，2：登录日志）")
    private Integer type;

    @ApiModelProperty(value = "日志内容")
    private String message;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
