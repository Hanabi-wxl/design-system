package edu.dlu.bysj.defense.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.dto.EachMarkConvey;
import edu.dlu.bysj.base.model.entity.EachMark;
import edu.dlu.bysj.base.model.entity.SubjectMajor;
import edu.dlu.bysj.base.model.query.MutualEvaluationQuery;
import edu.dlu.bysj.base.model.vo.MutualEvaluationVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.defense.mapper.EachMarkMapper;
import edu.dlu.bysj.defense.service.EachMarkService;
import edu.dlu.bysj.paper.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/6 9:38
 */
@Service

@Slf4j
public class EachMarkServiceImpl extends ServiceImpl<EachMarkMapper, EachMark> implements EachMarkService {

    private final EachMarkMapper eachMarkMapper;

    private final MessageService messageService;

    private final SubjectMapper subjectMapper;


    @Autowired
    public EachMarkServiceImpl(EachMarkMapper eachMarkMapper,SubjectMapper subjectMapper,
                               MessageService messageService) {
        this.eachMarkMapper = eachMarkMapper;
        this.messageService = messageService;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public TotalPackageVo<MutualEvaluationVo> eachMarkMajorInfo(MutualEvaluationQuery query) {
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();
        query.setYear(GradeUtils.getGrade(query.getYear()));
        List<MutualEvaluationVo> mutualEvaluationVos = eachMarkMapper.selectEachMarkInfoByQuery(query, start, query.getPageSize());
        Integer total = eachMarkMapper.totalEachMarkInfoByQuery(query);
        TotalPackageVo<MutualEvaluationVo> result = new TotalPackageVo<>();
        /*查询互评老师*/

        List<Integer> subjectIdList = new ArrayList<>();
        for (MutualEvaluationVo element : mutualEvaluationVos) {
            subjectIdList.add(element.getSubjectId());
        }
        Map<Integer, Map<String, Object>> teacherMap = eachMarkMapper.selectEachMarkTeacherName(subjectIdList);
        /*拼接互评老师*/
        if (teacherMap != null && !teacherMap.isEmpty()) {
            for (MutualEvaluationVo element : mutualEvaluationVos) {
                String teacherName = (String) teacherMap.get(element.getSubjectId()).get("name");
                String majorName = (String) teacherMap.get(element.getSubjectId()).get("majorName");
                String phone = (String) teacherMap.get(element.getSubjectId()).get("phone");
                Integer teacherId = (Integer) teacherMap.get(element.getSubjectId()).get("teacherId");
                element.setOtherTeacherName(teacherName);
                element.setOtherTeacherMajor(majorName);
                element.setOtherTeacherPhone(phone);
                element.setOtherTeacherId(teacherId);
            }
        }

        result.setTotal(total);
        result.setArrays(mutualEvaluationVos);
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean generateEachMarkTeacher(Integer majorId, Integer grade, Integer printOut, String time, Integer leadingId) {
        /*该专业的所有教师对应的id和题目*/
        grade = GradeUtils.getGrade(grade);
        List<EachMarkConvey> guide = eachMarkMapper.selectTeacherGuide(grade, majorId);
        if (ObjectUtil.isNull(guide) && guide.size() == 0) {
            return false;
        }
        for (EachMarkConvey element : guide) {
            EachMark eachMark = new EachMark();
            while (true) {
                /*生成[0,guide.size()) ,左毕右开*/
                int i = RandomUtil.randomInt(0, guide.size());
                /*该题目没有被选中并且,该题目的指导教师不是外层循环题目的指导教师(同一个教师是有可能评价到自己的题目的，比如该专业3个题目有一名老师占了2个的情况)*/
                if (guide.get(i).getStatus().equals(1)) {
                    eachMark.setTeacherId(element.getTeacherId());
                    eachMark.setSubjectId(guide.get(i).getSubjectId());
                    eachMark.setNeedPrint(printOut);
                    /*插入到eachMark表中*/
                    boolean save = this.save(eachMark);
                    /*如果printout == 1  && time != null or time != ''  以专业管理员的身份发送一条消息告知互评教师递交打印稿时间*/
                    if (printOut.equals(1) && !StringUtils.isEmpty(time)) {
                        messageService.sendMessage("递交互评打印稿通知", "请于" + time + "之前将互评打印稿递交给互评教师", leadingId, element.getTeacherId(), 0);
                    }

                    /*保存成功返回跳出循环;*/
                    if (save) {
                        /*修改随机生成题目的状态*/
                        guide.get(i).setStatus(0);
                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public Map<Integer, Map<String, Object>> selectEachMarkTeacherBySubject(Integer subjectId) {
        return eachMarkMapper.selectEachMarkTeacherBySubject(subjectId);
    }

    @Override
    public void removeOldDate(Integer majorId) {
        List<Integer> subjectIds = subjectMapper.getIdsByMajor(majorId);
        if (subjectIds.size() != 0)
            baseMapper.removeBySubjectIds(subjectIds);
    }
}
