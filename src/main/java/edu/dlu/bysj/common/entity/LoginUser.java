package edu.dlu.bysj.common.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author sinre
 * @create 04 07, 2022
 * @since 1.0.0
 */
@Data
public class LoginUser {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
