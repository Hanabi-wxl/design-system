package edu.dlu.bysj.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.Function;
import edu.dlu.bysj.base.model.vo.FunctionSimplifyVo;
import edu.dlu.bysj.base.model.vo.FunctionTimeVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/15 20:39
 */
public interface FunctionService extends IService<Function> {
    /**
     * 根据id 跟新function 的开始时间和结束时间
     *
     * @param functionId :功能id
     * @param startTime  :开始时间
     * @param endTime:   结束时间
     * @return 跟新是否成功
     */
    boolean updateFunctionTime(Integer functionId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 通过角色查询该角色的功能开放时间
     *
     * @param role        角色集合
     * @param currentTime 当前时间
     * @return 简单的个人功能集合;
     */
    List<FunctionSimplifyVo> personFunctionByTime(List<Integer> role, String currentTime);

    /**
     * 获取专业时间管理表
     *
     * @param majorId 专业id
     * @return 专业时间表集合
     */
    List<FunctionTimeVo> allFunction(Integer majorId);
}
