package edu.dlu.bysj.security.model.mathcer;

import cn.hutool.extra.expression.ExpressionException;
import edu.dlu.bysj.base.util.JwtUtil;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author XiangXinGang
 * @date 2021/10/11 22:17
 */
@Slf4j
public class JwtCredentialsMatcher  implements CredentialsMatcher {
    /**
     * 验证jwt 是否合法
     * @param authenticationToken token
     * @param authenticationInfo
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = authenticationToken.getCredentials().toString();
        //验证jwt
        try {
            JwtUtil.verify(token);
            return true;
        } catch (SignatureException e) {
            e.printStackTrace();
            log.info("token untruest");
        } catch (ExpressionException e) {
            e.printStackTrace();
            log.info("token express");
        }
        return false;
    }
}
