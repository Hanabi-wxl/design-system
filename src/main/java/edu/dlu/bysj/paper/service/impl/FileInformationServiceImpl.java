package edu.dlu.bysj.paper.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlu.bysj.base.model.entity.FileInfomation;
import edu.dlu.bysj.paper.mapper.FileInformationMapper;
import edu.dlu.bysj.paper.service.FileInformationService;
import org.springframework.stereotype.Service;

/**
 * @author XiangXinGang
 * @date 2021/11/8 8:33
 */
@Service
public class FileInformationServiceImpl extends ServiceImpl<FileInformationMapper, FileInfomation> implements FileInformationService {
}
