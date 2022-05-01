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
 * 总配置
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TotalConfig对象", description="总配置")
public class TotalConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "年级")
    private Integer grade;

    @ApiModelProperty(value = "专业id")
    private Integer majorId;

    @ApiModelProperty(value = "答辩分组规则(0-本专业 1-全院 2-按班级)")
    private Integer defenseRule;

    @ApiModelProperty(value = "申请答辩规则((0-学生自己申请 1-指定)")
    private Integer applyDefense;

    @ApiModelProperty(value = "开题报告类型(0-纯文字版     1-图文混排版（需上传)")
    private Integer openreportRule;

    @ApiModelProperty(value = "总评模板类型(0-按学校模板 1-自定义模板)")
    private Integer scoreRule;

    @ApiModelProperty(value = "互评老师分配规则(0-按专业自动分配 1-本专业手动分配 2-全院手动分配)")
    private Integer eachmark;

    @ApiModelProperty(value = "申请答辩是否携带毕业设计作品(0:不需要,1 :需要)")
    private Integer design;

    @ApiModelProperty(value = "此条记录是否有效")
    @TableLogic
    private Integer status;


}
