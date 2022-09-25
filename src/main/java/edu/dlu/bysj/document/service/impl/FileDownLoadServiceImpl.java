package edu.dlu.bysj.document.service.impl;

import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.vo.MajorSimpleInfoVo;
import edu.dlu.bysj.base.util.GradeUtils;
import edu.dlu.bysj.document.entity.*;
import edu.dlu.bysj.document.entity.dto.OpenReportBaseInfo;
import edu.dlu.bysj.notification.mapper.MessageFileMapper;
import edu.dlu.bysj.notification.mapper.NoticeFileMapper;
import edu.dlu.bysj.document.mapper.SubjectFileMapper;
import edu.dlu.bysj.notification.mapper.NoticeMapper;
import edu.dlu.bysj.paper.mapper.FileInformationMapper;
import edu.dlu.bysj.paper.mapper.MessageMapper;
import edu.dlu.bysj.paper.mapper.OpenReportMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.common.mapper.StudentMapper;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.document.service.FileDownLoadService;
import edu.dlu.bysj.paper.mapper.SubjectTypeMapper;
import edu.dlu.bysj.system.mapper.ClassMapper;
import edu.dlu.bysj.system.mapper.CollegeMapper;
import edu.dlu.bysj.system.mapper.MajorMapper;
import org.springframework.util.FileCopyUtils;

/**
 * @author XiangXinGang
 * @date 2021/11/17 16:05
 */
@Service
public class FileDownLoadServiceImpl implements FileDownLoadService {

    private final SubjectMapper subjectMapper;

    private final TeacherMapper teacherMapper;

    private final StudentMapper studentMapper;

    private final MajorMapper majorMapper;

    private final CollegeMapper collegeMapper;

    private final SubjectTypeMapper subjectTypeMapper;

    private final OpenReportMapper openReportMapper;

    private final SubjectFileMapper subjectFileMapper;

    private final FileInformationMapper fileInformationMapper;

    private final ClassMapper classMapper;

    private final NoticeFileMapper noticeFileMapper;

    private final MessageFileMapper messageFileMapper;

    private final NoticeMapper noticeMapper;

    private final MessageMapper messageMapper;

    public FileDownLoadServiceImpl(SubjectMapper subjectMapper, TeacherMapper teacherMapper,
                                   StudentMapper studentMapper, MajorMapper majorMapper, CollegeMapper collegeMapper, ClassMapper classMapper,
                                   SubjectTypeMapper subjectTypeMapper, OpenReportMapper openReportMapper, SubjectFileMapper subjectFileMapper,
                                   FileInformationMapper fileInformationMapper, NoticeFileMapper noticeFileMapper, MessageFileMapper messageFileMapper, NoticeMapper noticeMapper, MessageMapper messageMapper) {
        this.fileInformationMapper = fileInformationMapper;
        this.subjectFileMapper = subjectFileMapper;
        this.openReportMapper = openReportMapper;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.majorMapper = majorMapper;
        this.collegeMapper = collegeMapper;
        this.subjectTypeMapper = subjectTypeMapper;
        this.classMapper = classMapper;
        this.noticeFileMapper = noticeFileMapper;
        this.messageFileMapper = messageFileMapper;
        this.noticeMapper = noticeMapper;
        this.messageMapper = messageMapper;
    }

    @Value("${design.system}")
    private String sys;

    @Override
    public List<PaperCoverTemplate> packPaperCoverData(Integer majorId) {
        List<PaperCoverTemplate> paperCoverTemplates = subjectMapper.selectPaperCoverInfo(majorId);
        for (PaperCoverTemplate paperCoverTemplate : paperCoverTemplates) {
            paperCoverTemplate.setGrade(String.valueOf(Integer.parseInt(paperCoverTemplate.getGrade())));
        }
        return paperCoverTemplates;
    }

    @Override
    public SubjectApproveFormTemplate packPageSubjectApproveFormData(String subjectId) {
        Subject subject = subjectMapper.selectBySubjectId(subjectId);
        if (ObjectUtil.isNull(subject))
            subject = subjectMapper.selectById(subjectId);
        College college = new College();
        Class aClass = new Class();
        Major major = new Major();
        Student student = new Student();
        Map<Integer, Map<String, Object>> teacherInfo = null;
        SubjectType subjectType = new SubjectType();
        String studentMajor = null;
        if (ObjectUtil.isNotNull(subject)) {
            teacherInfo = teacherMapper.ApprovelFormTeacherInfo(
                Arrays.asList(subject.getFirstTeacherId(), subject.getMajorLeadingId(), subject.getCollegeLeadingId()));
            if (teacherInfo != null && !teacherInfo.isEmpty()) {
                for (Integer key : teacherInfo.keySet()) {
                    Map<String, Object> objectMap = teacherInfo.get(key);
                    Integer majorId = (Integer) objectMap.get("majorId");
                    major = majorMapper.selectById(majorId);
                }
            }
            college = collegeMapper.selectById(major.getCollegeId());
            ObjectUtil.isNotNull(major);
            student = studentMapper.selectById(subject.getStudentId());
            subjectType = subjectTypeMapper.selectById(subject.getSubjectTypeId());
            if (ObjectUtil.isNotNull(student)) {
                studentMajor = majorMapper.selectById(student.getMajorId()).getName();
                aClass = classMapper.selectById(student.getClassId());
            } else {
                student = new Student();
            }
        }
        return packageSubjectApproveForm(subject, teacherInfo, college.getName(), student.getName(), studentMajor, aClass.getName(),
                subjectType.getName());
    }

