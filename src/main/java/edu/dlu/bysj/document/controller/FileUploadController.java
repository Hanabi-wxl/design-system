package edu.dlu.bysj.document.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.common.service.*;
import edu.dlu.bysj.system.service.ClassService;
import edu.dlu.bysj.system.service.TeacherRoleService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.jfree.chart.title.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.document.service.FileUploadService;
import edu.dlu.bysj.document.service.SubjectFileService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.paper.service.OpenReportService;
import edu.dlu.bysj.system.service.CollegeService;
import edu.dlu.bysj.system.service.MajorService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * @author sinre
 * @create 05 14, 2022
 * @since 1.0.0
 */
@RestController
@RequestMapping("/paperManagement/fileUpload")
public class FileUploadController {
    private final StudentService studentService;
    private final CollegeService collegeService;
    private final OpenReportService openReportService;
    private final FileUploadService fileUploadService;
    private final SubjectService subjectService;
    private final FileInformationService fileInformationService;
    private final SubjectFileService subjectFileService;
    private final TeacherService teacherService;
    private final MajorService majorService;
    private final ClassService classService;
    private final TitleService titleService;
    private final DegreeService degreeService;
    private final TeacherRoleService teacherRoleService;
    private final OfficeService officeService;

    @Autowired
    public FileUploadController(StudentService studentService,MajorService majorService,TeacherRoleService teacherRoleService,
                                TeacherService teacherService,ClassService classService,
                                TitleService titleService, DegreeService degreeService, OfficeService officeService,
                                CollegeService collegeService, FileUploadService fileUploadService,
                                FileInformationService fileInformationService, OpenReportService openReportService,
                                SubjectService subjectService, SubjectFileService subjectFileService) {
        this.subjectFileService = subjectFileService;
        this.openReportService = openReportService;
        this.teacherRoleService = teacherRoleService;
        this.titleService = titleService;
        this.degreeService = degreeService;
        this.officeService = officeService;
        this.classService = classService;
        this.majorService = majorService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.fileUploadService = fileUploadService;
        this.collegeService = collegeService;
        this.fileInformationService = fileInformationService;
        this.teacherService = teacherService;
    }

    /**
     * 项目路径
     */
    @Value("${server.servlet.context-path}")
    public String contextPath;
    @Value("${design.system}")
    private String sys;

