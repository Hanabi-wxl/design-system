package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author XiangXinGang
 * @date 2021/10/9 10:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeListQuery  extends CommonPage {

    @Builder
    public NoticeListQuery(Integer pageSize, Integer pageNumber, String search) {
        super(pageSize, pageNumber);
        this.search = search;
    }

    @ApiModelProperty(value = "标题搜索")
    private String search;
}
