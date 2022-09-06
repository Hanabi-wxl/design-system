package edu.dlu.bysj.notification.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Major;
import edu.dlu.bysj.base.model.entity.Notice;
import edu.dlu.bysj.base.model.entity.NoticeFile;
import edu.dlu.bysj.base.model.query.NoticeListQuery;
import edu.dlu.bysj.base.model.vo.AddNoticeVo;
import edu.dlu.bysj.base.model.vo.NoticeVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.notification.mapper.NoticeMapper;
import edu.dlu.bysj.notification.service.NoticeFileService;
import edu.dlu.bysj.notification.service.NoticeService;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.system.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author XiangXinGang
 * @date 2021/11/8 20:39
 */
@RestController
@Api(tags = "通知列表")
@RequestMapping(value = "/notificationManagement")
@Validated
public class NotifyController {

    private final NoticeService noticeService;

    private final NoticeFileService noticeFileService;

    private final MajorService majorService;

    private final FileInformationService fileInformationService;

    private final NoticeMapper noticeMapper;

    public NotifyController(NoticeService noticeService,
                            NoticeFileService noticeFileService,
                            FileInformationService fileInformationService,
                            MajorService majorService, NoticeMapper noticeMapper) {
        this.noticeService = noticeService;
        this.majorService = majorService;
        this.noticeFileService = noticeFileService;
        this.fileInformationService = fileInformationService;
        this.noticeMapper = noticeMapper;
    }

