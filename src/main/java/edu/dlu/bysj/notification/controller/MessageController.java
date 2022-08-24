package edu.dlu.bysj.notification.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlu.bysj.base.model.entity.Message;
import edu.dlu.bysj.base.model.entity.MessageFile;
import edu.dlu.bysj.base.model.entity.Student;
import edu.dlu.bysj.base.model.query.basic.CommonPage;
import edu.dlu.bysj.base.model.vo.AddMessageVo;
import edu.dlu.bysj.base.model.vo.MessageVo;
import edu.dlu.bysj.base.model.vo.ReceiveMessageVo;
import edu.dlu.bysj.base.model.vo.TotalPackageVo;
import edu.dlu.bysj.base.result.CommonResult;
import edu.dlu.bysj.base.util.JwtUtil;
import edu.dlu.bysj.common.service.StudentService;
import edu.dlu.bysj.log.annotation.LogAnnotation;
import edu.dlu.bysj.notification.service.MessageFileService;
import edu.dlu.bysj.paper.service.FileInformationService;
import edu.dlu.bysj.paper.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author XiangXinGang
 * @date 2021/11/8 22:18
 */
@RestController
@RequestMapping(value = "/notificationManagement")
@Api(tags = "消息管理控制器")
@Validated
@Slf4j
public class MessageController {

    private final StudentService studentService;

    private final FileInformationService fileInformationService;

    private final MessageService messageService;

    private final MessageFileService messageFileService;

    @Autowired
    public MessageController(MessageService messageService,
                             MessageFileService messageFileService,
                             FileInformationService fileInformationService,
                             StudentService studentService) {
        this.messageService = messageService;
        this.messageFileService = messageFileService;
        this.fileInformationService = fileInformationService;
        this.studentService = studentService;
    }

    @GetMapping(value = "/message/list")
    @LogAnnotation(content = "查看收件箱列表")
    @RequiresPermissions({"message:sendList"})
    @ApiOperation(value = "查看收件箱")
    public CommonResult<TotalPackageVo<MessageVo>> obtainMessageList(@Valid CommonPage commonPage,
                                                                     HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        TotalPackageVo<MessageVo> result = new TotalPackageVo<>();
        if (!StringUtils.isEmpty(jwt)) {
            Integer userId = JwtUtil.getUserId(jwt);
            Integer start = (commonPage.getPageNumber() - 1) * commonPage.getPageSize();
            result = messageService.messageListAsReviver(userId, start, commonPage.getPageSize());
        }
        return CommonResult.success(result);
    }

    @GetMapping(value = "/message/sendList")
    @LogAnnotation(content = "获取发件消息列表")
    @RequiresPermissions({"message:sendList"})
    @ApiOperation(value = "获取发件消息列表")
    public CommonResult<TotalPackageVo<ReceiveMessageVo>> obtainSenderList(@Valid CommonPage commonPage,
                                                                           HttpServletRequest request) {
        TotalPackageVo<ReceiveMessageVo> result = null;
        String jwt = request.getHeader("jwt");
        if (!StringUtils.isEmpty(jwt)) {
            Integer userId = JwtUtil.getUserId(jwt);
            Integer start = (commonPage.getPageNumber() - 1) * commonPage.getPageSize();
            result = messageService.messageListAsSender(userId, start, commonPage.getPageSize());
        }
        return CommonResult.success(result);
    }

