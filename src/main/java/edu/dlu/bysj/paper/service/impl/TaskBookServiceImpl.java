package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.TaskBook;
import edu.dlu.bysj.paper.mapper.TaskBookMapper;
import edu.dlu.bysj.paper.service.TaskBookService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/2 22:16
 */
@Service
public class TaskBookServiceImpl extends ServiceImpl<TaskBookMapper, TaskBook> implements TaskBookService {
}
