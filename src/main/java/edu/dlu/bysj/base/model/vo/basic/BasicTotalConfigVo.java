package edu.dlu.bysj.base.model.vo.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BasicTotalConfigVo {
    @ApiModelProperty(value = "答辩分组规则(0-本专业 1-全院 2-按班级)")
    private  Integer defenceRule;
    @ApiModelProperty(value = "申请答辩规则((0-学生自己申请 1-指定)")
    private Integer applyDefence;
    @ApiModelProperty(value = "开题报告类型(0-纯文字版     1-图文混排版（需上传)")
    private Integer openReportRule;
    @ApiModelProperty(value = "总评模板类型(0-按学校模板 1-自定义模板)")
    private Integer scoreRule;
    @ApiModelProperty(value = "互评老师分配规则(0-按专业自动分配 1-本专业手动分配 2-全院手动分配)")
    private Integer eachMark;
    @ApiModelProperty(value = "申请答辩是否携带毕业设计作品(0:不需要,1 :需要)")
    private Integer design;
}
