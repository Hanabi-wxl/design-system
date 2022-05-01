package edu.dlu.bysj.base.model.dto;

/**
 * @author XiangXinGang
 * @date 2021/11/6 11:30
 */
public class EachMarkConvey {

    /**
     * 题目id
     */
    private Integer subjectId;

    /**
     * 教师id
     */
    private Integer teacherId;

    /**
     * 该题目的状态 , 1 未选(从数据库查询出来的状态为1未选,对应status字段) 0 选中
     */
    private Integer status;

    public EachMarkConvey() {
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
