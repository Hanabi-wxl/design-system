package edu.dlu.bysj.base.model.enums;

/**
 * @author XiangXinGang
 * @date 2021/10/20 10:15
 */
public enum RedisKeyEnum {
    MAJOR_FUNCTION_TABLE_KEY(
            "专业功能时间表缓存key",
            "edu.dlu.bysj.system.controller.scheduleController:majorFunction:",
            "String",
            "majorId"),
    TEACHER_TITLE_DEGREE_KEY(
            "教师学位和职称缓存Key",
            "edu.dlu.bysj.common.service.impl.TeacherServiceImpl:DegreeAndTile:",
            "String",
            "teacherId"),
    TEACHER_ROLE_NAME_KEY(
            "对教师角色名称的缓存key",
            "edu.dlu.bysj.system.service.impl.TeacherRoleServiceImpl:teacherRoleName:",
            "set",
            "teacherId"),
    TEACHER_MANAGER_ROLE_KEY(
            "系统中三类管理员(专业,学院,学校)的缓存key",
            "edu.dlu.bysj.system.service.impl.TeacherRoleServiceImpl:teacherMangerRoles:",
            "list",
            "roleId"),
    TOTAL_ROLE_KEY(
            "该系统的所有角色进行缓存key",
            "edu.dlu.bysj.system.controller.RoleController:roleList:",
            "list",
            ""),
    ROLE_FUNCTION_KEY(
            "角色-功能缓存key",
            "edu.dlu.bysj.system.service.impl.RoleFunctionServiceImpl:ObtainFunctionIds:",
            "list",
            "roleId"),
    COLLEGE_ZSET_KEY(
            "学校的学院缓存key(zset)",
            "edu.dlu.bysj.system.service.impl.CollegeServiceImpl:collegePagination:zsetKey:",
            "zset",
            "schoolId"),
    COLLEGE_HASH_KEY(
            "学校的学院缓存key(hash)",
            "edu.dlu.bysj.system.service.impl.CollegeServiceImpl:collegePagination:hashKey",
            "hash",
            "schoolId"),
    MAJOR_ZSET_KEY(
            "学院专业缓存key(zset)",
            "edu.dlu.bysj.system.service.impl.MajorServiceImpl:majorPaginationZsetKye:",
            "zset",
            "collegeId"),
    MAJOR_HASH_KEY(
            "学院专业缓存key(hash)",
            "edu.dlu.bysj.system.service.impl.MajorServiceImpl:majorPaginationHashKye:",
            "hash",
            "collegeId"),
    MAJOR_TEACHER_INFO_KEY(
            "专业教师信息进行缓存",
            "edu.dlu.bysj.common.service.impl.TeacherServiceImpl:getTeacherInfo:",
            "list",
            "majorId"),
    DEGREE_NAME_ID_KEY(
            "学位名称,id 缓存id",
            "edu.dlu.bysj.common.service.impl.DegreeServiceImpl:allDegreeSimplifyInfo:",
            "list",
            ""),
    OFFICE_NAME_ID_KEY(
            "办公室名称缓存ke",
            "edu.dlu.bysj.common.service.impl.OfficeServiceImpl:officeSimplifyInfo:",
            "list",
            ""),
    CLASS_MAJOR_NAME_ID_KEY(
            "专业班级名称,id缓存key",
            "edu.dlu.bysj.system.service.impl.ClassServiceImpl:classSimplifyInfo:",
            "list",
            "majorId"),
    ROLE_NAME_ID_KEY(
            "对所有角色的名称和id缓存key",
            "edu.dlu.bysj.system.service.impl.RoleServiceImpl:roleInfo:",
            "List",
            ""),
    SCHOOL_NAME_ID_KEY(
            "对学校名称和id进行缓存",
            "edu.dlu.bysj.system.service.impl.SchoolServiceImpl:schoolInfo:",
            "list",
            ""),
    COLLEGE_MAJOR_KEY(
            "该专业所属学院的所有专业缓存Key",
            "edu.dlu.bysj.common.service.impl.TeacherServiceImpl::teacherMajorByCollegeId",
            "list",
            "majorId"),
    MAJOR_ALL_TEACHER_NAME_ID_KEY(
            "本专业的所有老师,name ,id 缓存key",
            "edu.dlu.bysj.common.service.impl.TeacherServiceImpl:majorTeacherInfo:",
            "list",
            "majorId"),
    SOURCE_NAME_ID_KEY(
            "对题目来源的name, id,组成的集合key",
            "edu.dlu.bysj.paper.service.impl.SourceServiceImpl:checkAllSourceInfo:",
            "list",
            ""),

    SUBJECT_TYPE_NAME_ID_KEY(
            "对题目的类型缓存key",
            "edu.dlu.bysj.paper.service.impl.SubjectTypeServiceImpl:subjectTypeInfo:",
            "list",
            ""),
    TEACHER_SHORTEN_INFO_KEY("" +
            "对教师简单自身信息缓存key",
            "edu.dlu.bysj.common.service.impl.TeacherServiceImpl:teacherShortenInfo:",
            "String",
            "teacherId"),

    COLLEGE_MAJOR_SIMPLIFY_INFO_KEY(
            "对该学院下的专业名称,专业id进行缓存",
            "edu.dlu.bysj.system.service.impl.MajorServiceImpl:obtainCollegeMajor:",
            "list",
            "collegeId"),
    COLLEGE_NAME_ID_KEY(
            "学校下的学院名称，id",
            "edu.dlu.bysj.system.service.impl.CollegeServiceImpl:obtainCollegeInfoBySchool:",
            "list",
            "schoolId"
    ),
    COLLEGE_TEACHER_KEY(
            "学院内的老师",
            "edu.dlu.bysj.system.service.impl.MajorServiceImpl:obtainCollegeTeacher:",
            "list",
            "collegeId"
    );


    RedisKeyEnum(String keyDescription, String keyValue, String keyType, String changeValue) {
        this.keyDescription = keyDescription;
        this.keyValue = keyValue;
        this.keyType = keyType;
        this.changeValue = changeValue;
    }

    /**
     * 对redis key 的描述
     */
    private String keyDescription;
    /**
     * redis key 的固定值;
     */
    private String keyValue;

    /**
     * 使用的redis数据类型;
     */
    private String keyType;

    /**
     * private 变化值
     *
     * @return
     */
    private String changeValue;

    public String getKeyDescription() {
        return keyDescription;
    }

    public void setKeyDescription(String keyDescription) {
        this.keyDescription = keyDescription;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }
}
