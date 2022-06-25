package edu.dlu.bysj.document.service.impl;

import java.io.*;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.MyXWPFDocument;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.exception.GlobalException;
import edu.dlu.bysj.base.result.ResultCodeEnum;
import edu.dlu.bysj.document.entity.dto.OpenReportBaseInfo;
import edu.dlu.bysj.paper.mapper.OpenReportMapper;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import edu.dlu.bysj.base.model.entity.*;
import edu.dlu.bysj.base.model.entity.Class;
import edu.dlu.bysj.common.mapper.StudentMapper;
import edu.dlu.bysj.common.mapper.SubjectMapper;
import edu.dlu.bysj.common.mapper.TeacherMapper;
import edu.dlu.bysj.document.entity.PaperCoverTemplate;
import edu.dlu.bysj.document.entity.SubjectApproveFormTemplate;
import edu.dlu.bysj.document.entity.TopicSelectStaticsTemplate;
import edu.dlu.bysj.document.service.FileDownLoadService;
import edu.dlu.bysj.paper.mapper.SubjectTypeMapper;
import edu.dlu.bysj.system.mapper.ClassMapper;
import edu.dlu.bysj.system.mapper.CollegeMapper;
import edu.dlu.bysj.system.mapper.MajorMapper;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

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

    private final ClassMapper classMapper;

    public FileDownLoadServiceImpl(SubjectMapper subjectMapper, TeacherMapper teacherMapper,
        StudentMapper studentMapper, MajorMapper majorMapper, CollegeMapper collegeMapper, ClassMapper classMapper,
        SubjectTypeMapper subjectTypeMapper,OpenReportMapper openReportMapper) {
        this.openReportMapper = openReportMapper;
        this.subjectMapper = subjectMapper;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
        this.majorMapper = majorMapper;
        this.collegeMapper = collegeMapper;
        this.subjectTypeMapper = subjectTypeMapper;
        this.classMapper = classMapper;
    }

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
        College college = null;
        Class aClass = null;
        Major major = null;
        Student student = null;
        Map<Integer, Map<String, Object>> teacherInfo = null;
        SubjectType subjectType = null;
        String studentMajor = null;
        if (ObjectUtil.isNotNull(subject)) {
            teacherInfo = teacherMapper.ApprovelFormTeacherInfo(
                Arrays.asList(subject.getFirstTeacherId(), subject.getMajorLeadingId(), subject.getCollegeLeadingId()));
            major = majorMapper.selectById(subject.getMajorId());
            if (ObjectUtil.isNotNull(major)) {
                college = collegeMapper.selectById(major.getCollegeId());
            }
            student = studentMapper.selectById(subject.getStudentId());
            studentMajor = majorMapper.selectById(student.getMajorId()).getName();
            subjectType = subjectTypeMapper.selectById(subject.getSubjectTypeId());
            aClass = classMapper.selectById(student.getClassId());
        }
        return packageSubjectApproveForm(subject, teacherInfo, college.getName(), student.getName(), studentMajor, aClass.getName(),
                subjectType.getName());
    }

    @Override
    public void staticsSubjectTable(Integer majorId, HttpServletResponse response) throws IOException {
        /*该学院下的所有专业名称*/
        Map<Integer, Map<String, Object>> majorMap = majorMapper.selectAllMajorOfCollege(majorId);

        ClassPathResource resource = new ClassPathResource("template/excel/reportingTitleTable.xlsx");

        /*获取模板*/
        Workbook workbook = new XSSFWorkbook(resource.getInputStream());

        /*模板sheet*/
        Sheet sheetAt = workbook.getSheetAt(0);
        for (Map.Entry<Integer, Map<String, Object>> element : majorMap.entrySet()) {
            /*创建新sheet*/
            Sheet sheet = workbook.createSheet(((String)element.getValue().get("majorName")));
            fillingSelectStaticsSheetTitle(sheet, sheetAt, element.getValue());
            /*中间内容部分*/
            System.out.println("key" + element.getKey());
            List<TopicSelectStaticsTemplate> value = subjectMapper.selectAllSelectStaticsByMajorId(element.getKey());
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

                    this.fillingSelectStaticsContent(sheet, sheetAt, row + 1, value.get(i).getContent(), 4);
                    this.fillingSelectStaticsContent(sheet, sheetAt, row + 2, value.get(i).getNecessity(), 5);
                    this.fillingSelectStaticsContent(sheet, sheetAt, row + 3, value.get(i).getFeasiblity(), 6);
                    row += 4;
                }
            }
        }

        /*删除模板sheet(0)*/
        workbook.removeSheetAt(0);

        String fileName = DateUtil.year(new Date()) + "PRESUBJECT_" + majorMap.get(majorId).get("collegeId") + ".xlsx";

        /*设置导出参数*/
        response.setContentType("application/vnd.ms-excel");
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
            OutputStream outputStream = null;
            outputStream = response.getOutputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(new ClassPathResource(dir).getInputStream());
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void subjectOpenReportForm(HttpServletResponse response) {
        // 获取文件
        String dir = "template/file/openReportForm.doc";
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

    /**
     * 填充选题表部分;
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
    private void fillingSelectStaticsContent(Sheet target, Sheet source, int row, String value, int index) {
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
     * 填充选题表表头部分
     *
     * @param sheet
     * @param sheetAt
     * @param element
     */
    private void fillingSelectStaticsSheetTitle(Sheet sheet, Sheet sheetAt, Map<String, Object> element) {
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
        int year = DateUtil.year(new Date());
        titleCell.setCellValue(year + "-" + (year + 1) + "(2)学年本科生毕业论文(设计)报题统计表");
        titleRow.setHeightInPoints(25);

        /*学院专业打印时间*/
        Row printRow = sheet.createRow(1);
        Cell collegeMajorCell = printRow.createCell(0);
        collegeMajorCell.setCellStyle(sheetAt.getRow(1).getCell(0).getCellStyle());
        collegeMajorCell.setCellValue("学院: " + element.get("collegeName") + "  " + "专业: " + element.get("majorName"));

        Cell printDateCell = printRow.createCell(5);
        printDateCell.setCellStyle(sheetAt.getRow(1).getCell(1).getCellStyle());
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

}
