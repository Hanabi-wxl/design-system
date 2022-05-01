package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 11:16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserListQuery extends CommonPage {
    @Builder
    public UserListQuery(Integer pageSize, Integer pageNumber, String isStudent, String majorId, String userName, Integer userNumber) {
        super(pageSize, pageNumber);
        this.isStudent = isStudent;
        this.majorId = majorId;
        this.userName = userName;
        this.userNumber = userNumber;
    }

    @ApiModelProperty(value = "是否学生（0：学生,1老师）")
    private String isStudent;

    @ApiModelProperty(value = "专业id")
    private String majorId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "用户编号")
    private Integer userNumber;

}
