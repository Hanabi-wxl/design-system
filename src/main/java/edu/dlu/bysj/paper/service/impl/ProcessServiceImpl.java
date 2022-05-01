package edu.dlu.bysj.paper.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.Process;
import edu.dlu.bysj.base.model.vo.ProcessDetailVo;
import edu.dlu.bysj.paper.mapper.ProcessMapper;
import edu.dlu.bysj.paper.service.ProcessService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiangXinGang
 * @date 2021/11/5 22:33
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {


    @Override
    public List<ProcessDetailVo> processDetail(Integer subjectId) {
        return baseMapper.selectProcessInfoBySubjectId(subjectId);
    }

    @Override
    public List<Map<String, Object>> processStatus(Integer subjectId) {
        List<Process> processList = this.list(new QueryWrapper<Process>().eq("subject_id", subjectId));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Process element : processList) {
            Map<String, Object> map = new HashMap<>(16);
            /*根据教师评语和学生评语,日期是否完成判断*/
            if (!StringUtils.isEmpty(element.getTeacherComment()) && !StringUtils.isEmpty(element.getTeacherComment())
                    && ObjectUtil.isNotNull(element.getStudentDate()) && ObjectUtil.isNotNull(element.getTeacherDate())) {
                map.put("week", element.getWeek());
                map.put("status", 2);
            } else if (!StringUtils.isEmpty(element.getStudentComment()) && ObjectUtil.isNotNull(element.getStudentDate())) {
                /*学生填写了但是老师没填写该周就为进行中*/
                map.put("week", element.getWeek());
                map.put("status", 1);
            } else if (StringUtils.isEmpty(element.getStudentComment())) {
                /*学生都没填写该周就为未开始*/
                map.put("week", element.getWeek());
                map.put("status", 0);
            }
            result.add(map);
        }
        return result;
    }
}
