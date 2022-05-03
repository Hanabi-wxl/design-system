package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.TeacherRole;
import edu.dlu.bysj.base.model.vo.AdminVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author XiangXinGang
 * @date 2021/10/17 11:39
 */
@Repository
public interface TeacherRoleMapper extends BaseMapper<TeacherRole> {
    /**
     * 根据teacherId 获取教师角色列表
     *
     * @param teacherId 教师id
     * @return 角色集合
     */
    Set<Integer> getAllRoleById(String teacherId);



    /**
     * 根据角色id 获取该角色对应的所有老师
     *
     * @param roleId 角色Id (专业，校级，院级)管理员
     * @return 对应类别的教师集合;
     */
    List<AdminVo> getMangerList(@Param("roleId") Integer roleId, @Param("majorId") Integer majorId);


    /*
     * @Description: 根据专业管理员id获取管理员信息
     * @Author: sinre
     * @Date: 2022/5/3 14:04
     * @param majorAminIds
     * @return java.util.List<edu.dlu.bysj.base.model.vo.AdminVo>
     **/
    List<AdminVo> getMajorAdminList(@Param("ids") List<Integer> ids);

    /*
     * @Description: 根据角色id获取所有教师id
     * @Author: sinre
     * @Date: 2022/5/3 14:28
     * @param i
     * @return java.util.List<java.lang.Integer>
     **/
    List<Integer> getAllIdByRole(int roleId);
}
