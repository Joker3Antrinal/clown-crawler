package com.joker.just4fun.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 正则表达式工具
 *
 * @author Joker.Y
 * @since 2024/3/21
 * @version 1.0
 */
@Slf4j
public class RegExpUtil {

    // 英文字母(不分大小写)
    public static final String EN_EXP = "[A-Za-z]";
    // 双字节字符(包括汉字)
    public static final String DBL_BYTE_EXP = "[^\\x00-\\xff]";
    // 中文汉字
    public static final String ZH_CN_EXP = "[\\u4e00-\\u9fa5]";
    // 中文标点符号
    public static final String ZH_CN_MARK_EXP = "[\\u3002\\uff1b\\uff0c\\uff1a\\u201c\\u201d\\uff08\\uff09\\u3001\\uff1f\\u300a\\u300b]";
    // 除数字、字母、汉字外的“符号”
    public static final String MARK_EXP = "[^0-9A-Za-z\\u4e00-\\u9fff]";
    // 空白行
    public static final String SPACE_EXP = "\\n\\s*\\r";

    /*************以下为特殊限定*************/
    // 合法邮箱地址
    public static final String MAIL_URL = "[A-Za-z0-9]+([-._][A-Za-z0-9]+)*@[A-Za-z0-9]+(-[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6}|[A-Za-z]{2,4}\\.[A-Za-z]{2,3})";
    // 中国居民身份证18位
    public static final String ID_18_EXP = "[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]";
    // 中国居民身份证15位
    public static final String ID_15_EXP = "[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}";
    // 中国大陆固话号码
    public static final String ZH_ML_TEL = "(0\\d{2,3}(\\-|\\s*)?)?([2-9]\\d{6,7})";
    // 中国大陆客服热线
    public static final String ZH_ML_HL = "(400|800)(\\-|\\s*)?\\d{3}(\\-|\\s*)?\\d{4}";
    // 中国大陆手机号码
    public static final String ZH_ML_MBL = "((\\+86|\\+86(\\-|\\s*)?)|(86|86(\\-|\\s*)?)|(0086|0086(\\-|\\s*)?))?1[3|4|5|7|8|9]\\d{9}";


    /**
     * 匹配校验
     * @param input
     * @param regExp
     * @param fullMatchMode
     * @return
     */
    public static boolean matchValidate(String input, String regExp, boolean fullMatchMode){
        if (StringUtils.isBlank(regExp)) {
            log.error("=============== 正则表达式不能为空!! ===============");
            return false;
        }
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(input);
        if (fullMatchMode){
            return matcher.matches();
        }
        return matcher.find();
    }

    /**
     * 捕获匹配项
     * @param input
     * @param regExp
     * @return
     */
    public static List<String> matchCatch(String input, String regExp){
        List<String> matchRes;
        if (StringUtils.isBlank(regExp)) {
            log.error("=============== 正则表达式不能为空!! ===============");
            return null;
        }
        matchRes = new ArrayList<>();
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()){
            matchRes.add(matcher.group());
        }
        return matchRes;
    }
}
