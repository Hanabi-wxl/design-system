package edu.dlu.bysj.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 自定义转化器
 *
 * @author XiangXinGang
 * @date 2021/10/16 9:47
 */

@Configuration
public class MappingConverterAdapter {

    /**
     * 自定义LocalDateConvert (localDateTime)
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = null;
                try {
                    date = LocalDateTime.parse(source, df);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("转换异常");
                }
                return date;
            }
        };
    }

    /**
     * 自定义LocalDateConvert (localDate)
     *
     * @return
     */
    @Bean
    Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                /**
                 * DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                 * 		LocalDate date2 = LocalDate.parse(date1, fmt);
                 */

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = null;

                try {
                    date = LocalDate.parse(source, df);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("localDate类型转换异常");
                }
                return date;
            }
        };
    }
}
