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
    public TopicsListQuery(Integer pageSize, Integer pageNumber, String searchContent) {
        super(pageSize, pageNumber);
        this.searchContent = searchContent;
    }
    @ApiModelProperty(value = "搜索内容（可为空)")
    private String searchContent;
}
