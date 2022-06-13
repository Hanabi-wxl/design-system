package edu.dlu.bysj.base.util;

import java.time.LocalDateTime;

/**
 * 获取年级
 *
 * 解释：年级划分以9月为基础
 * 9月前所执行的为正在进行答辩相关的年级
 *
 * 若手动传入年份信息
 * 若传入年份与本年年份相同则按上述规则执行
 * 若不同则执行毕业年份的年级
 *
 * 例：2022年1月执行的为2018级答辩
 *    2022年12月执行的为2019级答辩
 *
 * 若传入 2021 则输出2021年毕业的2017级
 *
 * @author sinre
 * @create 06 11, 2022
 * @since 1.0.0
 */
public class GradeUtils {
    public static Integer getGrade(){
        int year = LocalDateTime.now().getYear();
        int monthValue = LocalDateTime.now().getMonthValue();
        return monthValue >= 9 ? year - 3 : year - 4;
    }
    public static Integer getGrade(int year){
        int yearValue = LocalDateTime.now().getYear();
        int monthValue = LocalDateTime.now().getMonthValue();
        if (year == yearValue)
            return monthValue >= 9 ? year - 3 : year - 4;
        else
            return year - 4;
    }
}
