package edu.dlu.bysj.document.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.common.service.TeacherService;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author sinre
 * @create 05 14, 2022
 * @since 1.0.0
 */
@RestController
@RequestMapping("/paperManagement/fileUpload/")
public class FileUploadController {
    private final StudentService studentService;
    private final CollegeService collegeService;
    private final OpenReportService openReportService;
    private final FileUploadService fileUploadService;
    private final SubjectService subjectService;
    private final FileInformationService fileInformationService;
    private final SubjectFileService subjectFileService;
    private final TeacherService teacherService;

    @Autowired
    public FileUploadController(StudentService studentService,
                                TeacherService teacherService,
                                CollegeService collegeService, FileUploadService fileUploadService,
                                FileInformationService fileInformationService, OpenReportService openReportService,
                                SubjectService subjectService, SubjectFileService subjectFileService) {
        this.subjectFileService = subjectFileService;
        this.openReportService = openReportService;
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

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传开题报告")
    @PostMapping("openReport")
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
            throw new Exception("上传失败: ");
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
                    throw new Exception("过程错误");
                }
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传论文")
    @PostMapping("paper")
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
                        throw new Exception("过程错误");
                    }
                }
            } else {
                throw new Exception("上传论文失败");
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("file:upload")
    @LogAnnotation(content = "上传毕业设计")
    @PostMapping("design")
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
                        throw new Exception("过程错误");
                    }
                }
            } else {
                throw new Exception("上传毕设失败");
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @RequiresPermissions("notice:upload")
    @LogAnnotation(content = "上传通知文件")
    @PostMapping("noticeFile")
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
    @PostMapping("messageFile")
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