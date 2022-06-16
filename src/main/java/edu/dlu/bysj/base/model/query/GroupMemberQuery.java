package edu.dlu.bysj.base.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import lombok.Data;

/**
 * @author sinre
 * @create 06 16, 2022
 * @since 1.0.0
 */
@Data
public class GroupMemberQuery extends CommonPage {
    private Integer groupId;
}
