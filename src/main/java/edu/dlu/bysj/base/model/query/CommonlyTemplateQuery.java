package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/7 21:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonlyTemplateQuery extends CommonPage {
     @Builder
     public CommonlyTemplateQuery(Integer pageSize, Integer pageNumber, String userNumber, String userName, Integer year, Integer majorId, Integer type) {
          super(pageSize, pageNumber);
          this.userNumber = userNumber;
          this.userName = userName;
          this.year = year;
          this.majorId = majorId;
          this.type = type;
     }

     @ApiModelProperty(value = "学号或教工号")
     private String userNumber;
     @ApiModelProperty(value = "用户姓名")
     private String userName;
     @ApiModelProperty(value = "年份")
     private Integer year;
     @ApiModelProperty(value = "专业id")
     private Integer majorId;
     @ApiModelProperty(value = "用户类型 0:学生,1教师")
     private Integer type;
}
