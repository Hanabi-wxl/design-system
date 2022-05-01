package edu.dlu.bysj.security.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/13 20:37
 */
@Repository
public interface AuthorityMapper {
    /**
     * 查询学生的权限字符串
     * @return 权限字符串集合
     */
    Set<String> studentPermissions(@Param("roleId") Integer roleId,
                                   @Param("currentTime") LocalDateTime currentTime);

    /**
     * 查询老师的权限字符串;
     * @return 权限字符串集合
     */
    Set<String> teacherPermissions(@Param("roleIds") List<Integer> roleIds,
                                   @Param("currentTime") LocalDateTime currentTime);



    //TODO 测试中使用全部权限
    Set<String> allAuthority();
}
