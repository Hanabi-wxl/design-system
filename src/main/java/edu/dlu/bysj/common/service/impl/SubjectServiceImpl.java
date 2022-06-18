package edu.dlu.bysj.common.service.impl;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.dlu.bysj.base.config.SnowflakeConfig;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.defense.mapper.EachMarkMapper;
import edu.dlu.bysj.system.mapper.MajorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.dto.AdminApprovalConvey;
import edu.dlu.bysj.base.model.dto.ApproveConditionConvey;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.model.query.*;
import edu.dlu.bysj.base.model.vo.*;
import edu.dlu.bysj.common.mapper.StudentMapper;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.paper.mapper.SourceMapper;
import edu.dlu.bysj.paper.mapper.SubjectTypeMapper;
import edu.dlu.bysj.paper.service.SubjectMajorService;
import edu.dlu.bysj.paper.service.TopicService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author XiangXinGang
 * @date 2021/10/24 16:43
 */
@Service
@Slf4j
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    private final SubjectMapper subjectMapper;

    private final EachMarkMapper eachMarkMapper;

    private final BaseMapper<Subject> baseMapper;

    private final TeacherMapper teacherMapper;

    private final StudentMapper studentMapper;

    private final SourceMapper sourceMapper;

    private final SubjectTypeMapper subjectTypeMapper;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final TopicService topicService;

    private final MajorMapper majorMapper;

    private final SubjectMajorService subjectMajorService;

    private final SnowflakeConfig snowflakeConfig;

    /**
     * 参数是否为第一次指导，是否雷同
     */
    private static final String YES = "是";

    private static final String NO = "否";

    /**
     * teacherMapper - firstAndSecondTeacherInfo () 方法返回值参数
     */
    private static final String TEACHER_NAME = "teacherName";
    private static final String MAJOR_NAME = "majorName";
    private static final String TITLE_NAME = "titleName";
    private static final String OFFICE_NAME = "officeName";
    private static final String PHONE_NAME = "phone";

    /**
     * studentMapper studentInfoById() 方法返回参数
     */
    private static final String STUDENT_NAME = "studentName";
    private static final String STUDENT_MAJOR_NAME = "studentMajorName";
    private static final String CLASS_NAME = "className";
    private static final String STUDENT_NUMBER = "studentNumber";

    /*循环依赖的时候使用懒加载*/

    @Autowired
    @Lazy
    public SubjectServiceImpl(BaseMapper<Subject> baseMapper, SubjectMapper subjectMapper, TeacherMapper teacherMapper,
        StudentMapper studentMapper, SourceMapper sourceMapper, SubjectTypeMapper subjectTypeMapper,
        StudentService studentService, TopicService topicService, SubjectMajorService subjectMajorService,SnowflakeConfig snowflakeConfig,
                              TeacherService teacherService, MajorMapper majorMapper,EachMarkMapper eachMarkMapper) {
        this.eachMarkMapper = eachMarkMapper;
        this.baseMapper = baseMapper;
        this.majorMapper = majorMapper;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.sourceMapper = sourceMapper;
        this.subjectTypeMapper = subjectTypeMapper;
        this.studentService = studentService;
        this.topicService = topicService;
        this.subjectMajorService = subjectMajorService;
        this.snowflakeConfig = snowflakeConfig;
        this.teacherService = teacherService;
    }

    @Override
    public SubjectSimplifyVo studentSimplifyInfo(Integer studentId) {
        return subjectMapper.subjectSimplifyInfo(studentId);
    }

    @Override
    public TotalPackageVo<ApprovalTaskBookVo> subjectApprovalInfo(TopicApprovalListQuery query) {

        // 调整query
        if (query.getPageNumber() > 0) {
            query.setPageNumber((query.getPageNumber() - 1) * query.getPageSize());
        }

        Integer total = subjectMapper.totalSubjectListByMajorId(query);
        List<ApprovalTaskBookVo> approvalTaskBookVos = subjectMapper.subjectListByMajorId(query);
        TotalPackageVo<ApprovalTaskBookVo> totalPackageVo = new TotalPackageVo<>();
        totalPackageVo.setTotal(total);
        totalPackageVo.setArrays(approvalTaskBookVos);
        return totalPackageVo;
    }

    @Override
    public SubjectDetailInfoVo obtainsSubjectRelationInfo(String subjectId) {
        Subject subject = baseMapper.selectOne(new QueryWrapper<Subject>().eq("subject_id", subjectId));
        if (ObjectUtil.isNull(subject))
            subject = baseMapper.selectOne(new QueryWrapper<Subject>().eq("id", subjectId));
        SubjectDetailInfoVo result = new SubjectDetailInfoVo();
        if (ObjectUtil.isNotNull(subject)) {
            result.setSubjectName(subject.getSubjectName());
            result.setIsFirst(subject.getIsFirstTeach() == 1 ? "1" : "0");
            result.setIsSimilar(subject.getIsSimilar() == 1 ? "1" : "0");
            result.setFirstTeacherId(subject.getFirstTeacherId());
            result.setSecondTeacherId(subject.getSecondTeacherId());
            result.setSourceId(subject.getSourceId());
            result.setPaperTypeId(subject.getSubjectTypeId());
            result.setNeedStudentNumber(subject.getStudentNeeded());
            result.setAbstractContent(subject.getTitleAbstract());
            result.setNecessity(subject.getNecessity());
            result.setFeasibility(subject.getFeasibility());

            /*获取第一第二指导教师名称，职称，专业*/
            Map<Integer, Map<String, Object>> teacherMap =
                teacherMapper.firstAndSecondTeacherInfo(subject.getFirstTeacherId(), subject.getSecondTeacherId());
            if (teacherMap != null && !teacherMap.isEmpty()) {
                if (teacherMap.containsKey(subject.getFirstTeacherId())) {
                    Object teacherName = teacherMap.get(subject.getFirstTeacherId()).get(TEACHER_NAME);
                    Object teacherMajor = teacherMap.get(subject.getFirstTeacherId()).get(MAJOR_NAME);
                    Object teacherTitle = teacherMap.get(subject.getFirstTeacherId()).get(TITLE_NAME);
                    result.setMajor((String)teacherMajor);
                    result.setTeacherName((String)teacherName);
                    result.setTitle((String)teacherTitle);
                }
                if (teacherMap.containsKey(subject.getSecondTeacherId())) {
                    Object teacherName = teacherMap.get(subject.getSecondTeacherId()).get(TEACHER_NAME);
                    result.setSecondTeacherName((String)teacherName);
                }
            }

            /*获取学生名称,学号，班级*/
            Map<Integer, Map<String, Object>> studentMap = studentMapper.studentInfoById(subject.getStudentId());
            if (studentMap != null && !studentMap.isEmpty()) {
                if (studentMap.containsKey(subject.getStudentId())) {
                    Object majorName = studentMap.get(subject.getStudentId()).get(STUDENT_MAJOR_NAME);
                    String studentName = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NAME);
                    String studentNumber = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NUMBER);
                    String className = (String)studentMap.get(subject.getStudentId()).get(CLASS_NAME);
                    result.setStudentMajor((String)majorName);
                    result.setStudentName(studentName);
                    result.setStudentNumber(studentNumber);
                    result.setClassName(className);
                }
            }

            /*获取论文类型，题目来源*/
            Source source = sourceMapper.selectById(subject.getSourceId());
            SubjectType subjectType = subjectTypeMapper.selectById(subject.getSubjectTypeId());
            if (ObjectUtil.isNotNull(source)) {
                result.setSubjectResource(source.getName());
            }
            if (ObjectUtil.isNotNull(subjectType)) {
                result.setPaperType(subjectType.getName());
            }

            //将要分配给的专业
            List<SubjectMajor> subjectMajorList = subjectMajorService.list(new QueryWrapper<SubjectMajor>().eq("subject_id", subjectId));
            List<Integer> majorIds = new ArrayList<>();
            for (SubjectMajor subjectMajor : subjectMajorList) {
                majorIds.add(subjectMajor.getMajorId());
            }
            result.setMajorIds(majorIds);
        }

        return result;
    }

    @Override
    public TotalPackageVo<SubjectDetailVo> teacherSubjectList(SubjectListQuery query, Integer teacherId) {
        TeacherShortenVo shortenInfo = null;
        try {
            shortenInfo = teacherService.teacherShortenInfo(teacherId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        /*分页*/
        Integer grade = GradeUtils.getGrade(query.getYear());
        List<SubjectDetailVo> subjectDetailVos = subjectMapper.teacherSubjectListByTeacherIdAndGrade(teacherId,
                grade, (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize());
        /*总数*/
        Integer total = subjectMapper.totalSubjectListByTeacher(teacherId, grade);
        /*查询学生信息*/
        List<Integer> studentIds = new ArrayList<>();
        for (SubjectDetailVo element : subjectDetailVos) {
            studentIds.add(element.getStudentId());
        }

        Map<Integer, Map<String, Object>> studentMap = studentMapper.teacherSubjectListStudentInfo(studentIds);

        Integer index;
        for (SubjectDetailVo element : subjectDetailVos) {
            Integer summaryScore = summaryScore(element.getDefenceScore(), element.getOtherScore(), element.getProcessScore(), element.getTeacherScore());
            element.setSummaryScore(summaryScore);
            index = element.getStudentId();
            if (studentMap.containsKey(index)) {
                element.setStudentName((String)studentMap.get(index).get("name"));
                element.setStudentNumber((String)studentMap.get(index).get("student_number"));
                element.setStudentPhone((String)studentMap.get(index).get("phone_number"));
                element.setClassName((String)studentMap.get(index).get("className"));
                element.setCollege((String)studentMap.get(index).get("collegeName"));
                element.setMajor((String)studentMap.get(index).get("majorName"));
            }
        }
        TotalPackageVo<SubjectDetailVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(subjectDetailVos);

        return result;
    }

    private Integer summaryScore(Integer s1, Integer s2, Integer s3, Integer s4){
        int sum = 0;
        if (ObjectUtil.isNotNull(s1)){
            sum += s1;
        } else if (ObjectUtil.isNotNull(s2)){
            sum += s2;
        } else if (ObjectUtil.isNotNull(s3)){
            sum += s3;
        } else if (ObjectUtil.isNotNull(s4)){
            sum += s4;
        }
        return sum;
    }

    // TODO: 2022/5/7  专业管理员获取审核列表
    @Override
    public TotalPackageVo<ApproveDetailVo> administratorApproveSubjectPagination(SubjectApproveListQuery query) {
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();

        /*由majorId,teacherId 查询该专业下的题目基干信息列表*/
        List<AdminApprovalConvey> adminApprovalConveys = subjectMapper.adminApprovalListPagination(query.getMajorId(),
            query.getTeacherId(), start, query.getPageSize(), query.getYear());

        /*总数*/
        Integer total = subjectMapper.totalAdminMajorApprovalList(query.getMajorId(), query.getTeacherId(), query.getYear());

        Set<Integer> teacherIds = new HashSet<>();
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            teacherIds.add(convey.getFirstTeacherId());
            teacherIds.add(convey.getSecondTeacherId());
        }
        /*查询第一第二指导教师信息*/
        Map<Integer, Map<String, Object>> teacherMap = teacherMapper.TeacherInfoByTeacherId(teacherIds);

        List<Integer> studentIds = new ArrayList<>();
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            studentIds.add(convey.getStudentId());
        }
        /*查询学生信息*/
        Map<Integer, Map<String, Object>> studentMap = studentMapper.adminApproveStudentInfoById(studentIds);

        /*参数拼接*/
        TotalPackageVo<ApproveDetailVo> result =
            this.packageParameterOfAdminApproveList(adminApprovalConveys, teacherMap, studentMap, total);

        return result;
    }

    @Override
    public TotalPackageVo<ApproveDetailVo> adminApprovePaginationByStudentCondition(ApproveConditionQuery query) {
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();

        /*在学生条件下的基干信息(学生-题目-教师部分信息)*/
        List<ApproveConditionConvey> approveConditionConveys = studentMapper.adminApproveByCondition(query.getMajorId(),
            query.getYear(), start, query.getPageSize(), query.getUserName(), query.getUserNumber());
        /*总数*/
        Integer total = studentMapper.totalAdminApproveByCondition(query.getMajorId(), query.getYear(),
            query.getUserName(), query.getUserNumber());

        Set<Integer> teacherIds = new HashSet<>();
        for (ApproveConditionConvey convey : approveConditionConveys) {
            teacherIds.add(convey.getFirstTeacherId());
            teacherIds.add(convey.getSecondTeacherId());
        }

        /*查询第一第二指导教师信息*/
        Map<Integer, Map<String, Object>> teacherMap = teacherMapper.TeacherInfoByTeacherId(teacherIds);
        return this.packageResultOfAdminListByStudentCondition(approveConditionConveys, teacherMap, total);
    }

    @Override
    public TotalPackageVo<ApproveDetailVo> adminApprovePaginationByTeacherCondition(ApproveConditionQuery query) {
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();

        /*基于teacherName, teacherNumber的分页,(第一第二都会被模糊查询) */
        List<AdminApprovalConvey> adminApprovalConveys =
            teacherMapper.adminApproveListByTeacherCondition(query.getMajorId(), query.getYear(), start,
                query.getPageSize(), query.getUserName(), query.getUserNumber());
        /*总数*/
        Integer total = teacherMapper.totalAdminApproveListTeacherCondition(query.getMajorId(), query.getYear(),
            query.getUserName(), query.getUserNumber());

        Set<Integer> teacherIds = new HashSet<>(16);
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            teacherIds.add(convey.getFirstTeacherId());
            teacherIds.add(convey.getSecondTeacherId());
        }

        /*查询第一第二指导教师信息*/
        Map<Integer, Map<String, Object>> teacherMap = teacherMapper.TeacherInfoByTeacherId(teacherIds);

        List<Integer> studentIds = new ArrayList<>();
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            studentIds.add(convey.getStudentId());
        }
        /*查询学生信息*/
        Map<Integer, Map<String, Object>> studentMap = studentMapper.adminApproveStudentInfoById(studentIds);

        /*参数拼接*/
        return this.packageParameterOfAdminApproveList(adminApprovalConveys, teacherMap, studentMap, total);

    }

    @Override
    public TotalPackageVo<TopicsVo> studentSelectSubjectInfo(TopicsListQuery query, Integer majorId, Integer grade) {
        TotalPackageVo<TopicsVo> result = new TotalPackageVo<>();
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();

        /*分页获取该年度该题目的指定条件下的记录*/
        List<TopicsVo> topicsVos = subjectMapper.studentSelectSubject(majorId, grade, start, query.getPageSize(),
            query.getTeacherName(), query.getSubjectName());
        Integer total =
            subjectMapper.totalStudentSelectSubject(majorId, grade, query.getTeacherName(), query.getSubjectName());
        result.setTotal(total);

        for (int i = 0; i < topicsVos.size(); i++) {
            /*获取该题已被多少人选中*/
            int count =
                topicService.count(new QueryWrapper<Topics>().eq("first_subject_id", topicsVos.get(i).getSubjectId())
                    .or().eq("second_subject_id", topicsVos.get(i).getSubjectId()));
            topicsVos.get(i).setSelectNumber(count);
        }

        result.setArrays(topicsVos);

        return result;
    }

    @Override
    public TotalPackageVo<DetermineStudentVo> studentEnsureSubjectByTeacherId(Integer teacherId, Integer grade,
        Integer pageNumber, Integer pageSize) {
        Integer start = (pageNumber - 1) * pageSize;

        List<DetermineStudentVo> determineStudentVos =
            subjectMapper.determinStudentSubject(teacherId, grade, start, pageSize);
        Integer total = subjectMapper.totalDeterminestudentSubject(teacherId, grade);

        TotalPackageVo<DetermineStudentVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(determineStudentVos);

        return result;
    }

    // TODO: 2022/5/6 报题
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addedApprove(SubjectApprovalVo subjectApprovalVo, Integer majorId) {
        subjectApprovalVo.setSubjectId(String.valueOf(snowflakeConfig.snowflakeId()));
        Integer studentNumber = subjectApprovalVo.getStudentNumber();
        Integer total = subjectMapper.totalSubjectListByStudent(studentService.numberToId(studentNumber),LocalDate.now().getYear());
        if (total < 1) {
            subjectApprovalVo.setStudentId(studentService.numberToId(studentNumber));
            Subject subject = new Subject();
            subject.setMajorId(majorId);
            this.packageSubject(subjectApprovalVo, subject);
            subject.setProgressId(ProcessEnum.AND_OR_MODIFY_TOPIC_DECLARATION.getProcessCode());
            List<SubjectMajor> subjectMajorList =
                    this.packageSubjectMajor(subjectApprovalVo.getMajorIds(), subjectApprovalVo.getSubjectId());
            /*设置题目提交日期*/
            subject.setSubmitDate(LocalDate.now());
            subject.setGrade(LocalDate.now().getYear());

            boolean save = this.save(subject);
            boolean saveBatch = subjectMajorService.saveBatch(subjectMajorList);

            return save && saveBatch;
        } else {
            return false;
        }
    }

    // TODO: 2022/5/6 修改题目
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean modifyApprove(SubjectApprovalVo subjectApprovalVo) {
        Subject subjectValue = this.getBySubjectId(subjectApprovalVo.getSubjectId());
        /*修改值*/
        subjectValue = this.packageSubject(subjectApprovalVo, subjectValue);
        /*打包新的subjectMajor*/
        List<SubjectMajor> subjectMajorList =
            this.packageSubjectMajor(subjectApprovalVo.getMajorIds(), subjectApprovalVo.getSubjectId());
        /*删除原有的，并新增新的*/
        boolean deleteFlag = subjectMajorService
            .remove(new QueryWrapper<SubjectMajor>().eq("subject_id", subjectApprovalVo.getSubjectId()));
        boolean saveBatch = subjectMajorService.saveBatch(subjectMajorList);
        boolean updateFlag = this.updateById(subjectValue);
        return deleteFlag && saveBatch && updateFlag;
    }

    @Override
    public boolean removeSubjectById(String subjectId) {
        return subjectMapper.removeSubjectById(subjectId);
    }

    @Override
    public List<Subject> listBySubjectIds(String id1, String id2) {
        return subjectMapper.listBySubjectIds(id1, id2);
    }

    @Override
    public TotalPackageVo<SubjectDetailVo> majorAdminSubjectList(SubjectListQuery query, Integer majorId) {

        /*分页*/
        List<SubjectDetailVo> subjectDetailVos =
                subjectMapper.adminSubjectListByMajorIdAndGrade(
                        majorId,
                        query.getYear(),
                        (query.getPageNumber() - 1) * query.getPageSize(),
                        query.getPageSize());

        /*总数*/
        Integer total = subjectMapper.totalSubjectListByMajor(majorId, query.getYear());

        /*查询学生信息*/
//        List<Integer> studentIds = new ArrayList<>();
//        for (SubjectDetailVo element : subjectDetailVos) {
//            studentIds.add(element.getStudentId());
//        }
//
//        Map<Integer, Map<String, Object>> studentMap = studentMapper.teacherSubjectListStudentInfo(studentIds);
//
//        Integer index;
//        for (SubjectDetailVo element : subjectDetailVos) {
//            index = element.getStudentId();
//            if (studentMap.containsKey(index)) {
//                element.setStudentName((String)studentMap.get(index).get("name"));
//                element.setStudentNumber((String)studentMap.get(index).get("student_number"));
//                element.setStudentPhone((String)studentMap.get(index).get("phone_number"));
//                element.setClassName((String)studentMap.get(index).get("className"));
//                element.setCollege((String)studentMap.get(index).get("collegeName"));
//                element.setMajor((String)studentMap.get(index).get("majorName"));
//            }
//        }
        TotalPackageVo<SubjectDetailVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        result.setArrays(subjectDetailVos);
        return result;
    }

    @Override
    public Subject getBySubjectId(String subjectId) {
        return baseMapper.selectOne(new QueryWrapper<Subject>().eq("subject_id", subjectId));
    }

    @Override
    public List<Subject> getBySubjectIds(String[] subjectIds) {
        return baseMapper.selectList(new QueryWrapper<Subject>().in("subject_id", Arrays.asList(subjectIds)));
    }

    // TODO: 2022/5/6 获取题目列表
    @Override
    public TotalPackageVo<SubjectDetailVo> studentSubjectList(SubjectListQuery query, Integer userId) {

        /*分页*/
        SubjectDetailVo subjectDetailVo = subjectMapper.studentSubjectListByStudentIdAndGrade(userId,
                query.getYear(), (query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize());
        /*总数*/
        Integer total = subjectMapper.totalSubjectListByStudent(userId, query.getYear());
        Student student = studentService.getById(userId);
        if (total != 0){
            subjectDetailVo.setStudentName(student.getName());
            subjectDetailVo.setStudentNumber(student.getStudentNumber());
            subjectDetailVo.setStudentPhone(student.getPhoneNumber());
        }
        TotalPackageVo<SubjectDetailVo> result = new TotalPackageVo<>();
        result.setTotal(total);
        boolean notNUll = Objects.nonNull(subjectDetailVo);
        if (notNUll)
            result.setArrays(Arrays.asList(subjectDetailVo));
        else
            result.setArrays(null);
        return result;
    }

    // TODO: 2022/5/7 获取院管理员审批列表
    @Override
    public TotalPackageVo<ApproveDetailVo> adminApproveSubjectPagination(SubjectApproveListQuery query) {
        Integer start = (query.getPageNumber() - 1) * query.getPageSize();

        /*由collegeId,teacherId 查询该专业下的题目基干信息列表*/
        List<AdminApprovalConvey> adminApprovalConveys = subjectMapper.approvalListPagination(query.getMajorId(),
                query.getTeacherId(), query.getSearchContent(), start, query.getPageSize(), query.getYear());

        /*总数*/
        Integer total = subjectMapper.totalAdminCollegeApprovalList(query.getMajorId(), query.getTeacherId(),
                query.getSearchContent(), query.getYear());

        Set<Integer> teacherIds = new HashSet<>();
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            teacherIds.add(convey.getFirstTeacherId());
            teacherIds.add(convey.getSecondTeacherId());
        }
        /*查询第一第二指导教师信息*/
        Map<Integer, Map<String, Object>> teacherMap = teacherMapper.TeacherInfoByTeacherId(teacherIds);

        List<Integer> studentIds = new ArrayList<>();
        for (AdminApprovalConvey convey : adminApprovalConveys) {
            studentIds.add(convey.getStudentId());
        }
        /*查询学生信息*/
        Map<Integer, Map<String, Object>> studentMap = studentMapper.adminApproveStudentInfoById(studentIds);

        /*参数拼接*/
        TotalPackageVo<ApproveDetailVo> result =
                this.packageParameterOfAdminApproveList(adminApprovalConveys, teacherMap, studentMap, total);

        return result;
    }

    @Override
    public List<Subject> listSubjectByIds(String firstSubjectId, String secondSubjectId) {
        return subjectMapper.listSubjectByIds(firstSubjectId,secondSubjectId);
    }

    @Override
    public Map<String,String> obtainsSubjectInfoById(Integer subjectId) {
        Subject subject = baseMapper.selectOne(new QueryWrapper<Subject>().eq("id", subjectId));
        Map<String,String> result = new HashMap<>();
        if (ObjectUtil.isNotNull(subject)) {
            result.put("subjectName",subject.getSubjectName());

            /*获取学生名称,学号，班级*/
            Map<Integer, Map<String, Object>> studentMap = studentMapper.studentInfoById(subject.getStudentId());
            if (studentMap != null && !studentMap.isEmpty()) {
                if (studentMap.containsKey(subject.getStudentId())) {
                    String studentName = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NAME);
                    String studentNumber = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NUMBER);
                    result.put("studentName",studentName);
                    result.put("studentNumber",studentNumber);
                }
            }
        }
        return result;
    }

    @Override
    public SubjectTableVo obtainsSubjectTableInfo(String subjectId) {
        Subject subject = baseMapper.selectOne(new QueryWrapper<Subject>().eq("id", subjectId));
        SubjectTableVo result = new SubjectTableVo();
        if (ObjectUtil.isNotNull(subject)) {
            result.setSubjectName(subject.getSubjectName());
            result.setIsFirst(subject.getIsFirstTeach() == 1 ? "是" : "否");
            result.setIsSimilar(subject.getIsSimilar() == 1 ? "是" : "否");
            result.setNeedStudentNumber(subject.getStudentNeeded());
            result.setAbstractContent(subject.getTitleAbstract());
            result.setNecessity(subject.getNecessity());
            result.setFeasibility(subject.getFeasibility());
            EachMark eachMark = eachMarkMapper.selectOne(new QueryWrapper<EachMark>().eq("subject_Id", subjectId));
            /*获取第一第二互评指导教师名称，职称，专业*/
            Map<Integer, Map<String, Object>> teacherMap =
                    teacherMapper.AllRelativeTeacherInfo(subject.getFirstTeacherId(), subject.getSecondTeacherId(), eachMark.getTeacherId());
            if (teacherMap != null && !teacherMap.isEmpty()) {
                if (teacherMap.containsKey(subject.getFirstTeacherId())) {
                    Object teacherName = teacherMap.get(subject.getFirstTeacherId()).get(TEACHER_NAME);
                    Object teacherMajor = teacherMap.get(subject.getFirstTeacherId()).get(MAJOR_NAME);
                    Object teacherTitle = teacherMap.get(subject.getFirstTeacherId()).get(TITLE_NAME);
                    Object officeName = teacherMap.get(subject.getFirstTeacherId()).get(OFFICE_NAME);
//                    Object otherTeacherName = teacherMap.get(eachMark.getTeacherId()).get(TEACHER_NAME);
//                    Object otherTeacherMajor = teacherMap.get(eachMark.getTeacherId()).get(MAJOR_NAME);
//                    Object otherTeacherPhone = teacherMap.get(eachMark.getTeacherId()).get(PHONE_NAME);
//                    result.setOtherTeacherName((String) otherTeacherName);
//                    result.setOtherTeacherName((String) otherTeacherMajor);
//                    result.setOtherTeacherName((String) otherTeacherPhone);
                    result.setFirstTeacherOffice((String) officeName);
                    result.setFirstTeacherMajor((String)teacherMajor);
                    result.setFirstTeacherName((String)teacherName);
                    result.setFirstTeacherTitle((String)teacherTitle);
                }
                if (teacherMap.containsKey(subject.getSecondTeacherId())) {
                    Object teacherName = teacherMap.get(subject.getSecondTeacherId()).get(TEACHER_NAME);
                    result.setSecondTeacherName((String)teacherName);
                }
            }

            /*获取学生名称,学号，班级*/
            Map<Integer, Map<String, Object>> studentMap = studentMapper.studentInfoById(subject.getStudentId());
            if (studentMap != null && !studentMap.isEmpty()) {
                if (studentMap.containsKey(subject.getStudentId())) {
                    Object majorName = studentMap.get(subject.getStudentId()).get(STUDENT_MAJOR_NAME);
                    String studentName = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NAME);
                    String studentNumber = (String)studentMap.get(subject.getStudentId()).get(STUDENT_NUMBER);
                    String className = (String)studentMap.get(subject.getStudentId()).get(CLASS_NAME);
                    result.setStudentMajor((String)majorName);
                    result.setStudentName(studentName);
                    result.setStudentNumber(studentNumber);
                    result.setClassName(className);
                }
            }

            /*获取论文类型，题目来源*/
            Source source = sourceMapper.selectById(subject.getSourceId());
            SubjectType subjectType = subjectTypeMapper.selectById(subject.getSubjectTypeId());
            if (ObjectUtil.isNotNull(source)) {
                result.setSource(source.getName());
            }
            if (ObjectUtil.isNotNull(subjectType)) {
                result.setPaperType(subjectType.getName());
            }

            //将要分配给的专业
            List<SubjectMajor> subjectMajorList = subjectMajorService.list(new QueryWrapper<SubjectMajor>().eq("subject_id", subject.getSubjectId()));
            List<Integer> majorIds = new ArrayList<>();
            for (SubjectMajor subjectMajor : subjectMajorList) {
                majorIds.add(subjectMajor.getMajorId());
            }
            List<String> majors = majorMapper.selectMajorNameByIds(majorIds);
            result.setMajors(majors);
        }

        return result;
    }

    @Override
    public TotalPackageVo<SubjectDetailVo> filterByYear(List<Integer> idList, Integer pageSize, Integer pageNumber, Integer year) {
        int start = (pageNumber - 1) * pageNumber;
        List<SubjectDetailVo> subjectDetailVos = subjectMapper.filterByYear(idList, start, pageSize, year);
        Integer total = subjectMapper.totalFilterByYear(idList, year);
        TotalPackageVo<SubjectDetailVo> vo = new TotalPackageVo<>();
        vo.setArrays(subjectDetailVos);
        vo.setTotal(total);
        return vo;
    }

    private Subject packageSubject(SubjectApprovalVo score, Subject target) {
        target.setSubjectId(score.getSubjectId());
        target.setSubjectName(score.getSubjectName());
        target.setFirstTeacherId(score.getTeacherId());
        target.setStudentId(score.getStudentId());
        target.setIsFirstTeach(score.getIsFirst());
        target.setIsSimilar(score.getIsSimilar());
        target.setSecondTeacherId(score.getSecondTeacherId());
        target.setStudentNeeded(score.getNeedStudentNumber());
        target.setSubjectTypeId(score.getPaperTypeId());
        target.setSourceId(score.getSourceId());
        target.setTitleAbstract(score.getAbstractContext());
        target.setNecessity(score.getNecessity());
        target.setFeasibility(score.getFeasibility());
        return target;
    }

    private List<SubjectMajor> packageSubjectMajor(List<Integer> majorIds, String subjectId) {
        List<SubjectMajor> subjectMajorList = new ArrayList<>();
        for (Integer element : majorIds) {
            SubjectMajor subjectMajor = new SubjectMajor();
            subjectMajor.setSubjectId(subjectId);
            subjectMajor.setMajorId(element);
            subjectMajorList.add(subjectMajor);
        }
        return subjectMajorList;
    }

    private TotalPackageVo<ApproveDetailVo> packageParameterOfAdminApproveList(List<AdminApprovalConvey> adminConveys,
        Map<Integer, Map<String, Object>> teacherMap, Map<Integer, Map<String, Object>> studentMap, Integer total) {
        TotalPackageVo<ApproveDetailVo> result = new TotalPackageVo<>();
        result.setTotal(total);

        List<ApproveDetailVo> approveList = new ArrayList<>();

        for (AdminApprovalConvey element : adminConveys) {

            ApproveDetailVo value = new ApproveDetailVo();
            /*题目基干信息*/
            value.setSubjectName(element.getSubjectName());
            value.setSubjectId(element.getSubjectId());
            value.setId(element.getId());
            value.setProgress(element.getProgress());

            /*教师信息, 封装成Map<Integer,Map<String,Object>>  第一层map 以teacherId 为key, 第二层map以字段的名称为key*/
            Integer firstTeacherId = element.getFirstTeacherId();
            Integer secondTeacherId = element.getSecondTeacherId();
            if (teacherMap.containsKey(firstTeacherId)) {
                value.setFirstTeacherName(((String)teacherMap.get(firstTeacherId).get("teacherName")));
                value.setFirstTeacherPhone(((String)teacherMap.get(firstTeacherId).get("phoneNumber")));
                value.setFirstTeacherTitle(((String)teacherMap.get(firstTeacherId).get("titleName")));
            }

            if (teacherMap.containsKey(secondTeacherId)) {
                value.setSecondTeacherName(((String)teacherMap.get(secondTeacherId).get("teacherName")));
                value.setSecondTeacherPhone(((String)teacherMap.get(secondTeacherId).get("phoneNumber")));
                value.setSecondTeacherTitle(((String)teacherMap.get(secondTeacherId).get("titleName")));
            }
            Integer studentId = element.getStudentId();

            /*学生, 封装成Map<Integer,Map<String,Object>>  第一层map 以teacherId 为key, 第二层map以字段的名称为key*/
            if (studentMap.containsKey(studentId)) {
                value.setStudentName(((String)studentMap.get(studentId).get("name")));
                value.setStudentNumber(((String)studentMap.get(studentId).get("student_number")));
                value.setStudentPhone(((String)studentMap.get(studentId).get("phone_number")));
            }
            approveList.add(value);
        }
        result.setArrays(approveList);
        return result;
    }

    private TotalPackageVo<ApproveDetailVo> packageResultOfAdminListByStudentCondition(
        List<ApproveConditionConvey> approveCondition, Map<Integer, Map<String, Object>> teacherMap, Integer total) {
        TotalPackageVo<ApproveDetailVo> approveDetailVoTotalPackageVo = new TotalPackageVo<>();
        List<ApproveDetailVo> result = new ArrayList<>();
        for (ApproveConditionConvey element : approveCondition) {
            ApproveDetailVo value = new ApproveDetailVo();
            value.setSubjectName(element.getSubjectName());
            value.setId(element.getId());
            value.setSubjectId(element.getSubjectId());
            value.setFillingNumber(element.getFillingNumber());
            value.setProgress(element.getProgress());
            value.setStudentName(element.getStudentName());
            value.setStudentNumber(element.getStudentNumber());
            value.setStudentPhone(element.getStudentPhone());

            /*教师信息, 封装成Map<Integer,Map<String,Object>>  第一层map 以teacherId 为key, 第二层map以字段的名称为key*/
            Integer firstTeacherId = element.getFirstTeacherId();
            Integer secondTeacherId = element.getSecondTeacherId();
            if (teacherMap.containsKey(firstTeacherId)) {
                value.setFirstTeacherName(((String)teacherMap.get(firstTeacherId).get("teacherName")));
                value.setFirstTeacherName(((String)teacherMap.get(firstTeacherId).get("phoneNumber")));
                value.setFirstTeacherTitle(((String)teacherMap.get(firstTeacherId).get("titleName")));
            }

            if (teacherMap.containsKey(secondTeacherId)) {
                value.setFirstTeacherName(((String)teacherMap.get(secondTeacherId).get("teacherName")));
                value.setFirstTeacherPhone(((String)teacherMap.get(secondTeacherId).get("phoneNumber")));
                value.setFirstTeacherTitle(((String)teacherMap.get(secondTeacherId).get("titleName")));
            }
            result.add(value);
        }
        approveDetailVoTotalPackageVo.setTotal(total);
        approveDetailVoTotalPackageVo.setArrays(result);
        return approveDetailVoTotalPackageVo;
    }

}