    @RequiresPermissions("file:uploadUser")
    @LogAnnotation(content = "批量上传学生信息")
    @PostMapping("/studentInfo")
    public CommonResult<Object> studentInfoSave(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        int year = LocalDate.now().getYear();
        String url = year + "/studentInfo";
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        if(map == null) {
            throw new GlobalException(400, "上传失败");
        }
        String dir = map.get("dir");
        if(sys.equals("win")){
            Resource resource = new ClassPathResource(dir);
            dir = resource.getURL().toString().split("file:/")[1];
        }
        XSSFWorkbook xssfSheet = new XSSFWorkbook(dir);
        XSSFSheet sheetAt = xssfSheet.getSheetAt(0);
        List<Student> students = new LinkedList<>();
        for (Row row : sheetAt) {
            if (row.getRowNum()==0)
                continue;
            Student student = new Student();
            String studentNumber = null;
            String majorName = null;
            String className = null;
            String collegeName = null;
            for (Cell cell : row) {
                if(cell.getColumnIndex()==0)
                    studentNumber = new DataFormatter().formatCellValue(cell);
                else if (cell.getColumnIndex()==1)
                    student.setName(new DataFormatter().formatCellValue(cell));
                else if (cell.getColumnIndex()==2)
                    collegeName = new DataFormatter().formatCellValue(cell);
                else if (cell.getColumnIndex()==3)
                    majorName = new DataFormatter().formatCellValue(cell);
                else if (cell.getColumnIndex()==4)
                    className = new DataFormatter().formatCellValue(cell);
                else if (cell.getColumnIndex()==5)
                    student.setSex(new DataFormatter().formatCellValue(cell));
                else if (cell.getColumnIndex()==6)
                    student.setPhoneNumber(new DataFormatter().formatCellValue(cell));
                else if (cell.getColumnIndex()==7)
                    student.setEmail(new DataFormatter().formatCellValue(cell));
            }
            student.setStudentNumber(studentNumber);

            SimpleHash simpleHash = new SimpleHash("MD5",studentNumber,studentNumber,1024);
            student.setPassword(simpleHash.toString());

            College college = collegeService.getOne(new QueryWrapper<College>().eq("name", collegeName));
            if(college==null)
                return CommonResult.failed("学院信息有误，请检查第" + (row.getRowNum()+1) + "行学院信息: " + collegeName);

            Major major = majorService.getOne(new QueryWrapper<Major>()
                    .eq("name", majorName)
                    .eq("college_id", college.getId()));

            if(major == null)
                return CommonResult.failed("专业信息有误，请检查第" + (row.getRowNum()+1) + "行专业信息: " + majorName);

            Class aClass = classService.getOne(new QueryWrapper<Class>().eq("name", className));
            if(aClass == null)
                return CommonResult.failed("班级信息有误，请检查第" + (row.getRowNum()+1) + "行班级信息: " + className);

            student.setClassId(aClass.getId());
            student.setMajorId(major.getId());
            students.add(student);
        }
        boolean flag = studentService.saveBatch(students);
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:uploadUser")
    @LogAnnotation(content = "批量上传教师信息")
    @PostMapping("/teacherInfo")
    public CommonResult<Object> teacherInfoSave(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        int year = LocalDate.now().getYear();
        String url = year + "/teacherInfo";
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        if(map == null) {
            throw new GlobalException(400, "上传失败");
        }
        String dir = map.get("dir");
        if(sys.equals("win")){
            Resource resource = new ClassPathResource(dir);
            dir = resource.getURL().toString().split("file:/")[1];
        }
        XSSFWorkbook xssfSheet = new XSSFWorkbook(dir);
        XSSFSheet sheetAt = xssfSheet.getSheetAt(0);
        List<Teacher> teachers = new LinkedList<>();
        List<Integer> numberList = new LinkedList<>();
        for (Row row : sheetAt) {
            if (row.getRowNum()==0)
                continue;
            Teacher teacher = new Teacher();
            String teacherNumber = null;
            String title = null;
            String degree = null;
            String majorName = null;
            String collegeName = null;
            String office = null;
            for (Cell cell : row) {
                if(cell.getColumnIndex()==0)
                    teacherNumber = new DataFormatter().formatCellValue(cell);
                else if(cell.getColumnIndex()==1)
                    teacher.setName(new DataFormatter().formatCellValue(cell));
                else if(cell.getColumnIndex()==2)
                    collegeName = new DataFormatter().formatCellValue(cell);
                else if(cell.getColumnIndex()==3)
                    majorName = new DataFormatter().formatCellValue(cell);
                else if(cell.getColumnIndex()==4)
                    teacher.setSex(new DataFormatter().formatCellValue(cell));
                else if(cell.getColumnIndex()==5)
                    teacher.setPhoneNumber(new DataFormatter().formatCellValue(cell));
                else if(cell.getColumnIndex()==6)
                    teacher.setEmail(new DataFormatter().formatCellValue(cell));
                else if(cell.getColumnIndex()==7)
                    title = new DataFormatter().formatCellValue(cell);
                else if(cell.getColumnIndex()==8)
                    degree = new DataFormatter().formatCellValue(cell);
                else if(cell.getColumnIndex()==9)
                    office = new DataFormatter().formatCellValue(cell);
            }
            TeacherTitle title1 = titleService.getOne(new QueryWrapper<TeacherTitle>().eq("name", title));
            teacher.setTitleId(title1.getId());
            College college = collegeService.getOne(new QueryWrapper<College>().eq("name", collegeName));
            if(college==null)
                return CommonResult.failed("学院信息有误，请检查第" + (row.getRowNum()+1) + "行学院信息: " + collegeName);

            Major major = majorService.getOne(new QueryWrapper<Major>()
                    .eq("name", majorName)
                    .eq("college_id", college.getId()));

            if(major == null)
                return CommonResult.failed("专业信息有误，请检查第" + (row.getRowNum()+1) + "行专业信息: " + majorName);
            else
                teacher.setMajorId(major.getId());

            Degree degree1 = degreeService.getOne(new QueryWrapper<Degree>().eq("name", degree));
            teacher.setDegreeId(degree1.getId());
            Office office1 = officeService.getOne(new QueryWrapper<Office>().eq("name", office));
            if(office1 == null) {
                Office office2 = new Office();
                office2.setName(office);
                boolean save = officeService.save(office2);
                if(save)
                    teacher.setOfficeId(officeService.getOne(new QueryWrapper<Office>().eq("name", office)).getId());
            } else {
                teacher.setOfficeId(office1.getId());
            }
            SimpleHash simpleHash = new SimpleHash("MD5",teacherNumber,teacherNumber,1024);
            teacher.setPassword(simpleHash.toString());
            teacher.setTeacherNumber(teacherNumber);
            numberList.add(Integer.parseInt(teacherNumber));

            teachers.add(teacher);
        }
        boolean flag = teacherService.saveBatch(teachers);
        if (flag) {
            List<TeacherRole> ids = teacherService.listByNumbers(numberList);
            teacherRoleService.saveBatch(ids);
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传开题报告")
    @PostMapping("/openReport")
    public CommonResult<Object> uploadReport(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber = studentService.idToNumber(userId);
        Student student = studentService.getById(userId);
        // 例：2022/open-report/college1/major1/20423034
        String url = year + "/open-report/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        if(map == null) {
            throw new GlobalException(400, "上传失败");
        }
        FileInfomation infomation = new FileInfomation();
        // 1 : 开题报告
        infomation.setType("1");
        infomation.setTitle(userNumber + "开题报告");
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(JwtUtil.getRoleIds(jwt).contains(1) ? 1 : 0);
        boolean save = fileInformationService.save(infomation);
        boolean flag = false;
        if (save) {
            OpenReport openReport = new OpenReport();
            openReport.setSubjectId(student.getSubjectId());
            FileInfomation fileInfomation = fileInformationService.getOne(new QueryWrapper<FileInfomation>()
                    .eq("type", 1).eq("user_id", userId)
                    .eq("is_student", 1).eq("dir", map.get("dir")));
            Subject subject = subjectService.getById(student.getSubjectId());
            openReport.setFileId(Integer.parseInt(fileInfomation.getId().toString()));
            openReport.setMajorLeadingId(subject.getMajorLeadingId());
            openReport.setCollegeLeadingId(subject.getCollegeLeadingId());
            boolean save1 = openReportService.saveOrUpdate(openReport, new QueryWrapper<OpenReport>()
                    .eq("subject_id", student.getSubjectId()));
            if (save1) {
                Integer processCode = ProcessEnum.SUBMIT_OPEN_REPORT.getProcessCode();
                if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                    subject.setProgressId(processCode);
                    flag = subjectService.updateById(subject);
                } else {
                    throw new GlobalException(400, "过程错误");
                }
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传论文")
    @PostMapping("/paper")
    public CommonResult<Object> uploadPaper(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber = studentService.idToNumber(userId);
        Student student = studentService.getById(userId);
        // 例：2022/paper/college1/major1/20423034
        String url = year + "/paper/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;

        Map<String, String> map = fileUploadService.uploadFile(file, url);
        FileInfomation infomation = new FileInfomation();
        // 2 : 论文
        infomation.setType("2");
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        infomation.setTitle(userNumber + "论文" + fileSuffix);
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(JwtUtil.getRoleIds(jwt).contains(1) ? 1 : 0);
        boolean save = fileInformationService.save(infomation);
        boolean flag = false;
        if (save) {
            SubjectFile subjectFile = new SubjectFile();
            subjectFile.setSubjectId(student.getSubjectId());
            FileInfomation fileInfomation = fileInformationService.getOne(new QueryWrapper<FileInfomation>()
                    .eq("type", 2).eq("user_id", userId)
                    .eq("is_student", 1).eq("dir", map.get("dir")));
            if (ObjectUtil.isNotNull(fileInfomation)) {
                subjectFile.setFileId(Integer.parseInt(fileInfomation.getId().toString()));
                subjectFile.setFileType(1);
                boolean save1 = subjectFileService.saveOrUpdate(subjectFile, new QueryWrapper<SubjectFile>()
                        .eq("subject_id", student.getSubjectId())
                        .eq("file_type", 1));
                Subject subject = subjectService.getById(student.getSubjectId());
                if (save1) {
                    Integer processCode = ProcessEnum.SUBMIT_PAPER.getProcessCode();
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        subject.setProgressId(processCode);
                        flag = subjectService.updateById(subject);
                    } else {
                        CommonResult.failed("过程错误");
                    }
                }
            } else {
                throw new GlobalException(400, "上传论文失败");
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传答辩记录纸")
    @PostMapping("/recordPaper")
    public CommonResult<Object> uploadRecordPaper(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer isStudent = JwtUtil.getRoleIds(jwt).contains(1) ? 1: 0;
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber;
        if (isStudent==1)
            userNumber = studentService.idToNumber(userId);
        else
            userNumber = Integer.parseInt(teacherService.idToNumber(userId));

        // 例：2022/paper/college1/major1/20423034
        String url = year + "/recordPaper/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        FileInfomation infomation = new FileInfomation();
        // 6: 答辩记录纸
        infomation.setType("6");
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        infomation.setTitle(userNumber + "答辩记录纸" + fileSuffix);
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(isStudent);
        boolean save = fileInformationService.save(infomation);
        return save ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传毕业设计")
    @PostMapping("/design")
    public CommonResult<Object> uploadDesign(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber = studentService.idToNumber(userId);
        Student student = studentService.getById(userId);
        // 例：2022/design/college1/major1/20423034
        String url = year + "/design/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        FileInfomation infomation = new FileInfomation();
        // 3 : 毕设
        String originalFilename = file.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        infomation.setType("3");
        infomation.setTitle(userNumber + "毕设" + fileSuffix);
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(JwtUtil.getRoleIds(jwt).contains(1) ? 1 : 0);
        boolean save = fileInformationService.save(infomation);
        boolean flag = false;
        if (save) {
            SubjectFile subjectFile = new SubjectFile();
            subjectFile.setSubjectId(student.getSubjectId());
            FileInfomation fileInfomation = fileInformationService.getOne(new QueryWrapper<FileInfomation>()
                    .eq("type", 3).eq("user_id", userId)
                    .eq("is_student", 1).eq("dir", map.get("dir")));
            if (ObjectUtil.isNotNull(fileInfomation)) {
                subjectFile.setFileId(Integer.parseInt(fileInfomation.getId().toString()));
                subjectFile.setFileType(2);
                boolean save1 = subjectFileService.saveOrUpdate(subjectFile, new QueryWrapper<SubjectFile>()
                        .eq("subject_id", student.getSubjectId())
                        .eq("file_type", 2));
                Subject subject = subjectService.getById(student.getSubjectId());
                if (save1) {
                    Integer processCode = ProcessEnum.SUBMIT_PAPER.getProcessCode();
                    if (processCode.equals(subject.getProgressId()) || processCode.equals(subject.getProgressId() + 1)) {
                        subject.setProgressId(processCode);
                        flag = subjectService.updateById(subject);
                    } else {
                        return CommonResult.failed("过程错误");
                    }
                }
            } else {
                throw new GlobalException(400, "上传毕设失败");
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("notice:upload")
    @LogAnnotation(content = "上传通知文件")
    @PostMapping("/noticeFile")
    public CommonResult<Object> uploadNoticeFile(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber = Integer.valueOf(teacherService.idToNumber(userId));

        // 例：2022/open-report/college1/major1/20423034
        String url = year + "/notice-file/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);

        FileInfomation infomation = new FileInfomation();
        //4 : 通知文件
        infomation.setType("4");
        infomation.setTitle(userNumber + "通知文件");
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(JwtUtil.getRoleIds(jwt).contains(1) ? 1 : 0);
        fileInformationService.save(infomation);

        CommonResult<Object> noticeResult = new CommonResult<>();
        noticeResult.setCode(200);
        noticeResult.setMessage("提交成功");
        noticeResult.setData(infomation);

        return noticeResult;
    }

    @RequiresPermissions("message:upload")
    @LogAnnotation(content = "上传消息文件")
    @PostMapping("/messageFile")
    public CommonResult<Object> uploadMessageFile(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        int year = LocalDate.now().getYear();
        Integer userNumber = Integer.valueOf(teacherService.idToNumber(userId));

        // 例：2022/open-report/college1/major1/20423034
        String url = year + "/message-file/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);

        FileInfomation infomation = new FileInfomation();
        //5 : 消息文件
        infomation.setType("5");
        infomation.setTitle(userNumber + "消息文件");
        infomation.setDir(map.get("dir"));
        infomation.setUserId(userId);
        infomation.setIsStudent(JwtUtil.getRoleIds(jwt).contains(1) ? 1 : 0);
        fileInformationService.save(infomation);

        CommonResult<Object> noticeResult = new CommonResult<>();
        noticeResult.setCode(200);
        noticeResult.setMessage("提交成功");
        noticeResult.setData(infomation);

        return noticeResult;
    }
}