    @GetMapping(value = "/notice/list")
    @RequiresPermissions({"notice:list"})
    @LogAnnotation(content = "查询本专业的通知列表")
    @ApiOperation(value = "查询本专业通知列表")
    public CommonResult<TotalPackageVo<NoticeVo>> obtainNotifyList(@Valid NoticeListQuery query,
                                                                   HttpServletRequest request) {

        String jwt = request.getHeader("jwt");
        Integer majorId = JwtUtil.getMajorId(jwt);
        String userType = JwtUtil.getUserType(jwt);
        Major major = majorService.getOne(new QueryWrapper<Major>().eq("id", majorId));
        Integer collegeId = major.getCollegeId();
        List<NoticeVo> allNoticeList = noticeService.allNoticeList(majorId,collegeId);

        List<NoticeVo> list1 = new ArrayList<>();
        List<NoticeVo> list2 = new ArrayList<>();
        List<NoticeVo> list3 = new ArrayList<>();

        for (NoticeVo noticeVo : allNoticeList) {

            String typeName = noticeVo.getTypeName();

            //学生则不显示隐藏的通知
            if (userType.equals("0")) {
                if (typeName.equals("隐藏")) {
                    continue;
                }
            }

            if (typeName.equals("置顶")) {
                list1.add(noticeVo);
            } else {
                list2.add(noticeVo);
            }
        }

        list3.addAll(list1);
        list3.addAll(list2);

        TotalPackageVo<NoticeVo> noticeVoTotalPackageVo = new TotalPackageVo<>();
        noticeVoTotalPackageVo.setTotal(list3.size());
        noticeVoTotalPackageVo.setArrays(list3);


        return CommonResult.success(noticeVoTotalPackageVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/notice/addOrModify")
    @LogAnnotation(content = "新增/修改通知")
    @RequiresPermissions({"notice:add"})
    @ApiOperation(value = "新增/修改通知")
    public CommonResult<Object> modifyNotice( @RequestBody AddNoticeVo noticeVo,
                                             HttpServletRequest request) {

        String jwt = request.getHeader("jwt");
        boolean noticeFlag = false;

        if (!StringUtils.isEmpty(jwt)) {

            Notice notice = null;
            if (ObjectUtil.isNotNull(noticeVo.getNoticeId())) {

                notice = noticeService.getById(noticeVo.getNoticeId());
            }

            if (ObjectUtil.isNull(notice)) {
                notice = new Notice();
            }

            notice.setCollegeId(noticeVo.getCollegeId());
            notice.setMajorId(noticeVo.getMajorId());
            notice.setType(noticeVo.getType());
            notice.setImportance(noticeVo.getImportance());
            notice.setTitle(noticeVo.getTitle());
            notice.setContent(noticeVo.getContent());
            notice.setSenderId(JwtUtil.getUserId(jwt));
            notice.setDate(LocalDateTime.now());

            noticeFlag = noticeService.saveOrUpdate(notice);
            /*若有文件则插入文件类型*/
            Integer id = notice.getId();
            if (noticeVo.getFileId() != null && !noticeVo.getFileId().isEmpty()) {

                for (Integer fileId : noticeVo.getFileId()) {

                    /*插入新的值*/
                    NoticeFile file = new NoticeFile();
                    file.setFileId(fileId);
                    file.setNoticeId(id);
                    boolean flag = noticeFileService.save(file);
                }
            }
        }

        return noticeFlag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }

    @RequiresPermissions({"notice:list"})
    @GetMapping(value = "/notice/getNoticeById/{noticeId}")
    @ApiOperation(value = "获取已增消息用于修改")
    public CommonResult<Object> getNoticeById(@PathVariable("noticeId") @NotNull Integer noticeId){

        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",noticeId);

        List<Notice> list = noticeMapper.selectList(queryWrapper);

        return CommonResult.success(list);

    }

    @GetMapping(value = "/notice/detail/{noticeId}")
    @LogAnnotation(content = "查看通知详情")
    @RequiresPermissions({"notice:detail"})
    @ApiOperation(value = "获取通知详情")
    @ApiImplicitParam(name = "noticeId", value = "通知id")
    public CommonResult<Map<String, Object>> obtainNoticeDetail(@PathVariable("noticeId") @NotNull Integer noticeId) {
        Notice notice = noticeService.getById(noticeId);
        List<NoticeFile> files = noticeFileService.list(new QueryWrapper<NoticeFile>().eq("notice_id", noticeId));

        List<String> fileUrl = new ArrayList<>();
        List<String> fileName = new ArrayList<>();

        //TODO 暂未完成,对文件地址和名称的显示,可能需要搭建一个自己的文件服务器

        Map<String, Object> map = new HashMap<>(16);

        LocalDateTime localDateTime = notice.getDate();
        map.put("publishTime", localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        map.put("content", notice.getContent());
        map.put("title", notice.getTitle());
        map.put("fileUrl", fileUrl);
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/notice/delete/{noticeId}")
    @LogAnnotation(content = "删除通知")
    @RequiresPermissions({"notice:delete"})
    @ApiOperation(value = "删除通知")
    @ApiImplicitParam(name = "noticeId", value = "通知id")
    public CommonResult<Object> deleteNotice(@PathVariable("noticeId") @NotNull Integer noticeId) {
//        boolean fileFlag = noticeFileService.remove(new QueryWrapper<NoticeFile>().eq("notice_id", noticeId));
        boolean fileFlag = true;
        boolean noticeFlag = noticeService.removeById(noticeId);

        return (fileFlag && noticeFlag) ? CommonResult.success("删除成功") : CommonResult.failed("删除失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/notice/batchDelete/{noticeIds}")
    @LogAnnotation(content = "批量删除通知")
    @RequiresPermissions({"notice:delete"})
    @ApiOperation(value = "批量删除通知")
    @ApiImplicitParam(name = "noticeIds", value = "通知ids")
    public CommonResult<Object> batchDelete(@PathVariable("noticeIds") @NotNull String ids) {
//        boolean fileFlag = noticeFileService.remove(new QueryWrapper<NoticeFile>().eq("notice_id", noticeId));
        boolean fileFlag = true;

        String[] noticeIds = ids.split(",");

        boolean noticeFlag = false;
        for (String noticeId : noticeIds) {
            noticeFlag = noticeService.removeById(Integer.parseInt(noticeId));
            if (!noticeFlag) {
                return CommonResult.failed("删除失败");
            }
        }
        return (fileFlag && noticeFlag) ? CommonResult.success("删除成功") : CommonResult.failed("删除失败");
    }
}
