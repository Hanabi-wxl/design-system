package edu.dlu.bysj.document.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.entity.Subject;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.common.service.SubjectService;
import edu.dlu.bysj.document.entity.PaperCoverTemplate;
import edu.dlu.bysj.document.entity.SubjectApproveFormTemplate;
import edu.dlu.bysj.document.service.FileDownLoadService;
import edu.dlu.bysj.system.service.MajorService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/17 21:21
 */
@Controller
public class FileDownLoadController {

    private final FileDownLoadService fileDownLoadService;

    private final MajorService majorService;

    private final SubjectService subjectService;

    private final StudentService studentService;

    @Autowired
    public FileDownLoadController(FileDownLoadService fileDownLoadService, MajorService majorService,
        SubjectService subjectService, StudentService studentService) {
        this.fileDownLoadService = fileDownLoadService;
        this.majorService = majorService;
        this.subjectService = subjectService;
        this.studentService = studentService;
    }

    @RequestMapping(value = "/paperManagement/fileDownload/paperCover", method = RequestMethod.GET)
    public void paperCoverDownLoad(HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader("jwt");

        if (!StringUtils.isEmpty(jwt)) {
            Integer majorId = JwtUtil.getMajorId(jwt);
            List<PaperCoverTemplate> result = fileDownLoadService.packPaperCoverData(majorId);
            Major major = majorService.getById(1);
            String fileName = DateUtil.year(new Date()) + "_DADFP_" + major.getCode() + ".pdf";
            ClassPathResource classPathResource = new ClassPathResource("template/pdf/PaperCover.jasper");

            InputStream source = null;
            ServletOutputStream target = null;

            try {
                target = response.getOutputStream();
                source = classPathResource.getInputStream();
                /*填充报表并转换到outPutStream流中*/
                response.setContentLength(source.available());
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

                JasperRunManager.runReportToPdfStream(source, target, new HashMap<>(16),
                    new JRBeanCollectionDataSource(result));
            } catch (IOException | JRException e) {
                e.printStackTrace();
                throw new GlobalException(ResultCodeEnum.FAILED.getCode(), "论文封皮下载失败");
            } finally {
                try {
                    if (source != null) {
                        source.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequestMapping(value = "/paperManagement/fileDownload/subjectAuditTable", method = RequestMethod.GET)
    public void subjectApproveFormDownLoad(@RequestParam("subjectId") @NotNull String subjectId,
        HttpServletResponse response) {

        SubjectApproveFormTemplate result = fileDownLoadService.packPageSubjectApproveFormData(subjectId);
        Subject subject = subjectService.getById(subjectId);
        String studentNumber = null;
        String studentName = "";
        if (ObjectUtil.isNotNull(subject)) {
            Student student = studentService.getById(subject.getStudentId());
            studentNumber = student.getStudentNumber();
            studentName = student.getName();
        }

        Map<String, Object> objectMap = BeanUtil.beanToMap(result);

        /*生成二维码*/
        /*
         byte[] bytes = QrCodeUtil.generatePng(subject.getSubjectId(), 60, 60);
         ImageEntity image = new ImageEntity();
         image.setData(bytes);
         image.setColspan(1);
         image.setColspan(1);
         objectMap.put("qrCode", image);
         */

        /*加载模板填充数据*/
        ClassPathResource resource = new ClassPathResource("template/excel/SubjectApprovalForm.xls");
        String fileName = DateUtil.year(new Date()) + "_TMSPB_" + studentNumber + ".xls";
        TemplateExportParams params = new TemplateExportParams(resource.getPath(), true);

        Workbook workbook = ExcelExportUtil.exportExcel(params, objectMap);
        workbook.setSheetName(0, studentName + "的审题统计表");
        try {
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.FAILED.getCode(), "下载题目审批表失败");
        }
    }

    @RequestMapping(value = "fileDownload/statistics/topics", method = RequestMethod.GET)

    public void subjectSelectStaticsTable(HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader("jwt");
        if (!StringUtils.isEmpty(jwt)) {
            Integer majorId = JwtUtil.getMajorId(jwt);
            try {
                fileDownLoadService.staticsSubjectTable(majorId, response);
            } catch (IOException e) {
                e.printStackTrace();
                throw new GlobalException(ResultCodeEnum.FAILED.getCode(), "下载选题统计表失败");
            }
        }
    }
}