    @Override
    public void staticsSubjectTable(Integer majorId, Integer year, HttpServletResponse response) throws IOException {
        /*该学院下的所有专业名称*/
        Map<Integer, Map<String, Object>> majorMap = majorMapper.selectAllMajorOfCollege(majorId);

        ClassPathResource resource = new ClassPathResource("template/excel/reportingTitleTable.xlsx");

        /*获取模板*/
        Workbook workbook = new XSSFWorkbook(resource.getInputStream());

        //sheetAt 是模板
        //sheet 是需要操作的
        /*模板sheet*/
        Sheet sheetAt = workbook.getSheetAt(0);
        for (Map.Entry<Integer, Map<String, Object>> element : majorMap.entrySet()) {
            /*创建新sheet*/
            Sheet sheet = workbook.createSheet(((String)element.getValue().get("majorName")));
            fillingReportStaticsSheetTitle(sheet, sheetAt, element.getValue(), year);
            /*中间内容部分*/
//            System.out.println("key" + element.getKey());

            List<ReportStaticsTemplate> value = subjectMapper.selectAllReportStaticsByMajorId(element.getKey());
            if (ObjectUtil.isNotNull(value)) {
                Row fixedRow2 = null;
                int row = 3;
                for (int i = 0; i < value.size(); i++) {
                    /*动态部分循环合并单元格*/
                    sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 2));
                    sheet.addMergedRegion(new CellRangeAddress(row + 1, row + 1, 0, 1));
                    sheet.addMergedRegion(new CellRangeAddress(row + 2, row + 2, 0, 1));
                    sheet.addMergedRegion(new CellRangeAddress(row + 3, row + 3, 0, 1));

                    sheet.addMergedRegion(new CellRangeAddress(row + 1, row + 1, 2, 8));
                    sheet.addMergedRegion(new CellRangeAddress(row + 2, row + 2, 2, 8));
                    sheet.addMergedRegion(new CellRangeAddress(row + 3, row + 3, 2, 8));
                    // 3
                    fixedRow2 = sheet.createRow(row);
                    Cell sequenceCell2 = fixedRow2.createCell(0);
                    sequenceCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    sequenceCell2.setCellValue(i + 1);

                    Cell topicCell2 = fixedRow2.createCell(1);

                    topicCell2.setCellStyle(sheetAt.getRow(3).getCell(1).getCellStyle());
                    topicCell2.setCellValue(value.get(i).getTopicName());
                    Cell topicCell3 = fixedRow2.createCell(2);
                    topicCell3.setCellStyle(sheetAt.getRow(3).getCell(2).getCellStyle());

                    Cell sNameCell2 = fixedRow2.createCell(3);
                    sNameCell2.setCellStyle(sheetAt.getRow(3).getCell(2).getCellStyle());
                    sNameCell2.setCellValue(value.get(i).getStudentName());

                    Cell cNameCell2 = fixedRow2.createCell(4);
                    cNameCell2.setCellStyle(sheetAt.getRow(3).getCell(3).getCellStyle());
                    cNameCell2.setCellValue(value.get(i).getClassName());

                    Cell gNameCell2 = fixedRow2.createCell(5);
                    gNameCell2.setCellStyle(sheetAt.getRow(3).getCell(4).getCellStyle());
                    gNameCell2.setCellValue(value.get(i).getGuideName());

                    Cell sourceCell2 = fixedRow2.createCell(6);
                    sourceCell2.setCellStyle(sheetAt.getRow(3).getCell(5).getCellStyle());
                    sourceCell2.setCellValue(value.get(i).getTopicSource());

                    Cell typeCell2 = fixedRow2.createCell(7);
                    typeCell2.setCellStyle(sheetAt.getRow(3).getCell(6).getCellStyle());
                    typeCell2.setCellValue(value.get(i).getTopicType());

                    Cell remarkCell2 = fixedRow2.createCell(8);
                    remarkCell2.setCellStyle(sheetAt.getRow(3).getCell(7).getCellStyle());
                    remarkCell2.setCellValue(value.get(i).getRemark());
                    fixedRow2.setHeightInPoints(25);

                    this.fillingReportStaticsContent(sheet, sheetAt, row + 1, value.get(i).getContent(), 4);
                    this.fillingReportStaticsContent(sheet, sheetAt, row + 2, value.get(i).getNecessity(), 5);
                    this.fillingReportStaticsContent(sheet, sheetAt, row + 3, value.get(i).getFeasiblity(), 6);
                    row += 4;
                }
            }
        }

        /*删除模板sheet(0)*/
        workbook.removeSheetAt(0);

        //乱码---
        //String fileName = "报题统计表_" +  GradeUtils.getGrade(DateUtil.year(new Date())) + "_PRESUBJECT_" + majorMap.get(majorId).get("collegeId") + ".xlsx";

        String fileName = "ReportTable_" +  GradeUtils.getGrade(DateUtil.year(new Date())) + "_PRESUBJECT_" + majorMap.get(majorId).get("collegeId") + ".xlsx";




        /*设置导出参数*/
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        /*将内容导出到outPutStream*/
        workbook.write(response.getOutputStream());
    }

    private void fileDownload(String dir, String fileName, HttpServletResponse response) {
        if (Objects.isNull(dir)) {
            // 如果接收参数为空则抛出异常，由全局异常处理类去处理。
            throw new NullPointerException("下载地址为空");
        }
        // 重置response
        response.reset();
        // ContentType，即告诉客户端所发送的数据属于什么类型
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("Access-control-Allow-Origin","*");
        // 设置编码格式
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 发送给客户端的数据
/*            OutputStream outputStream = null;
            outputStream = response.getOutputStream();

            byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource(dir).getInputStream());
            outputStream.write(bytes);*/
            File file = new File(dir);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subjectOpenReportForm(HttpServletResponse response) {
        // 获取文件
        String dir = "";
        if(sys.equals("win")) {
            dir = "template/file/openReportForm.doc";
        } else if(sys.equals("linux")) {
            dir = "/usr/fileDownload/openReportForm.doc";
        }
        String fileName = "开题报告模板_" + (DateUtil.year(new Date()) - 3) + ".doc";
        fileDownload(dir, fileName, response);
    }

    @Override
    public void openReport(String subjectId, HttpServletResponse response) {
        OpenReportBaseInfo baseInfo = openReportMapper.getFileBaseInfoById(subjectId);
        String fileName = baseInfo.getTitle() + ".pdf";
        String dir = baseInfo.getDir();
        fileDownload(dir, fileName, response);
    }

    @Override
    public void selectSubjectTable(Integer majorId, Integer year, HttpServletResponse response) throws IOException {
        /*该学院下的所有专业名称*/
        Map<Integer, Map<String, Object>> majorMap = majorMapper.selectAllMajorOfCollege(majorId);

        ClassPathResource resource = new ClassPathResource("template/excel/selectSubjectTable.xlsx");

        /*获取模板*/
        Workbook workbook = new XSSFWorkbook(resource.getInputStream());

        /*模板sheet*/
        Sheet sheetAt = workbook.getSheetAt(0);
        for (Map.Entry<Integer, Map<String, Object>> element : majorMap.entrySet()) {
            /*创建新sheet*/
            Sheet sheet = workbook.createSheet(((String)element.getValue().get("majorName")));
            fillingSelectStaticsSheetTitle(sheet, sheetAt, element.getValue(), year);
            /*中间内容部分*/
            System.out.println("key" + element.getKey());
            List<SelectStaticsTemplate> value = subjectMapper.selectAllSelectStaticsByMajorId(element.getKey());
            if (ObjectUtil.isNotNull(value)) {
                Row fixedRow2 = null;
                int row = 3;
                for (int i = 0; i < value.size(); i++) {

                    fixedRow2 = sheet.createRow(row);

                    Cell sequenceCell2 = fixedRow2.createCell(0);
                    sequenceCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    sequenceCell2.setCellValue(i + 1);

                    Cell topicCell2 = fixedRow2.createCell(1);
                    topicCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    topicCell2.setCellValue(value.get(i).getTopicName());

                    Cell topicCell3 = fixedRow2.createCell(2);
                    topicCell3.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    topicCell3.setCellValue(value.get(i).getStudentName());

                    Cell sNameCell2 = fixedRow2.createCell(3);
                    sNameCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    sNameCell2.setCellValue(value.get(i).getClassName());

                    Cell cNameCell2 = fixedRow2.createCell(4);
                    cNameCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    cNameCell2.setCellValue(value.get(i).getGuideName());

                    Cell gNameCell2 = fixedRow2.createCell(5);
                    gNameCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    gNameCell2.setCellValue(value.get(i).getGuideTitle());

                    Cell sourceCell2 = fixedRow2.createCell(6);
                    sourceCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    sourceCell2.setCellValue(value.get(i).getSourceName());

                    Cell typeCell2 = fixedRow2.createCell(7);
                    typeCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    typeCell2.setCellValue(value.get(i).getTypeName());

                    Cell remarkCell2 = fixedRow2.createCell(8);
                    remarkCell2.setCellStyle(sheetAt.getRow(3).getCell(0).getCellStyle());
                    remarkCell2.setCellValue(value.get(i).getFillingNumber());




                    fixedRow2.setHeightInPoints(25);
                    row++;
                }
            }
        }

        /*删除模板sheet(0)*/
        workbook.removeSheetAt(0);

        String fileName = "选题统计表_" +  GradeUtils.getGrade(DateUtil.year(new Date())) + "_XTTJB_" + majorMap.get(majorId).get("collegeId") + ".xlsx";

        /*设置导出参数*/
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        /*将内容导出到outPutStream*/
        workbook.write(response.getOutputStream());
    }

    @Override
    public void selectSubjectTableAnalysis(Integer collegeId, Integer year, HttpServletResponse response) throws IOException {
        ClassPathResource resource = new ClassPathResource("template/excel/selectAnalysisTable.xlsx");
        /*获取模板*/
        Workbook workbook = new XSSFWorkbook(resource.getInputStream());
        /*模板sheet*/
        Sheet sheetAt = workbook.getSheetAt(0);
        CollegeAnalysisTemplate template = new CollegeAnalysisTemplate();
        College college = collegeMapper.selectById(collegeId);
        Integer count = majorMapper.selectCount(new QueryWrapper<Major>().eq("college_id", collegeId));
        template.setMajorSum(count);
        int countStu = collegeMapper.selectStudentCount(collegeId);
        template.setStudentSum(countStu);
        template.setCollegeName(college.getName());

        /*创建新sheet*/
        Sheet sheet = workbook.createSheet(college.getName());

        fillingSelectAnalysisSheetTitle(sheet, sheetAt, template, year);
        /*中间内容部分*/
        Integer grade = GradeUtils.getGrade(year);
        List<MajorSimpleInfoVo> majorSimpleInfoVos = majorMapper.selectMajorInfoByCollegeId(collegeId);
        Row fixedRow2 = null;
        int row = 6;
        for (MajorSimpleInfoVo majorSimpleInfoVo : majorSimpleInfoVos) {
            Integer majorCount = studentMapper.selectCount(new QueryWrapper<Student>().eq("major_id", majorSimpleInfoVo.getMajorId()));
            Integer subCount = subjectMapper.selectCount(new QueryWrapper<Subject>()
                    .eq("major_id", majorSimpleInfoVo.getMajorId())
                    .isNotNull("student_id")
                    .isNotNull("first_teacher_id"));
            SubjectSelectAnalysis analysis = subjectMapper.selectAnalysisCount(majorSimpleInfoVo.getMajorId(),grade);
            if (ObjectUtil.isNotNull(analysis)) {
                fixedRow2 = sheet.createRow(row);
                fixedRow2.setHeightInPoints(43);
                Cell sequenceCell0 = fixedRow2.createCell(0);
                sequenceCell0.setCellStyle(sheetAt.getRow(6).getCell(0).getCellStyle());
                sequenceCell0.setCellValue(majorSimpleInfoVo.getMajorName());

                Cell sequenceCell1 = fixedRow2.createCell(1);
                sequenceCell1.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell1.setCellValue(majorCount);

                Cell sequenceCell2 = fixedRow2.createCell(2);
                sequenceCell2.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell2.setCellValue(subCount);

                Cell sequenceCell3 = fixedRow2.createCell(3);
                sequenceCell3.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell3.setCellValue(analysis.getGcsj());
                Cell sequenceCell4 = fixedRow2.createCell(4);
                sequenceCell4.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell4.setCellValue(analysis.getKxsy());
                Cell sequenceCell5 = fixedRow2.createCell(5);
                sequenceCell5.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell5.setCellValue(analysis.getRjkf());
                Cell sequenceCell6 = fixedRow2.createCell(6);
                sequenceCell6.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell6.setCellValue(analysis.getLlyj());
                Cell sequenceCell7 = fixedRow2.createCell(7);
                sequenceCell7.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell7.setCellValue(analysis.getZh1());
                Cell sequenceCell8 = fixedRow2.createCell(8);
                sequenceCell8.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell8.setCellValue(analysis.getQt1());
                Cell sequenceCell9 = fixedRow2.createCell(9);
                sequenceCell9.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell9.setCellValue(analysis.getLlxyj());
                Cell sequenceCell10 = fixedRow2.createCell(10);
                sequenceCell10.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell10.setCellValue(analysis.getYyxyj());
                Cell sequenceCell11 = fixedRow2.createCell(11);
                sequenceCell11.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell11.setCellValue(analysis.getYyrjsj());
                Cell sequenceCell12 = fixedRow2.createCell(12);
                sequenceCell12.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell12.setCellValue(analysis.getDcbg());
                Cell sequenceCell13 = fixedRow2.createCell(13);
                sequenceCell13.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell13.setCellValue(analysis.getZh2());
                Cell sequenceCell14 = fixedRow2.createCell(14);
                sequenceCell14.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell14.setCellValue(analysis.getQt2());
                Cell sequenceCell15 = fixedRow2.createCell(15);
                sequenceCell15.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell15.setCellValue(analysis.getGuideTotal());

                analysis = teacherMapper.selectAnalysisCount(majorSimpleInfoVo.getMajorId(), grade);
                int countTeacher = analysis.getGj() + analysis.getFgj() + analysis.getZj() + analysis.getCj() + analysis.getQt3();
                DecimalFormat df = new DecimalFormat("0.00%");

                Cell sequenceCell16 = fixedRow2.createCell(16);
                sequenceCell16.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell16.setCellValue(analysis.getGj());
                Cell sequenceCell17 = fixedRow2.createCell(17);
                sequenceCell17.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell17.setCellValue(df.format(1.0*analysis.getGj()/countTeacher));
                Cell sequenceCell18 = fixedRow2.createCell(18);
                sequenceCell18.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell18.setCellValue(analysis.getFgj());
                Cell sequenceCell19 = fixedRow2.createCell(19);
                sequenceCell19.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell19.setCellValue(df.format(1.0*analysis.getFgj()/countTeacher));
                Cell sequenceCell20 = fixedRow2.createCell(20);
                sequenceCell20.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell20.setCellValue(analysis.getZj());
                Cell sequenceCell21 = fixedRow2.createCell(21);
                sequenceCell21.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell21.setCellValue(df.format(1.0*analysis.getZj()/countTeacher));
                Cell sequenceCell22 = fixedRow2.createCell(22);
                sequenceCell22.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell22.setCellValue(analysis.getCj());
                Cell sequenceCell23 = fixedRow2.createCell(23);
                sequenceCell23.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell23.setCellValue(df.format(1.0*analysis.getCj()/countTeacher));
                Cell sequenceCell24 = fixedRow2.createCell(24);
                sequenceCell24.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell24.setCellValue(analysis.getQt3());
                Cell sequenceCell25 = fixedRow2.createCell(25);
                sequenceCell25.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell25.setCellValue(df.format(1.0*analysis.getQt3()/countTeacher));
                Cell sequenceCell26 = fixedRow2.createCell(26);
                sequenceCell26.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell26.setCellValue(analysis.getBs());
                Cell sequenceCell27 = fixedRow2.createCell(27);
                sequenceCell27.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell27.setCellValue(df.format(1.0*analysis.getBs()/countTeacher));
                Cell sequenceCell28 = fixedRow2.createCell(28);
                sequenceCell28.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell28.setCellValue(analysis.getSs());
                Cell sequenceCell29 = fixedRow2.createCell(29);
                sequenceCell29.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell29.setCellValue(df.format(1.0*analysis.getSs()/countTeacher));
                Cell sequenceCell30 = fixedRow2.createCell(30);
                sequenceCell30.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell30.setCellValue(analysis.getXs());
                Cell sequenceCell31 = fixedRow2.createCell(31);
                sequenceCell31.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell31.setCellValue(df.format(1.0*analysis.getXs()/countTeacher));
                Cell sequenceCell32 = fixedRow2.createCell(32);
                sequenceCell32.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell32.setCellValue(analysis.getQt4());
                Cell sequenceCell33 = fixedRow2.createCell(33);
                sequenceCell33.setCellStyle(sheetAt.getRow(6).getCell(1).getCellStyle());
                sequenceCell33.setCellValue(df.format(1.0*analysis.getQt4()/countTeacher));
            }
        }

        /*删除模板sheet(0)*/
        workbook.removeSheetAt(0);

        String fileName = "选题分析表_" +  GradeUtils.getGrade(DateUtil.year(new Date())) + "_XTFXB_" + college.getId() + ".xlsx";

        /*设置导出参数*/
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        /*将内容导出到outPutStream*/
        workbook.write(response.getOutputStream());
    }

    @Override
    public void paper(String subjectId, HttpServletResponse response) {
        Integer fileId = subjectFileMapper.selectOne(new QueryWrapper<SubjectFile>()
                .eq("subject_id", subjectId)
                .eq("file_type", 1)).getFileId();
        FileInfomation information = fileInformationMapper.selectOne(new QueryWrapper<FileInfomation>().eq("id", fileId));
        String fileName = information.getTitle();
        String dir = information.getDir();
        fileDownload(dir, fileName, response);
    }

    @Override
    public void design(String subjectId, HttpServletResponse response) {
        Integer fileId = subjectFileMapper.selectOne(new QueryWrapper<SubjectFile>()
                .eq("subject_id", subjectId)
                .eq("file_type", 2)).getFileId();
        FileInfomation information = fileInformationMapper.selectOne(new QueryWrapper<FileInfomation>().eq("id", fileId));
        String fileName = information.getTitle();
        String dir = information.getDir();
        fileDownload(dir, fileName, response);
    }

    private void setBorder(Sheet sheet, CellRangeAddress region) {

        // 合并单元格左边框样式
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格上边框样式
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格右边框样式
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

        // 合并单元格下边框样式
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);

    }


    private void fillingSelectAnalysisSheetTitle(Sheet sheet, Sheet sheetAt, CollegeAnalysisTemplate template, Integer year) {
        /*设置行高列宽*/
        sheet.setColumnWidth(0, sheetAt.getColumnWidth(0));
        sheet.setColumnWidth(1, sheetAt.getColumnWidth(1));
        sheet.setColumnWidth(2, sheetAt.getColumnWidth(2));
        sheet.setColumnWidth(3, sheetAt.getColumnWidth(3));
        sheet.setColumnWidth(4, sheetAt.getColumnWidth(4));
        sheet.setColumnWidth(5, sheetAt.getColumnWidth(5));
        sheet.setColumnWidth(6, sheetAt.getColumnWidth(6));
        sheet.setColumnWidth(7, sheetAt.getColumnWidth(7));
        sheet.setColumnWidth(8, sheetAt.getColumnWidth(8));
        sheet.setColumnWidth(9, sheetAt.getColumnWidth(9));
        sheet.setColumnWidth(10, sheetAt.getColumnWidth(10));
        sheet.setColumnWidth(11, sheetAt.getColumnWidth(11));
        sheet.setColumnWidth(12, sheetAt.getColumnWidth(12));
        sheet.setColumnWidth(13, sheetAt.getColumnWidth(13));
        sheet.setColumnWidth(14, sheetAt.getColumnWidth(14));
        sheet.setColumnWidth(15, sheetAt.getColumnWidth(15));
        sheet.setColumnWidth(16, sheetAt.getColumnWidth(16));
        sheet.setColumnWidth(17, sheetAt.getColumnWidth(17));
        sheet.setColumnWidth(18, sheetAt.getColumnWidth(18));
        sheet.setColumnWidth(19, sheetAt.getColumnWidth(19));
        sheet.setColumnWidth(20, sheetAt.getColumnWidth(20));
        sheet.setColumnWidth(21, sheetAt.getColumnWidth(21));
        sheet.setColumnWidth(22, sheetAt.getColumnWidth(22));
        sheet.setColumnWidth(23, sheetAt.getColumnWidth(23));
        sheet.setColumnWidth(24, sheetAt.getColumnWidth(24));
        sheet.setColumnWidth(25, sheetAt.getColumnWidth(25));
        sheet.setColumnWidth(26, sheetAt.getColumnWidth(26));
        sheet.setColumnWidth(27, sheetAt.getColumnWidth(27));
        sheet.setColumnWidth(28, sheetAt.getColumnWidth(28));
        sheet.setColumnWidth(29, sheetAt.getColumnWidth(29));
        sheet.setColumnWidth(30, sheetAt.getColumnWidth(30));
        sheet.setColumnWidth(31, sheetAt.getColumnWidth(31));
        sheet.setColumnWidth(32, sheetAt.getColumnWidth(32));
        sheet.setColumnWidth(33, sheetAt.getColumnWidth(33));

        /**/
        /*合并单元格，创建单元格填充数据*/
        /*设置跨行*/
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 33));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 33));
        sheet.addMergedRegion(new CellRangeAddress(2, 5, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(2, 5, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 5, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 8));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 8));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 14));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 9, 14));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 15, 33));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 4, 4));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 5, 5));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 6, 6));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 7, 7));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 8, 8));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 9, 9));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 10, 10));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 11, 11));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 12, 12));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 13, 13));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 14, 14));
        sheet.addMergedRegion(new CellRangeAddress(4, 5, 15, 15));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 16, 25));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 26, 33));

        /*表头 (1)*/
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        CellStyle cellStyle = sheetAt.getRow(0).getCell(0).getCellStyle();
        titleCell.setCellStyle(cellStyle);
        titleCell.setCellValue("大连大学本科生毕业论文（设计）选题及指导教师情况分析表");
        titleRow.setHeightInPoints(32);

        /*学院专业签字打印时间*/
        Row printRow = sheet.createRow(1);
        Cell collegeMajorCell = printRow.createCell(0);
        int grade = GradeUtils.getGrade(year);
        CellStyle printStyle = sheetAt.getRow(1).getCell(0).getCellStyle();
        collegeMajorCell.setCellStyle(printStyle);
        collegeMajorCell.setCellValue(template.getCollegeName()+"(公章)" + "  "
                + (grade+4) + "届  专业数：" + template.getMajorSum() + "  毕业生数：" + template.getStudentSum() + "  分管院长签字：" );

        printRow.setHeightInPoints(20);


        /*固定列名 (2)*/
        Row fixedRow2 = sheet.createRow(2);

        Cell sequenceCell = fixedRow2.createCell(0);
        sequenceCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        sequenceCell.setCellValue("专业名称");

        Cell topicCell = fixedRow2.createCell(1);
        topicCell.setCellStyle(sheetAt.getRow(2).getCell(1).getCellStyle());
        topicCell.setCellValue("本专业毕业人数");

        Cell sNameCell = fixedRow2.createCell(2);
        sNameCell.setCellStyle(sheetAt.getRow(2).getCell(2).getCellStyle());
        sNameCell.setCellValue("本专业毕业论文数");

        Cell cNameCell = fixedRow2.createCell(3);
        cNameCell.setCellStyle(sheetAt.getRow(2).getCell(3).getCellStyle());
        cNameCell.setCellValue("理、工、医类");

        Cell gNameCell = fixedRow2.createCell(9);
        gNameCell.setCellStyle(sheetAt.getRow(2).getCell(9).getCellStyle());
        gNameCell.setCellValue("文、管、艺术类");

        Cell teacherTitleCell = fixedRow2.createCell(15);
        teacherTitleCell.setCellStyle(sheetAt.getRow(2).getCell(15).getCellStyle());
        teacherTitleCell.setCellValue("指导教师情况");

        Row fixedRow3 = sheet.createRow(3);

        Cell lei1 = fixedRow3.createCell(3);
        lei1.setCellStyle(sheetAt.getRow(3).getCell(3).getCellStyle());
        lei1.setCellValue("毕业论文（设计）性质");

        Cell lei2 = fixedRow3.createCell(9);
        lei2.setCellStyle(sheetAt.getRow(3).getCell(9).getCellStyle());
        lei2.setCellValue("毕业论文论文性质");

        Row fixedRow4 = sheet.createRow(4);
        Cell lei43 = fixedRow4.createCell(3);
        lei43.setCellStyle(sheetAt.getRow(4).getCell(3).getCellStyle());
        lei43.setCellValue("工程设计");
        Cell lei44 = fixedRow4.createCell(4);
        lei44.setCellStyle(sheetAt.getRow(4).getCell(4).getCellStyle());
        lei44.setCellValue("科学实验");
        Cell lei45 = fixedRow4.createCell(5);
        lei45.setCellStyle(sheetAt.getRow(4).getCell(5).getCellStyle());
        lei45.setCellValue("软件开发");
        Cell lei46 = fixedRow4.createCell(6);
        lei46.setCellStyle(sheetAt.getRow(4).getCell(6).getCellStyle());
        lei46.setCellValue("理论研究");
        Cell lei47 = fixedRow4.createCell(7);
        lei47.setCellStyle(sheetAt.getRow(4).getCell(7).getCellStyle());
        lei47.setCellValue("综合");
        Cell lei48 = fixedRow4.createCell(8);
        lei48.setCellStyle(sheetAt.getRow(4).getCell(8).getCellStyle());
        lei48.setCellValue("其他");
        Cell lei49 = fixedRow4.createCell(9);
        lei49.setCellStyle(sheetAt.getRow(4).getCell(9).getCellStyle());
        lei49.setCellValue("理论性研究");
        Cell lei410 = fixedRow4.createCell(10);
        lei410.setCellStyle(sheetAt.getRow(4).getCell(10).getCellStyle());
        lei410.setCellValue("应用性研究");
        Cell lei411 = fixedRow4.createCell(11);
        lei411.setCellStyle(sheetAt.getRow(4).getCell(11).getCellStyle());
        lei411.setCellValue("应用软件设计");
        Cell lei412 = fixedRow4.createCell(12);
        lei412.setCellStyle(sheetAt.getRow(4).getCell(12).getCellStyle());
        lei412.setCellValue("调查报告");
        Cell lei413 = fixedRow4.createCell(13);
        lei413.setCellStyle(sheetAt.getRow(4).getCell(13).getCellStyle());
        lei413.setCellValue("综合");
        Cell lei414 = fixedRow4.createCell(14);
        lei414.setCellStyle(sheetAt.getRow(4).getCell(14).getCellStyle());
        lei414.setCellValue("其他");
        Cell lei415 = fixedRow4.createCell(15);
        lei415.setCellStyle(sheetAt.getRow(4).getCell(15).getCellStyle());
        lei415.setCellValue("指导教师总数");
        Cell lei416 = fixedRow4.createCell(16);
        lei416.setCellStyle(sheetAt.getRow(4).getCell(16).getCellStyle());
        lei416.setCellValue("职称情况");
        Cell lei426 = fixedRow4.createCell(26);
        lei426.setCellStyle(sheetAt.getRow(4).getCell(26).getCellStyle());
        lei426.setCellValue("学位情况");


        Row fixedRow5 = sheet.createRow(5);

        Cell lei516 = fixedRow5.createCell(16);
        lei516.setCellStyle(sheetAt.getRow(5).getCell(16).getCellStyle());
        lei516.setCellValue("高级职称人数");
        Cell lei517 = fixedRow5.createCell(17);
        lei517.setCellStyle(sheetAt.getRow(5).getCell(17).getCellStyle());
        lei517.setCellValue("所占比例");
        Cell lei518 = fixedRow5.createCell(18);
        lei518.setCellStyle(sheetAt.getRow(5).getCell(18).getCellStyle());
        lei518.setCellValue("副高级职称人数");
        Cell lei519 = fixedRow5.createCell(19);
        lei519.setCellStyle(sheetAt.getRow(5).getCell(19).getCellStyle());
        lei519.setCellValue("所占比例");
        Cell lei520 = fixedRow5.createCell(20);
        lei520.setCellStyle(sheetAt.getRow(5).getCell(20).getCellStyle());
        lei520.setCellValue("中级职称人数");
        Cell lei521 = fixedRow5.createCell(21);
        lei521.setCellStyle(sheetAt.getRow(5).getCell(21).getCellStyle());
        lei521.setCellValue("所占比例");
        Cell lei522 = fixedRow5.createCell(22);
        lei522.setCellStyle(sheetAt.getRow(5).getCell(22).getCellStyle());
        lei522.setCellValue("初级职称人数");
        Cell lei523 = fixedRow5.createCell(23);
        lei523.setCellStyle(sheetAt.getRow(5).getCell(23).getCellStyle());
        lei523.setCellValue("所占比例");
        Cell lei524 = fixedRow5.createCell(24);
        lei524.setCellStyle(sheetAt.getRow(5).getCell(24).getCellStyle());
        lei524.setCellValue("其他人数");
        Cell lei525 = fixedRow5.createCell(25);
        lei525.setCellStyle(sheetAt.getRow(5).getCell(25).getCellStyle());
        lei525.setCellValue("所占比例");
        Cell lei526 = fixedRow5.createCell(26);
        lei526.setCellStyle(sheetAt.getRow(5).getCell(26).getCellStyle());
        lei526.setCellValue("博士人数");
        Cell lei527 = fixedRow5.createCell(27);
        lei527.setCellStyle(sheetAt.getRow(5).getCell(27).getCellStyle());
        lei527.setCellValue("所占比例");
        Cell lei528 = fixedRow5.createCell(28);
        lei528.setCellStyle(sheetAt.getRow(5).getCell(28).getCellStyle());
        lei528.setCellValue("硕士人数");
        Cell lei529 = fixedRow5.createCell(29);
        lei529.setCellStyle(sheetAt.getRow(5).getCell(29).getCellStyle());
        lei529.setCellValue("所占比例");
        Cell lei530 = fixedRow5.createCell(30);
        lei530.setCellStyle(sheetAt.getRow(5).getCell(30).getCellStyle());
        lei530.setCellValue("学士人数");
        Cell lei531 = fixedRow5.createCell(31);
        lei531.setCellStyle(sheetAt.getRow(5).getCell(31).getCellStyle());
        lei531.setCellValue("所占比例");
        Cell lei532 = fixedRow5.createCell(32);
        lei532.setCellStyle(sheetAt.getRow(5).getCell(32).getCellStyle());
        lei532.setCellValue("其他");
        Cell lei533 = fixedRow5.createCell(33);
        lei533.setCellStyle(sheetAt.getRow(5).getCell(33).getCellStyle());
        lei533.setCellValue("所占比例");

        setBorder(sheet, new CellRangeAddress(2, 2, 3, 8));
        setBorder(sheet, new CellRangeAddress(3, 3, 3, 8));
        setBorder(sheet, new CellRangeAddress(2, 2, 9, 14));
        setBorder(sheet, new CellRangeAddress(3, 3, 9, 14));
        setBorder(sheet, new CellRangeAddress(2, 3, 15, 33));
        setBorder(sheet, new CellRangeAddress(0,0,0,33));
        setBorder(sheet, new CellRangeAddress(1, 1, 0, 33));
        setBorder(sheet, new CellRangeAddress(4, 4, 16, 25));
        setBorder(sheet, new CellRangeAddress(4, 4, 26, 33));
        setBorder(sheet, new CellRangeAddress(4, 5, 3, 3));
        setBorder(sheet, new CellRangeAddress(4, 5, 4, 4));
        setBorder(sheet, new CellRangeAddress(4, 5, 5, 5));
        setBorder(sheet, new CellRangeAddress(4, 5, 6, 6));
        setBorder(sheet, new CellRangeAddress(4, 5, 7, 7));
        setBorder(sheet, new CellRangeAddress(4, 5, 8, 8));
        setBorder(sheet, new CellRangeAddress(4, 5, 9, 9));
        setBorder(sheet, new CellRangeAddress(4, 5, 10, 10));
        setBorder(sheet, new CellRangeAddress(4, 5, 11, 11));
        setBorder(sheet, new CellRangeAddress(4, 5, 12, 12));
        setBorder(sheet, new CellRangeAddress(4, 5, 13, 13));
        setBorder(sheet, new CellRangeAddress(4, 5, 14, 14));
        setBorder(sheet, new CellRangeAddress(4, 5, 15, 15));
        setBorder(sheet, new CellRangeAddress(0,0,0,33));
        setBorder(sheet, new CellRangeAddress(1, 1, 0, 33));
        setBorder(sheet, new CellRangeAddress(2, 5, 0, 0));
        setBorder(sheet, new CellRangeAddress(2, 5, 1, 1));
        setBorder(sheet, new CellRangeAddress(2, 5, 2, 2));

        fixedRow2.setHeightInPoints(20);
        fixedRow3.setHeightInPoints(20);
        fixedRow4.setHeightInPoints(20);
        fixedRow5.setHeightInPoints(102);
    }

    /*
     * @Description: 选题表头
     * @Author: sinre
     * @Date: 2022/6/25 23:29
     * @param sheet
     * @param sheetAt
     * @param element
     * @param year
     * @return void
     **/
    private void fillingSelectStaticsSheetTitle(Sheet sheet, Sheet sheetAt, Map<String, Object> element, Integer year) {
        /*设置行高列宽*/
        sheet.setColumnWidth(0, sheetAt.getColumnWidth(0));
        sheet.setColumnWidth(1, sheetAt.getColumnWidth(1));
        sheet.setColumnWidth(2, sheetAt.getColumnWidth(2));
        sheet.setColumnWidth(3, sheetAt.getColumnWidth(3));
        sheet.setColumnWidth(4, sheetAt.getColumnWidth(4));
        sheet.setColumnWidth(5, sheetAt.getColumnWidth(5));
        sheet.setColumnWidth(6, sheetAt.getColumnWidth(6));
        sheet.setColumnWidth(7, sheetAt.getColumnWidth(7));
        sheet.setColumnWidth(8, sheetAt.getColumnWidth(8));
        sheet.setColumnWidth(9, sheetAt.getColumnWidth(9));

        /**/
        /*合并单元格，创建单元格填充数据*/
        /*设置跨行*/
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 5));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));

        /*表头 (1)*/
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        CellStyle cellStyle = sheetAt.getRow(0).getCell(0).getCellStyle();
        titleCell.setCellStyle(cellStyle);
        int grade = GradeUtils.getGrade(year);
        titleCell.setCellValue((grade+3) + "-" + (grade+4) + "(2)学年本科生毕业论文(设计)选题统计表");
        titleRow.setHeightInPoints(25);

        /*学院专业签字打印时间*/
        Row printRow = sheet.createRow(1);
        Cell collegeMajorCell = printRow.createCell(0);
        CellStyle printStyle = sheetAt.getRow(1).getCell(0).getCellStyle();
        collegeMajorCell.setCellStyle(printStyle);
        collegeMajorCell.setCellValue("学院: " + element.get("collegeName") + "  " + "专业: " + element.get("majorName"));

        Cell signCell = printRow.createCell(2);
        signCell.setCellStyle(printStyle);
        signCell.setCellValue("教学院长签字: ");

        Cell printCell = printRow.createCell(6);
        printCell.setCellStyle(printStyle);
        printCell.setCellValue("打印时间:" + DateUtil.format(new Date(), "yyyy-MM-dd"));

        printRow.setHeightInPoints(20);

        /*固定列名 (2)*/
        Row fixedRow = sheet.createRow(2);
        Cell sequenceCell = fixedRow.createCell(0);
        sequenceCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        sequenceCell.setCellValue("序号");

        Cell topicCell = fixedRow.createCell(1);
        topicCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        topicCell.setCellValue("毕业论文(设计)选题");

        Cell sNameCell = fixedRow.createCell(2);
        sNameCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        sNameCell.setCellValue("学生姓名");

        Cell cNameCell = fixedRow.createCell(3);
        cNameCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        cNameCell.setCellValue("班级");

        Cell gNameCell = fixedRow.createCell(4);
        gNameCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        gNameCell.setCellValue("指导教师姓名");

        Cell teacherTitleCell = fixedRow.createCell(5);
        teacherTitleCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        teacherTitleCell.setCellValue("指导教师职称");

        Cell sourceCell = fixedRow.createCell(6);
        sourceCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        sourceCell.setCellValue("*题目来源");

        Cell typeCell = fixedRow.createCell(7);
        typeCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        typeCell.setCellValue("*题目类型");

        Cell fillingCell = fixedRow.createCell(8);
        fillingCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        fillingCell.setCellValue("归档序号");

        fixedRow.setHeightInPoints(30);
    }


    /**
     * 填充报题表部分;
     *
     * @param target
     *            目标sheet
     * @param source
     *            模板sheet
     * @param row
     *            行
     * @param value
     *            行值;
     */
    private void fillingReportStaticsContent(Sheet target, Sheet source, int row, String value, int index) {
        /*行头部分*/
        Row contentRow = target.createRow(row);

        Cell contentMarkCell = contentRow.createCell(0);
        contentMarkCell.setCellStyle(source.getRow(index).getCell(0).getCellStyle());
        contentMarkCell.setCellValue(source.getRow(index).getCell(0).getStringCellValue());
        Cell contentMarkCell2 = contentRow.createCell(1);
        contentMarkCell2.setCellStyle(source.getRow(index).getCell(1).getCellStyle());

        /*行内容*/
        Cell contentCell = contentRow.createCell(2);
        contentCell.setCellStyle(source.getRow(index).getCell(2).getCellStyle());
        contentCell.setCellValue(value);

        Cell contentCell1 = contentRow.createCell(3);
        contentCell1.setCellStyle(source.getRow(index).getCell(3).getCellStyle());

        Cell contentCell2 = contentRow.createCell(4);
        contentCell2.setCellStyle(source.getRow(index).getCell(4).getCellStyle());

        Cell contentCell3 = contentRow.createCell(5);
        contentCell3.setCellStyle(source.getRow(index).getCell(5).getCellStyle());

        Cell contentCell4 = contentRow.createCell(6);
        contentCell4.setCellStyle(source.getRow(index).getCell(6).getCellStyle());

        Cell contentCell5 = contentRow.createCell(7);
        contentCell5.setCellStyle(source.getRow(index).getCell(7).getCellStyle());

        Cell contentCell6 = contentRow.createCell(8);
        contentCell6.setCellStyle(source.getRow(index).getCell(8).getCellStyle());

        contentRow.setHeightInPoints(75);

    }

    /**
     * 填充报题表表头部分
     *
     * @param sheet
     * @param sheetAt
     * @param element
     */
    private void fillingReportStaticsSheetTitle(Sheet sheet, Sheet sheetAt, Map<String, Object> element, Integer year) {
        /*设置行高列宽*/
        sheet.setColumnWidth(0, sheetAt.getColumnWidth(0));
        sheet.setColumnWidth(1, sheetAt.getColumnWidth(1));
        sheet.setColumnWidth(2, sheetAt.getColumnWidth(2));
        sheet.setColumnWidth(3, sheetAt.getColumnWidth(3));
        sheet.setColumnWidth(4, sheetAt.getColumnWidth(4));
        sheet.setColumnWidth(5, sheetAt.getColumnWidth(5));
        sheet.setColumnWidth(6, sheetAt.getColumnWidth(6));
        sheet.setColumnWidth(7, sheetAt.getColumnWidth(7));
        sheet.setColumnWidth(8, sheetAt.getColumnWidth(8));

        /**/
        /*合并单元格，创建单元格填充数据*/
        /*设置跨行*/
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 8));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));

        /*表头 (1)*/
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(sheetAt.getRow(0).getCell(0).getCellStyle());
        int grade = GradeUtils.getGrade(year);
        titleCell.setCellValue((grade+3) + "-" + (grade + 4) + "(2)学年本科生毕业论文(设计)报题统计表");
        titleRow.setHeightInPoints(25);

        /*学院专业打印时间*/
        Row printRow = sheet.createRow(1);
        Cell collegeMajorCell = printRow.createCell(0);
        collegeMajorCell.setCellStyle(sheetAt.getRow(1).getCell(0).getCellStyle());
        collegeMajorCell.setCellValue("学院: " + element.get("collegeName") + "  " + "专业: " + element.get("majorName"));

        Cell printDateCell = printRow.createCell(5);
        printDateCell.setCellStyle(sheetAt.getRow(1).getCell(0).getCellStyle());
        printDateCell.setCellValue("打印时间:" + DateUtil.format(new Date(), "yyyy-MM-dd"));
        printRow.setHeightInPoints(20);

        /*固定列名 (2)*/
        Row fixedRow = sheet.createRow(2);
        Cell sequenceCell = fixedRow.createCell(0);
        sequenceCell.setCellStyle(sheetAt.getRow(2).getCell(0).getCellStyle());
        sequenceCell.setCellValue("序号");

        Cell topicCell = fixedRow.createCell(1);
        topicCell.setCellStyle(sheetAt.getRow(2).getCell(1).getCellStyle());
        topicCell.setCellValue("题目名称");

        Cell topicCell1 = fixedRow.createCell(2);
        topicCell1.setCellStyle(sheetAt.getRow(2).getCell(2).getCellStyle());

        Cell sNameCell = fixedRow.createCell(3);
        sNameCell.setCellStyle(sheetAt.getRow(2).getCell(2).getCellStyle());
        sNameCell.setCellValue("学生姓名");

        Cell cNameCell = fixedRow.createCell(4);
        cNameCell.setCellStyle(sheetAt.getRow(2).getCell(3).getCellStyle());
        cNameCell.setCellValue("班级");

        Cell gNameCell = fixedRow.createCell(5);
        gNameCell.setCellStyle(sheetAt.getRow(2).getCell(4).getCellStyle());
        gNameCell.setCellValue("指导教师姓名");

        Cell sourceCell = fixedRow.createCell(6);
        sourceCell.setCellStyle(sheetAt.getRow(2).getCell(5).getCellStyle());
        sourceCell.setCellValue("题目来源");

        Cell typeCell = fixedRow.createCell(7);
        typeCell.setCellStyle(sheetAt.getRow(2).getCell(6).getCellStyle());
        typeCell.setCellValue("题目类型");

        Cell remarkCell = fixedRow.createCell(8);
        remarkCell.setCellStyle(sheetAt.getRow(2).getCell(7).getCellStyle());
        remarkCell.setCellValue("备注");

        fixedRow.setHeightInPoints(30);
    }

    private SubjectApproveFormTemplate packageSubjectApproveForm(Subject subject,
                                                                 Map<Integer, Map<String, Object>> teacherInfo,
                                                                 String collegeName, String studentName, String studentMajor, String className,
                                                                 String paperType) {

        SubjectApproveFormTemplate result = new SubjectApproveFormTemplate();

        result.setCollege(collegeName);
        if (teacherInfo != null && !teacherInfo.isEmpty()) {
            for (Integer key : teacherInfo.keySet()) {
                Map<String, Object> objectMap = teacherInfo.get(key);
                String teacherName = (String)objectMap.get("teacherName");
                String titleName = (String)objectMap.get("titleName");
                String majorName = ((String)objectMap.get("majorName"));
                if (key.equals(subject.getFirstTeacherId())) {
                    result.setGuideTeacherName(teacherName);
                    result.setGuideTeacherTitle(titleName);
                    result.setGuideTeacherMajor(majorName);
                }

                if (key.equals(subject.getMajorLeadingId())) {
                    result.setMajorTeacherName(teacherName);
                }

                if (key.equals(subject.getCollegeLeadingId())) {
                    result.setCollegeTeacherName(teacherName);
                }
            }
        }
        result.setStudentName(studentName + "(" + className + ")");
        if (ObjectUtil.isNull(studentName)) {
            result.setStudentName("");
        }
        result.setStudentMajor(studentMajor);
        result.setPaperType(paperType);
        if (ObjectUtil.isNotNull(subject)) {
            result.setFirst(subject.getIsFirstTeach());
            result.setNeedNumber(subject.getStudentNeeded());
            result.setSimilar(subject.getIsSimilar());
            result.setSubjectName(subject.getSubjectName());
            result.setContent(subject.getTitleAbstract());
            result.setNecessity(subject.getNecessity());
            result.setFeasibility(subject.getFeasibility());
            result.setMajorOpinion(subject.getMajorAgree());
            result.setCollegeOption(subject.getCollegeAgree());
            result.setGuideOpinionDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(subject.getSubmitDate()));
            if(ObjectUtil.isNotNull(subject.getMajorDate())){
                result.setMajorOpinionDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(subject.getMajorDate()));
            }
            if(ObjectUtil.isNotNull(subject.getCollegeDate())){
                result.setCollegeOpinionDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(subject.getCollegeDate()));
            }
        }
        return result;
    }

    @Override
    public void notice(String noticeId, HttpServletResponse response) {
        Integer fileId = noticeFileMapper.selectOne(new QueryWrapper<NoticeFile>()
                .eq("notice_id", noticeId)).getFileId();
        FileInfomation information = fileInformationMapper.selectOne(new QueryWrapper<FileInfomation>().eq("id", fileId));

        String dir = information.getDir();

        //获取文件的后缀名
        String suffix = dir.substring(dir.lastIndexOf(".") + 1);

        String fileName = noticeMapper.selectOne(new QueryWrapper<Notice>()
                .eq("id",noticeId)).getTitle() + "." + suffix;

        fileDownload(dir, fileName, response);
    }

    @Override
    public void message(String messageId, HttpServletResponse response) {
        Integer fileId = messageFileMapper.selectOne(new QueryWrapper<MessageFile>()
                .eq("message_id", messageId)).getFileId();
        FileInfomation information = fileInformationMapper.selectOne(new QueryWrapper<FileInfomation>().eq("id", fileId));

        String dir = information.getDir();

        //获取文件的后缀名
        String suffix = dir.substring(dir.lastIndexOf(".") + 1);

        String fileName = messageMapper.selectOne(new QueryWrapper<Message>()
                .eq("id", messageId)).getTitle() + "." + suffix;

        fileDownload(dir, fileName, response);
    }

}