    @GetMapping(value = "/message/readOrNot/{messageId}/{hasRead}")
    @LogAnnotation(content = "修改消息状态")
    @RequiresPermissions({"message:status"})
    @ApiOperation(value = "改变消息已读/未读状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id"),
            @ApiImplicitParam(name = "hasRead", value = "阅读标志（1：未读，0：已读）")
    })
    public CommonResult<Object> modifyReadStatus(@PathVariable("messageId") @NotNull Integer messageId,
                                                 @PathVariable("hasRead") @NotNull Integer hasRead) {
        Message message = messageService.getById(messageId);
        boolean flag = false;
        if (ObjectUtil.isNotNull(message)) {
            message.setHasRead(hasRead);
            flag = messageService.updateById(message);
        }
        return flag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
    }


    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/message/delete/{messageId}")
    @LogAnnotation(content = "删除消息")
    @RequiresPermissions({"message:delete"})
    @ApiOperation(value = "删除消息")
    @ApiImplicitParam(name = "messageId", value = "消息id")
    public CommonResult<Object> deleteMessage(@PathVariable("messageId") @NotNull Integer messageId) {
        boolean messageFlag = messageService.removeById(messageId);
//        boolean fileFlag = messageFileService.remove(new QueryWrapper<MessageFile>().eq("message_id", messageId));
        boolean fileFlag = true;
        return (messageFlag && fileFlag) ? CommonResult.success("删除成功") : CommonResult.failed("删除失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @GetMapping(value = "/message/batchDelete/{messageIds}")
    @LogAnnotation(content = "批量删除消息")
    @RequiresPermissions({"message:delete"})
    @ApiOperation(value = "批量删除消息")
    @ApiImplicitParam(name = "messageId", value = "消息id")
    public CommonResult<Object> batchDeleteMessage(@PathVariable("messageIds") @NotNull String messageIds) {
        boolean messageFlag = messageService.removeByIds(Arrays.asList(messageIds.split(",")));
//        boolean fileFlag = messageFileService.remove(new QueryWrapper<MessageFile>().eq("message_id", messageId));
        boolean fileFlag = true;
        return (messageFlag && fileFlag) ? CommonResult.success("删除成功") : CommonResult.failed("删除失败");
    }

    @GetMapping(value = "/message/detail/{messageId}")
    @LogAnnotation(content = "查看消息内容")
    @RequiresPermissions({"message:detail"})
    @ApiOperation(value = "获取消息详情")
    @ApiImplicitParam(name = "messageId", value = "消息id")
    public CommonResult<Map<String, Object>> obtainMessageContent(@PathVariable("messageId") @NotNull Integer messageId) {
        Map<String, Object> map = new HashMap<>(16);
        Message message = messageService.getById(messageId);
        map.put("content", message.getContent());
        List<String> fileUrl = new ArrayList<>();
        List<String> fileName = new ArrayList<>();
        List<MessageFile> messageFile = messageFileService.list(new QueryWrapper<MessageFile>().eq("message_id", message));

        //TODO 暂时未完成文件的地址放置,可能需要搭建自己的文件服务器

        map.put("fileUrl", fileUrl);
        map.put("fileName", fileName);
        return CommonResult.success(map);
    }


    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/message/send")
    @LogAnnotation(content = "教师给学生发送消息")
    @RequiresPermissions({"message:send"})
    @ApiOperation(value = "教师给学生发送消息")
    public CommonResult<Object> sendMessageOfTeacher(@Valid @RequestBody AddMessageVo addMessageVo,
                                                     HttpServletRequest request) {
        String jwt = request.getHeader("jwt");
        boolean messageFlag = false;
        boolean fileFlag = false;
        if (!StringUtils.isEmpty(jwt)) {
            Message message = new Message();
            message.setTitle(addMessageVo.getMessageTitle());
            message.setContent(addMessageVo.getMessageContent());
            Student student = studentService.getOne(new QueryWrapper<Student>().eq("student_number", addMessageVo.getReceiver()));
            /*当发送人不为空的时候*/
            if (ObjectUtil.isNotNull(student)) {
                message.setReceiverId(student.getId());
                message.setSenderId(JwtUtil.getUserId(jwt));
                message.setSendTime(LocalDateTime.now());
                message.setLevel(addMessageVo.getLevel());

                messageFlag = messageService.save(message);
                if (addMessageVo.getFileId() != null && !addMessageVo.getFileId().isEmpty()) {
                    List<MessageFile> messageFiles = new ArrayList<>();
                    for (int i = 0; i < addMessageVo.getFileId().size(); i++) {
                        MessageFile messageFile = new MessageFile();
                        messageFile.setMessageId(message.getId());
                        messageFile.setFileId(addMessageVo.getFileId().get(i));
                        messageFiles.add(messageFile);
                    }
                    fileFlag = messageFileService.saveBatch(messageFiles);
                } else {
                    /*没有文件插入*/
                    return messageFlag ? CommonResult.success("操作成功") : CommonResult.failed("操作失败");
                }
            }

        }

        return (fileFlag && messageFlag) ? CommonResult.success("操作成功") : CommonResult.failed("操作失败,请查看学生的学号是否正确，或则是否按规则填写学号");
    }
}
