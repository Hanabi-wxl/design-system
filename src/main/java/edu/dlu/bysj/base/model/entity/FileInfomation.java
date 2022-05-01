package edu.dlu.bysj.base.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件信息
 * </p>
 *
 * @author XiangXinGang
 * @since 2021-10-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FileInfomation对象", description = "文件信息")
public class FileInfomation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "文件类型（例如：中期检查表）")
    private String type;

    @ApiModelProperty(value = "文件标题")
    private String title;

    @ApiModelProperty(value = "文件摘要")
    private String summary;

    @ApiModelProperty(value = "文件地址")
    private String dir;

    @ApiModelProperty(value = "下载次数")
    private Integer count;

    @ApiModelProperty(value = "上传日期")
    private LocalDateTime uploadDate;

    @ApiModelProperty(value = "此条数据是否有效（1：有效，0：无效）")
    @TableLogic
    private Integer status;

    @ApiModelProperty(name = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "是否为学生(1:学生,0老师)")
    private Integer isStudent;


}
