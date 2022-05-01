package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.TeacherRole;
import edu.dlu.bysj.base.model.vo.AdminVo;

import java.util.List;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/17 11:40
 */
public interface TeacherRoleService extends IService<TeacherRole> {


    /**
     * 根据teacherId 获取该教师角色列表
     *
     * @param teacherId 教师id
     * @return 角色列表
     */
    Set<Integer> teacherRoleName(String teacherId);


    /**
     * 根据角色获取对应的管理员集合;
     *
     * @param roleId  roleid (专业，学院，学校)管理员
     * @param majorId majorId(专业Id )
     * @return 对应的role信息集合;
     */
    List<AdminVo> teacherMangerRoles(Integer roleId, Integer majorId);
}
