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
 * 答辩分组表
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */

/*
 * @Description: fix
 * @Author: sinre
 * @Date: 2022/6/10 20:41
 * @param null
 * @return
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Team对象", description="答辩分组表")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分组编号")
    private String teamNumber;

    @ApiModelProperty(value = "该组答辩地址")
    private String address;

    @ApiModelProperty(value = "该组答辩开始时间")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "该组答辩结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "年级")
    private Integer grade;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "该组答辩要求")
    private String request;

    @ApiModelProperty(value = "是否2答（1：参加二答，0：不参加二答）")
    private Integer isRepeat;

    @ApiModelProperty(value = "分组类型")
    private Integer type;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;


}
