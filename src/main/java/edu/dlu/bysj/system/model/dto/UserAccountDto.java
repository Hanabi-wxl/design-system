package edu.dlu.bysj.system.model.dto;

import lombok.Data;

/**
 * @author sinre
 * @create 05 01, 2022
 * @since 1.0.0
 */
@Data
public class UserAccountDto {
    private String password;
    private Integer userId;
    private Integer isStudent;
}
