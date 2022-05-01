package edu.dlu.bysj.defense.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.DefenceRecord;
import edu.dlu.bysj.base.model.query.DefenseRecordQuery;
import edu.dlu.bysj.base.model.vo.DefenseRecordVo;
import edu.dlu.bysj.base.model.vo.RecordInfoVo;
import edu.dlu.bysj.base.model.vo.SimilarTeamStudentVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.defense.service.DefenseRecordService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/8 15:15
 */
@RestController
@RequestMapping(value = "/defenseManagement")
@Api(tags = "答辩记录控制器")
@Validated
public class DefenseRecordController {
    private final DefenseRecordService defenseRecordService;

    @Autowired
    public DefenseRecordController(DefenseRecordService defenseRecordService) {
        this.defenseRecordService = defenseRecordService;
    }

    @PostMapping(value = "/defence/record/add")
    @LogAnnotation(content = "新增/修改答辩记录")
    @RequiresPermissions({"record:add"})
    @ApiOperation(value = "新增/修改答辩记录")
    public CommonResult<Object> modifyDefenseQuestion(@Valid DefenseRecordVo recordVo,
                                                      HttpServletRequest request) {
        boolean flag = false;
        String jwt = request.getHeader("jwt");
        if (!StringUtils.isEmpty(jwt)) {
            DefenceRecord record = new DefenceRecord();
            record.setSubjectId(recordVo.getSubjectId());
            record.setId(record.getId());
            record.setQuestion(recordVo.getQuestionName());
            record.setAnswer(record.getAnswer());
            record.setResult(recordVo.getCorrect());
            record.setDate(LocalDate.now());
            record.setNoteTakerId(JwtUtil.getUserId(jwt));
            flag = defenseRecordService.saveOrUpdate(record);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @DeleteMapping(value = "/defence/record/delete/{recordId}")
    @LogAnnotation(content = "删除答辩记录")
    @RequiresPermissions({"record:delete"})
    @ApiOperation(value = "删除答辩记录")
    @ApiImplicitParam(name = "recordId", value = "答辩记录id")
    public CommonResult<Object> deleteDefenceRecord(@PathVariable("recordId") @NotNull Integer recordId) {
        return defenseRecordService.removeById(recordId) ? CommonResult.success("删除成功") : CommonResult.failed("删除失败");
    }

    @GetMapping(value = "/defence/record/list/{subjectId}")
    @LogAnnotation(content = "获取答辩记录列表")
    @RequiresPermissions({"record:list"})
    @ApiOperation(value = "获取答辩记录列表")
    @ApiImplicitParam(name = "subjectId", value = "题目id")
    public CommonResult<List<Map<String, Object>>> defenseRecordList(@PathVariable("subjectId") @NotNull Integer subjectId) {
        List<Map<String, Object>> array = new ArrayList<>();
        List<DefenceRecord> records = defenseRecordService.list(new QueryWrapper<DefenceRecord>()
                .eq("subject_id", subjectId));
        for (DefenceRecord element : records) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("recordId", element.getId());
            map.put("questionName", element.getQuestion());
            array.add(map);
        }
        return CommonResult.success(array);
    }

    @GetMapping(value = "defence/record/yearMajorGroup")
    @LogAnnotation(content = "查询组内学生信息")
    @RequiresPermissions({"record:yearGroup"})
    @ApiOperation(value = "查询组内学生信息")
    public CommonResult<TotalPackageVo<SimilarTeamStudentVo>> defenseRecordListOfSimilarTeam(@Valid DefenseRecordQuery query) {
        TotalPackageVo<SimilarTeamStudentVo> result = defenseRecordService.studentInfoOfTeam(query);
        return CommonResult.success(result);
    }


    @GetMapping(value = "/defence/record/detail/{recordId}")
    @LogAnnotation(content = "获取学生答辩记录详情")
    @RequiresPermissions({"record:detail"})
    @ApiOperation(value = "获取答辩记录详情")
    @ApiImplicitParam(name = "recordId", value = "记录id")
    public CommonResult<RecordInfoVo> obtainRecord(@PathVariable("recordId") @NotNull Integer recordId) {
        DefenceRecord record = defenseRecordService.getById(recordId);
        RecordInfoVo recordInfoVo = null;
        if (ObjectUtil.isNotNull(record)) {
            recordInfoVo = new RecordInfoVo();
            recordInfoVo.setQuestionName(record.getQuestion());
            recordInfoVo.setQuestionAnswer(record.getAnswer());
            recordInfoVo.setTime(record.getDate());
            recordInfoVo.setCorrect(record.getResult() == 1 ? "正确" : "不正确");
        }
        return CommonResult.success(recordInfoVo);
    }
}
