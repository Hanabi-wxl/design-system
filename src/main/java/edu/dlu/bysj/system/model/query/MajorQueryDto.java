package edu.dlu.bysj.system.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sinre
 * @create 04 25, 2022
 * @since 1.0.0
 */
@Data
public class MajorQueryDto implements Serializable {
    private String collegeId;
    private Integer pageSize;
    private Integer pageNumber;
}
