package edu.dlu.bysj.security.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/13 20:35
 */
public interface AuthorityService {
    /**
     * 获取学生的认证字串
     * @param roleId 学生id
     * @param currentTime 当前时间
     * @return 权限字符串集合
     */
       Set<String> studentAuthorization(Integer roleId, LocalDateTime currentTime);

    /**
     * 获取老师认证字符串
     * @param roleIds 角色集合
     * @param currentTime 当前时间
     * @return 权限字符集合
     */
    Set<String> teacherAuthorization(List<Integer> roleIds,LocalDateTime currentTime);
}
