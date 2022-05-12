package edu.dlu.bysj.paper.controller;

import edu.dlu.bysj.paper.model.dto.EntrustDto;
import edu.dlu.bysj.base.model.entity.Entrust;
import edu.dlu.bysj.base.model.vo.EntrustInfoVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.TeacherService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.paper.service.EntrustService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author XiangXinGang
 * @date 2021/10/31 8:52
 */
@RestController
@RequestMapping(value = "/paperManagement")
@Api(tags = "委托管理控制器")
@Validated
public class EntrustController {
    private final EntrustService entrustService;
    private final TeacherService teacherService;

    @Autowired
    public EntrustController(EntrustService entrustService,TeacherService teacherService) {
        this.entrustService = entrustService;
        this.teacherService = teacherService;
    }


    @PostMapping(value = "/entrust/setEntrust")
    @LogAnnotation(content = "设置题目委托")
    @RequiresPermissions({"entrust:subjectEntrust"})
    @ApiOperation(value = "设置题目委托")
    public CommonResult<Object> setUpSubjectEntrust(@RequestBody EntrustDto dto, HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        Integer subjectId = dto.getSubjectId();
        Integer consigneeId = dto.getConsigneeId();
        boolean save = false;
        if (!StringUtils.isEmpty(jwt)) {
            /*委托人id*/
            Integer userId = JwtUtil.getUserId(jwt);
            Entrust entrust = new Entrust();
            entrust.setSubjectId(subjectId);
            entrust.setConsigneeId(consigneeId);
            entrust.setConsignorId(userId);
            entrust.setDate(LocalDateTime.now());
            save = entrustService.save(entrust);
        }
        return save ? CommonResult.success(null) : CommonResult.failed();
    }


    @GetMapping(value = "/entrust/myList")
    @LogAnnotation(content = "获取我的委托列表")
    @RequiresPermissions({"entrust:list"})
    @ApiOperation(value = "获取我的委托列表")
    public CommonResult<TotalPackageVo<EntrustInfoVo>> selfSubjectEntrust(
              Integer pageSize,
              Integer pageNumber,
              String year,
              HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<EntrustInfoVo> result = null;
        Integer userId = JwtUtil.getUserId(jwt);
        String userNumber = teacherService.idToNumber(userId);
        if (!StringUtils.isEmpty(jwt)) {
            result = entrustService.selfEntrusts(Integer.valueOf(userNumber), year, pageNumber, pageSize);
        }
        return CommonResult.success(result);
    }

}
