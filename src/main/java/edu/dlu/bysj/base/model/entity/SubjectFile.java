package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author sinre
 * @create 05 19, 2022
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SubjectFile对象", description = "论文文件")
public class SubjectFile implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer subjectId;
    private Integer fileId;
    private Integer fileType;
    @TableLogic
    private Integer status;
}
