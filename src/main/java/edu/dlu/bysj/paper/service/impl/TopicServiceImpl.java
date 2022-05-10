package edu.dlu.bysj.paper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.model.entity.Topics;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.paper.mapper.TopicMapper;
import edu.dlu.bysj.paper.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/10/31 14:17
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topics> implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private SubjectService subjectService;


    @Override
    public List<SelectedVo> studentSelectSubject(Integer studentId) {
        Topics studentTopic = this.getOne(new QueryWrapper<Topics>().eq("student_id", studentId));
        List<SelectedVo> selectedVos = null;
        if (ObjectUtil.isNotNull(studentTopic)) {
            selectedVos = topicMapper.selectStudentChooseInfo(studentTopic.getFirstSubjectId(),
                    studentTopic.getSecondSubjectId());
        }

        if (selectedVos != null && !selectedVos.isEmpty()) {

            for (SelectedVo selectedVo : selectedVos) {
                if (String.valueOf(selectedVo.getSubjectId()).equals(studentTopic.getFirstSubjectId())) {
                    selectedVo.setChangeNumber(studentTopic.getFirstChange());
                } else if (String.valueOf(selectedVo.getSubjectId()).equals(studentTopic.getSecondSubjectId())) {
                    selectedVo.setChangeNumber(studentTopic.getSecondChange());
                }
            }
        }

        return selectedVos;
    }

    @Override
    public TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>> unselectStudentList(Integer teacherId, Integer grade, Integer pageNUmber, Integer pageSize) {
        TotalPackageVo<TotalVolunteerPackage<UnselectStudentVo>> result = new TotalPackageVo<>();
        Page<Subject> page = new Page<>(pageNUmber, pageSize);
        Page<Subject> IPage = subjectService.page(page, new QueryWrapper<Subject>().eq("first_teacher_id", teacherId).eq("grade", grade));

        long total = IPage.getTotal();
        List<Subject> records = IPage.getRecords();
        result.setTotal(new Long(total).intValue());

        List<TotalVolunteerPackage<UnselectStudentVo>> volunteerList = new ArrayList<>();
        for (Subject element : records) {
            /*该题目被学生作为第一志愿*/
            /*参数1， 为该题目是第一志愿的类型*/
            List<UnselectStudentVo> firstStudentVos = topicMapper.firstAndSecondVolunteer(element.getId(), 1);

            /*该题目被学生选为第二志愿 */
            /*参数 2 为该题作为第二志愿的类型*/
            List<UnselectStudentVo> secondStudentVos = topicMapper.firstAndSecondVolunteer(element.getId(), 2);

            TotalVolunteerPackage<UnselectStudentVo> value = new TotalVolunteerPackage<>();
            value.setFirstVolunteer(firstStudentVos);
            value.setSecondVolunteer(secondStudentVos);
            value.setSubjectId(element.getId());
            value.setSubjectName(element.getSubjectName());
            volunteerList.add(value);
        }
        result.setArrays(volunteerList);

        return result;
    }


    @Override
    public List<UnselectStudentVo> unChooseStudentList(String studentNumber, String studentName, Integer year, Integer majorId) {
        return topicMapper.unChooseTopicStudent(studentNumber, studentName, year, majorId);
    }


    @Override
    public List<UnSelectTopicVo> unChooseSubjectList(String teacherNumber, String teacherName, Integer year, Integer majorId) {
        return topicMapper.unChooseSubject(teacherNumber, teacherName, year, majorId);
    }
}
