package edu.dlu.bysj.document.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.enums.ProcessEnum;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.document.service.FileUploadService;
import edu.dlu.bysj.document.service.SubjectFileService;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.paper.service.OpenReportService;
import edu.dlu.bysj.system.service.CollegeService;
import edu.dlu.bysj.system.service.MajorService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

/**
 * @author sinre
 * @create 05 14, 2022
 * @since 1.0.0
 */
@RestController
public class FileUploadController {
    private final StudentService studentService;
    private final CollegeService collegeService;
    private final OpenReportService openReportService;
    private final FileUploadService fileUploadService;
    private final SubjectService subjectService;
    private final FileInformationService fileInformationService;
    private final SubjectFileService subjectFileService;

    @Autowired
    public FileUploadController(StudentService studentService,
                                CollegeService collegeService,FileUploadService fileUploadService,
                                FileInformationService fileInformationService,OpenReportService openReportService,
                                SubjectService subjectService, SubjectFileService subjectFileService){
        this.subjectFileService = subjectFileService;
        this.openReportService = openReportService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.fileUploadService = fileUploadService;
        this.collegeService = collegeService;
        this.fileInformationService = fileInformationService;
    }

    /**
     * 项目路径
     */
    @Value("${server.servlet.context-path}")
    public String contextPath;

    @PostMapping("/paperManagement/fileUpload/openReport")
    public CommonResult<Object> uploadReport(@RequestBody MultipartFile file, HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Integer userNumber = studentService.idToNumber(userId);
        Student student = studentService.getById(userId);
        // 例：2022/open-report/college1/major1/20423034
        String url =  year + "/open-report/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        FileInfomation infomation = new FileInfomation();
        // 1 : 开题报告
        infomation.setType("1");
        infomation.setTitle(userNumber+"开题报告");
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
            boolean save1 = openReportService.save(openReport);
            if (save1){
                int progress = subject.getProgressId().equals(ProcessEnum.SUBMIT_OPEN_REPORT.getProcessCode()) ?
                        subject.getProgressId() : (subject.getProgressId() + 1);
                subject.setProgressId(progress);
                flag = subjectService.updateById(subject);
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    @PostMapping("/paperManagement/fileUpload/paper")
    public CommonResult<Object> uploadPaper(@RequestBody MultipartFile file, HttpServletRequest request){
        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        Integer userId = JwtUtil.getUserId(jwt);
        Integer collegeId = collegeService.getCollegeIdByMajorId(majorId);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Integer userNumber = studentService.idToNumber(userId);
        Student student = studentService.getById(userId);
        // 例：2022/paper/college1/major1/20423034
        String url =  year + "/paper/" + "college" + collegeId + "/" + "major" + majorId + "/" + userNumber;
        Map<String, String> map = fileUploadService.uploadFile(file, url);
        FileInfomation infomation = new FileInfomation();
        // 2 : 论文
        infomation.setType("2");
        infomation.setTitle(userNumber+"论文");
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
            subjectFile.setFileId(Integer.parseInt(fileInfomation.getId().toString()));
            subjectFile.setFileType(1);
            boolean save1 = subjectFileService.save(subjectFile);
            Subject subject = subjectService.getById(student.getSubjectId());
            if (save1){
                int progress = subject.getProgressId().equals(ProcessEnum.SUBMIT_PAPER.getProcessCode()) ?
                        subject.getProgressId() : (subject.getProgressId() + 1);
                subject.setProgressId(progress);
                flag = subjectService.updateById(subject);
            }
        }
        return flag ? CommonResult.success("提交成功") : CommonResult.failed();
    }

    // TODO: 2022/5/19 上传毕设
    @PostMapping("/paperManagement/fileUpload/design")
    public CommonResult<Object> uploadDesign(@RequestBody MultipartFile file, HttpServletRequest request){
        return null;
    }
}
