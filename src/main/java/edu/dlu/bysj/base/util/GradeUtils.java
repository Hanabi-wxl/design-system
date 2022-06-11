package edu.dlu.bysj.base.util;

import java.time.LocalDateTime;

/**
 * 获取年级
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
