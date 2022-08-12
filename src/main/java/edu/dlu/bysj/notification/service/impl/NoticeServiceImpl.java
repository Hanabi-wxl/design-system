package edu.dlu.bysj.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Notice;
import edu.dlu.bysj.base.model.enums.NoticeStatusEnum;
import edu.dlu.bysj.base.model.enums.NoticeTypeEnum;
import edu.dlu.bysj.base.model.vo.NoticeVo;
import edu.dlu.bysj.notification.mapper.NoticeMapper;
import edu.dlu.bysj.notification.service.NoticeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author XiangXinGang
 * @date 2021/11/8 20:43
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public List<NoticeVo> allNoticeList(Integer collegeId, Integer majorId) {
        // List allNoticeList

        // 从notice里筛选全校通知  type = 0
        // schoolList -> allNoticeList

        // 从notice里筛选全院通知
        // 1. jwtUtil - > collegeId
        // 2. type = 1 collegeId = id
        // collegeList -> allNoticeList

        // 从notice里筛选全专业通知
        // 1. jwtUtil - > majorId
        // 2. type = 2 and majorId = id
        // majorList -> allNoticeList

        // return allNoticeList
        List<NoticeVo> noticeVos = baseMapper.allNoticeList(majorId, collegeId);

        for (NoticeVo noticeVo : noticeVos) {

            String type = NoticeStatusEnum.noticeStatus(Integer.valueOf(noticeVo.getImportance()));
            String union = NoticeTypeEnum.noticeMessage(noticeVo.getType());
            String content = noticeVo.getContent();
            Integer college = noticeVo.getCollegeId();
            Integer major = noticeVo.getMajorId();
            LocalDateTime localDateTime = noticeVo.getTime();

            noticeVo.setTypeName(type);
            noticeVo.setImportance(union);
            noticeVo.setContent(content);
            noticeVo.setCollegeId(college);
            noticeVo.setMajorId(major);
            noticeVo.setTime(localDateTime);

        }

        return noticeVos;
    }
}
