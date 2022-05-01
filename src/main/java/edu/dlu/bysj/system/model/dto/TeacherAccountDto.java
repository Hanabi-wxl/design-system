package edu.dlu.bysj.system.model.dto;

import lombok.Data;

/**
 * @author sinre
 * @create 05 01, 2022
 * @since 1.0.0
 */
@Data
public class TeacherAccountDto extends UserAccountDto{
    private String teacherNumber;
}
