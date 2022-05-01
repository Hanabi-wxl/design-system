package edu.dlu.bysj.defense.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlu.bysj.base.model.entity.EachMark;
import edu.dlu.bysj.base.model.query.MutualEvaluationQuery;
import edu.dlu.bysj.base.model.vo.MutualEvaluationVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;

import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/6 9:38
 */
public interface EachMarkService extends IService<EachMark> {

    /**
     * 通过 MutualEvaluationQuery 中的信息查询专业互评分组结果
     *
     * @param query 查询对象
     * @return 分页 打包的 MutualEvaluationVo
     */
    TotalPackageVo<MutualEvaluationVo> eachMarkMajorInfo(MutualEvaluationQuery query);

    /**
     * 生成本专业的互评数据
     * 生成规则: 该教师在本专该年级业指导了多少论文就分配多少互评题目(排除本身)
     *
     * @param majorId   专业id
     * @param grade     年级
     * @param printOut  是否打印
     * @param time      提交打印稿时间
     * @param leadingId 专业管理员id
     * @return
     */
    boolean generateEachMarkTeacher(Integer majorId, Integer grade, Integer printOut, String time, Integer leadingId);


    /**
     * 查看该题目的互评教师信息
     *
     * @param subjectId
     * @return map集合以subjectId 作为第一层key
     */
    Map<Integer, Map<String, Object>> selectEachMarkTeacherBySubject(Integer subjectId);
}
