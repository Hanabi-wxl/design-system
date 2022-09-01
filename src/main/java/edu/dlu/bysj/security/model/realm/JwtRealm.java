package edu.dlu.bysj.security.model.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Teacher;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.security.mapper.AuthorityMapper;
import edu.dlu.bysj.security.model.token.JwtToken;
import edu.dlu.bysj.security.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author XiangXinGang
 * @date 2021/10/11 9:08
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private StudentService iStudentService;

    @Autowired
    private TeacherService iTeacherService;

    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * 限定Realm 只处理我们自定义的JwtToken
     * @param token jwt 串
     *
     */
   @Override
   public boolean supports(AuthenticationToken token) {
       return  token instanceof JwtToken;
   }


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

      // TODO 学生老师授权操作
        String userNumber = (String) principalCollection.getPrimaryPrincipal();
        Student student = iStudentService.studentPassword(userNumber);
        Optional<Student>  studentValue = Optional.ofNullable(student);

        Set<String> authorities = null;
        List<Integer> roleIds = new ArrayList<>();
        if (studentValue.isPresent()) {
            roleIds.add(studentValue.get().getRoleId());
        }
        //学生授权
        if (roleIds.size() == 1) {
              authorities = authorityService.studentAuthorization(roleIds.get(0),  LocalDateTime.now());
        } else {
            Teacher teacher = iTeacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_number",userNumber));
            Optional<Teacher> teacherOptional = Optional.ofNullable(teacher);
            if (teacherOptional.isPresent()) {
                List<Integer> teacherRoleIds = iTeacherService.getTeacherRoles(teacherOptional.get().getId());
                if(teacherRoleIds.size() == 6)
                    authorities = authorityMapper.allAuthority();
                else
                    authorities = authorityService.teacherAuthorization(teacherRoleIds, LocalDateTime.now());
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置权限
        info.setStringPermissions(authorities);
        return info;
        // TODO 开发期间每个接口都开放权限
//        System.out.println("开始授权======================================");
//        Set<String> authirties = authorityMapper.allAuthority();
//       SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//       info.setStringPermissions(authirties);
//       return info;

    }

    /**
     *  验证密码该方法不会使用;
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("开始认证======================================");
        JwtToken token = (JwtToken) authenticationToken;
        String tokenString = (String) token.getCredentials();
        String userNumber = JwtUtil.getUserNumber(tokenString);
        //学生
        Student student = studentService.studentPassword(userNumber);
        Optional<Student> value = Optional.ofNullable(student);

        if (value.isPresent()) {
            if (value.get().getCanUse() == 0) {
                throw new LockedAccountException();
            }
            if (!StringUtils.isEmpty(value.get().getPassword())) {
                return new SimpleAuthenticationInfo(value.get().getStudentNumber(), value.get().getPassword(), ByteSource.Util.bytes(value.get().getStudentNumber()), this.getName());
            }
        }

        //老师;
        Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_number", userNumber));
        Optional<Teacher> teacherValue = Optional.ofNullable(teacher);


        if (teacherValue.isPresent()) {
            if (teacherValue.get().getCanUse() == 0) {
                throw new LockedAccountException();
            }
            if (!StringUtils.isEmpty(teacherValue.get().getPassword())) {
                System.out.println(token instanceof JwtToken);
                return new SimpleAuthenticationInfo(teacherValue.get().getTeacherNumber(), teacherValue.get().getPassword(), ByteSource.Util.bytes(teacherValue.get().getTeacherNumber()), this.getName());
            }
        }
        return null;
    }
}
