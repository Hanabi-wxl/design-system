package edu.dlu.bysj.document.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import edu.dlu.bysj.base.model.entity.MiddleCheck;
import edu.dlu.bysj.base.model.vo.SubjectTableVo;
import edu.dlu.bysj.document.entity.MiddleCheckTemplate;
import edu.dlu.bysj.document.entity.PaperCoverTemplate;
import edu.dlu.bysj.document.entity.SubjectApproveFormTemplate;
import io.swagger.models.auth.In;

/**
 * @author XiangXinGang
 * @date 2021/11/17 16:05
 */
public interface FileDownLoadService {
    /**
     * 打包该专业的下载论文封皮信息的
     * 
     * @param majorId
     * @return
     */
    List<PaperCoverTemplate> packPaperCoverData(Integer majorId);

    /**
     * 打包题目审批表数据
     * 
     * @param subjectId
     * @return
     */
    SubjectApproveFormTemplate packPageSubjectApproveFormData(String subjectId);

    /**
     * 下载选题统计表
     * 
     * @param majorId
     *            专业id
     * @param response
     *            返回结果
     */
    void staticsSubjectTable(Integer majorId, Integer year, HttpServletResponse response) throws IOException;

    void subjectOpenReportForm(HttpServletResponse response) throws UnsupportedEncodingException;

    void openReport(String subjectId, HttpServletResponse response) throws Exception;

    void selectSubjectTable(Integer majorId, Integer year, HttpServletResponse response) throws IOException;

    void selectSubjectTableAnalysis(Integer collegeId, Integer year, HttpServletResponse response) throws IOException;

    void paper(String subjectId, HttpServletResponse response);

    void design(String subjectId, HttpServletResponse response);

    void notice(String noticeId, HttpServletResponse response);

    void message(String messageId, HttpServletResponse response);


    MiddleCheckTemplate packPageMiddleCheckInfo(MiddleCheck middleCheck, SubjectTableVo subjectTableVo);

    void staticsEachTable(Integer majorId, Integer year, HttpServletResponse response) throws IOException;

    void staticsGroupTable(Integer majorId, Integer year, Boolean isRepeat, HttpServletResponse response) throws IOException;

    void teacherInfoTable(HttpServletResponse response);

    void studentInfoTable(HttpServletResponse response);

    void middleStatistics(Integer majorId, Integer year, HttpServletResponse response) throws IOException;
}
