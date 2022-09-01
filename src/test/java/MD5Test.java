import edu.dlu.bysj.base.model.entity.Team;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author sinre
 * @create 04 07, 2022
 * @since 1.0.0
 */
public class MD5Test {
    @Test
    public void test(){
        String password = "111111";//明码
        String algorithmName = "MD5";//加密算法
        Object source = password;//要加密的密码

        Object salt = "22222226";//盐值，一般都是用户名或者userid，要保证唯一
        int hashIterations = 1024;//加密次数

        SimpleHash simpleHash = new SimpleHash(algorithmName,source,salt,hashIterations);
        System.out.println(simpleHash);//打印出经过盐值、加密次数、md5后的密码
    }
}
