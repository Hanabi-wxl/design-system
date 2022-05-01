package edu.dlu.bysj.security.model.token;

import edu.dlu.bysj.base.util.JwtUtil;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/10 22:26
 */
public class JwtToken  implements AuthenticationToken {
    /**
     * 加密后的 jwt token 串
     */
    private String token;
    /**
     * 角色ids;
     */
    private List<Integer> roleIds;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public JwtToken(String token) {
        this.token = token;
        this.roleIds =JwtUtil.getRoleIds(token);
    }

    public JwtToken(String username, String password, List<Integer> roleIds) {
        this.password = password;
        this.username = username;
        this.roleIds = roleIds;
    }


    @Override
    public Object getPrincipal() {
        return this.roleIds;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
