package edu.dlu.bysj.paper.model.dto;

import edu.dlu.bysj.base.model.entity.Process;
import lombok.Data;

import java.util.List;

/**
 * @author sinre
 * @create 05 15, 2022
 * @since 1.0.0
 */
@Data
public class ProcessListDto {
    private String subjectId;
    private List<Process> processList;
}
