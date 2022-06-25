package edu.dlu.bysj.system.model.query;

import edu.dlu.bysj.base.model.query.basic.CommonPage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author sinre
 * @create 04 25, 2022
 * @since 1.0.0
 */
@Data
public class MajorQueryDto extends CommonPage {
    @NotNull(message = "学院id不能为空")
    private Integer collegeId;
}
