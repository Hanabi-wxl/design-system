package edu.dlu.bysj.base.model.dto;

import lombok.Data;

/**
 * @author sinre
 * @create 04 27, 2022
 * @since 1.0.0
 */
@Data
public class UserInitDto {
    private Integer userId;
    private String isStudent;
    private String newPassword;
    private String userNumber;
}
