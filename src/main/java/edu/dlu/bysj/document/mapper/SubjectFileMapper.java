package edu.dlu.bysj.document.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dlu.bysj.base.model.entity.SubjectFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface SubjectFileMapper extends BaseMapper<SubjectFile> {
}
