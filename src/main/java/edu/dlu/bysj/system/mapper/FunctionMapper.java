package edu.dlu.bysj.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.Function;
import edu.dlu.bysj.base.model.vo.FunctionSimplifyVo;
import edu.dlu.bysj.base.model.vo.FunctionTimeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/15 20:38
 */
@Repository
public interface FunctionMapper extends BaseMapper<Function> {
    /**
     * 根据function id 跟新 function的startTime 和 endTime
     *
     * @param functionId 功能id
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 操作成功记录数;
     */
    Integer updateStartTimeAndEndTime(
            @Param("functionId") Integer functionId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 通过role列表获取 个人菜单信息
     *
     * @param role     角色列表
     * @param current: 当前时间
     * @return 个人菜单信息集合
     */
    List<FunctionSimplifyVo> getPersonFunction(
            @Param("roles") List<Integer> role, @Param("currentTime") String current);

    /**
     * 通过majorId 获取该专业的所有菜单时间表
     *
     * @param majorId 专业id
     * @return 菜单时间表返回值;
     */
    List<FunctionTimeVo> getFunctionByMajorId(Integer majorId);
}
