package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 22:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicsListQuery  extends CommonPage {
    @Builder
    public TopicsListQuery(Integer pageSize, Integer pageNumber, String teacherName, String subjectName) {
        super(pageSize, pageNumber);
        this.teacherName = teacherName;
        this.subjectName = subjectName;
    }
    @ApiModelProperty(value = "教师姓名（可为空)")
    private String teacherName;
    @ApiModelProperty(value = "题目名称（可为空)")
    private String subjectName;
}
