package com.jumore.devmaster.common.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author Long
 * @version $Id: StringUtil.java, v 0.1 2015年6月8日 下午5:35:09 YongW Exp $
 */
public class StringUtil {

    /**  获取随机数的数组*/
    private final static String[] RAND_STR = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * 判断字符窜是否为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str));
    }

    /**
     * 判断字符窜是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 将对象转换为整数
     */
    public static int strToInt(Object o) {
        String str = valueOf(o);
        return strToInt(str);
    }

    /**
     * 将字符窜转换为整数,如果转换过程中发生异常,则返回最小整数值
     * 
     * @param str
     * @return 如果转换过程中发生异常,则返回最小整数值
     */
    public static int strToInt(String str) {
        int i = Integer.MIN_VALUE;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            i = Integer.MIN_VALUE;
        }

        return i;
    }

    /**
     * 判断字符串是否为数字组成
     * 2011.01.12 by sunyouhua
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 从页面获取字符串转码
     * 2011.03.12 by songxianyou
     * @param str
     * @return
     */
    public static String transcoding(String str, CHART_ENCODEING_TYPE codType) {
        if (str == null)
            return "";
        String changedStr = "";
        try {
            if (codType == null)
                changedStr = URLDecoder.decode(str, CHART_ENCODEING_TYPE.UTF_8.code());
            else
                changedStr = URLDecoder.decode(str, codType.code());
        } catch (UnsupportedEncodingException e1) {
            return str.trim();
        }
        return changedStr == null ? "" : changedStr.trim();
    }

    /**
     * 编码类型
     * @author W  2011-3-17
     */
    public enum CHART_ENCODEING_TYPE {

        UTF_8("utf-8");

        private String codeType;

        CHART_ENCODEING_TYPE(String code) {
            this.codeType = code;
        }

        public String code() {
            return codeType;
        }
    }

    /**
     * 获取8位随机数
     * @return
     */
    public static String getRandomNum() {
        String s = "";
        for (int i = 0; i < 8; i++) {
            int a = (int) (Math.random() * 62);
            s += RAND_STR[a];
        }
        return s;
    }

    /**
     * 获取6位随机数,只有数字
     * @return
     */
    public static String getRandomNum2() {
        String s = "";
        for (int i = 0; i < 6; i++) {
            int a = (int) (Math.random() * 10);
            s += RAND_STR[a];
        }
        return s;
    }

    /**

     * 获取用户IP

     */
    public static String getRequestIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

    public static Integer parseInteger(String objValue) {
        if (objValue == null)
            return null;
        try {
            return Integer.parseInt(objValue.toString());
        } catch (Exception e) {
        }
        return null;
    }

    public static Integer parseInt(String objValue) {
        if (objValue == null)
            return null;
        try {
            return Integer.parseInt(objValue.toString());
        } catch (Exception e) {
        }
        return null;
    }

    public static Long parseLong(String objValue) {
        if (objValue == null)
            return null;
        try {
            return Long.parseLong(objValue.toString());
        } catch (Exception e) {
        }
        return null;
    }

    public static Double parseDouble(String objValue) {
        if (objValue == null)
            return null;
        try {
            return Double.parseDouble(objValue.toString());
        } catch (Exception e) {
        }
        return null;
    }

    public static Float parseFloat(String objValue) {
        if (objValue == null)
            return null;
        try {
            return Float.parseFloat(objValue.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @description 根据传入的字符串以及类型字符串 返回相应类型的数据,出错则返回null
     * @author GZL
     * @date 2015-2-5
     */
    public static Object parseObjectToString(Object obj, String javaType) {
        try {
            if (obj == null || javaType == null || javaType.trim().length() == 0) {
                return null;
            }
            Method tempMethod = null;
            //得到大写开头的类型 如：long -> Long int->Int
            String mName = javaType.substring(0, 1).toUpperCase() + javaType.substring(1);
            if (mName.equals("String")) {
                return obj;
            }
            if (mName.equals("Date")) {
                if (obj.toString().length() == 10) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    return format.format(obj);
                } else if (obj.toString().length() > 10) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return format.format(obj);
                }
            }
            Method[] methods = StringUtil.class.getMethods();
            for (Method m : methods) {
                if (m.getName().equals("parse" + mName) && m.getGenericParameterTypes().length == 1
                    && m.getGenericParameterTypes()[0] == String.class) {
                    tempMethod = m;
                    break;
                }
            }
            return tempMethod.invoke(null, obj);

        } catch (Exception e) {
            //TODO 
        }
        return null;
    }

    public static boolean isNull(String str) {
        return str == null || "".equals(str.trim()) ? true : false;
    }

    public static boolean isNotNull(String str) {
        return str == null || "".equals(str.trim()) ? false : true;
    }

    // 把对象转换成String
    public static String valueOf(Object obj) {
        return (obj == null || "".equals(obj) || "null".equals(obj)) ? "" : obj.toString().trim();
    }

    /**
      * 自动补对应的 字符位
      * @param isLeft    是否从左边补位
      * @param fillChar  自动填补的字符
      * @param length    填补后的总字符串长度
      * @param originalValue 原字符串 
      * @return
      */
    public static String fillString(boolean isLeft, char fillChar, int length, String originalValue) {
        if (null == originalValue || "".equals(originalValue))
            return "";

        int originalLength = originalValue.length();
        if (length <= originalLength)
            return originalValue;

        String fillCharStr = "";
        for (int op = 1; op <= length - originalLength; op++) {
            fillCharStr = fillChar + fillCharStr;
        }
        return isLeft ? (fillCharStr + originalValue) : (originalValue + fillCharStr);
    }

    /**
     * 去除填充字符
     * @param isLeft
     * @param fillChar
     * @param originalValue
     * @return
     */
    public static String removeFillChar(boolean isLeft, char fillChar, String originalValue) {
        if (null == originalValue || "".equals(originalValue))
            return "";
        int fillLength = 0;
        int originalLength = originalValue.length();

        if (isLeft) {
            for (int op = 0; op < originalLength; op++) {
                if (originalValue.substring(op, op + 1).equals(String.valueOf(fillChar)))
                    fillLength++;
                else
                    break;
            }
            return originalValue.substring(fillLength);
        } else {
            for (int op = originalLength; op > 0; op--) {
                if (originalValue.substring(op - 1, op).equals(String.valueOf(fillChar)))
                    fillLength++;
                else
                    break;
            }
            return originalValue.substring(0, originalLength - fillLength);
        }
    }

    /**
     * 获取随机的6位数的字符串形式
     * @return
     */
    public static String genRandom() {
        int[] intRet = new int[6];
        int intRd = 0; // 存放随机数
        int count = 0; // 记录生成的随机数个数
        int flag = 0; // 是否已经生成过标志
        while (count < 6) {
            Random rdm = new Random(System.currentTimeMillis());
            intRd = Math.abs(rdm.nextInt()) % 9 + 1;
            for (int i = 0; i < count; i++) {
                if (intRet[i] == intRd) {
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
            if (flag == 0) {
                intRet[count] = intRd;

            }
            count++;
        }
        String str = "";
        for (int t = 0; t < 6; t++) {
            str = str + intRet[t];
        }
        return str;
    }

    /**
     * 字符数字转化为int型数字
     * @param value
     * @return
     */
    public static int getInt(String value) {
        int i = 0;
        try {
            if (value != null)
                value = value.trim();
            i = Integer.parseInt(value);
        } catch (Exception e) {
        }
        return i;
    }

    /**
     * 转码 js encodeURIComponent 编码后数据
     * yzh
     * 2015-03-27
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(str.getBytes("ISO8859-1"), "UTF-8");
    }

    /**
     * 扩展并右对齐字符串，用指定字符填充左边。
     * <pre>
     * StringUtil.alignRight(null, *, *)     = null
     * StringUtil.alignRight("", 3, 'z')     = "zzz"
     * StringUtil.alignRight("bat", 3, 'z')  = "bat"
     * StringUtil.alignRight("bat", 5, 'z')  = "zzbat"
     * StringUtil.alignRight("bat", 1, 'z')  = "bat"
     * StringUtil.alignRight("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str 要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @param padChar 填充字符
     *
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignRight(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }

        int pads = size - str.length();

        if (pads <= 0) {
            return str;
        }

        return alignRight(str, size, String.valueOf(padChar));
    }

    /**
     * 扩展并右对齐字符串，用指定字符串填充左边。
     * <pre>
     * StringUtil.alignRight(null, *, *)      = null
     * StringUtil.alignRight("", 3, "z")      = "zzz"
     * StringUtil.alignRight("bat", 3, "yz")  = "bat"
     * StringUtil.alignRight("bat", 5, "yz")  = "yzbat"
     * StringUtil.alignRight("bat", 8, "yz")  = "yzyzybat"
     * StringUtil.alignRight("bat", 1, "yz")  = "bat"
     * StringUtil.alignRight("bat", -1, "yz") = "bat"
     * StringUtil.alignRight("bat", 5, null)  = "  bat"
     * StringUtil.alignRight("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str 要对齐的字符串
     * @param size 扩展字符串到指定宽度
     * @param padStr 填充字符串
     *
     * @return 扩展后的字符串，如果字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String alignRight(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }

        if ((padStr == null) || (padStr.length() == 0)) {
            padStr = " ";
        }

        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;

        if (pads <= 0) {
            return str;
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();

            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }

            return new String(padding).concat(str);
        }
    }

    /**
     * 物性表中文名称 # 号替换成 /
     * @param name
     * @return
     */
    public static String formatCateNameCn(String name) {
        if (StringUtil.isEmpty(name)) {
            return "";
        }
        return name.replace("#","/");
    }

    /**
     * 生成有遮挡的公司名称
     * @param companyName
     * @return
     */
    public static String getHideCompanyName(String companyName) {
        String sign = "******" ;
        if (companyName == null || "".equals(companyName.trim())) {
            return "";
        }
        if (companyName.length() <= 2) {
            return companyName.substring(0,1) + sign;
        } else if (companyName.length() == 3) {
            return companyName.substring(0,2) + sign;
        } else if (companyName.length() > 3) {
            return companyName.substring(0,2) + sign + companyName.substring(companyName.length() - 2);
        }

        return "";
    }

    /**
     * 高亮显示
     * 不区分大小写
     * @param name
     * @param keywords
     * @return
     */
    public static String highLight(String name, String keywords) {
        if (name == null || "".equals(name)) {
            return "";
        }
        if (keywords == null || "".equals(keywords)) {
            return name;
        }
        StringBuffer sb = new StringBuffer();
        String[] keywordsArr=keywords.split(" ");
		//去除空元素
		List<String> list=new ArrayList<String>();
		for(int i=0;i<keywordsArr.length;i++){
			if(!keywordsArr[i].equals("")){
				list.add(keywordsArr[i].trim());
			}
		}
        if(list!=null&&list.size()>0&&!list.get(0).equals("")){
        	boolean flag=false;
        	for(int i=0;i<list.size();i++){
        		String keywordsStr=list.get(i);
        		Pattern p = Pattern.compile(keywordsStr, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(name);
                while(m.find()) {
                	flag=true;
                	m.appendReplacement(sb,"<em class='red'>"+m.group()+"</em>");
                }
                if(flag){
                	m.appendTail(sb);
                	break;
                }else{
                	if(i==list.size()-1){
                		m.appendTail(sb);
                    	break;
                	}
                }
        	}
        }else{
        	Pattern p = Pattern.compile(keywords, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(name);
            while(m.find()) {
                m.appendReplacement(sb,"<em class='red'>"+m.group()+"</em>");
            }
            m.appendTail(sb);
        }
        return sb.toString();
    }
    
    /**
     * 字符串转码
     * 2016.04.28 by 
     * @param str
     * @return
     */
    public static String searchCoding(String str) {
        if (str == null)
            return "";
        String changedStr = "";
        try {
                changedStr = URLEncoder.encode(str, CHART_ENCODEING_TYPE.UTF_8.code());
        } catch (UnsupportedEncodingException e1) {
            return str.trim();
        }
        return changedStr;
    }

    /**
     * 根据指定长度截取字符串
     * @param strSource 原始字符串
     * @param beginIndex	开始位置
     * @param subLen	截取长度
     * @return	String
     */
    public static String subString(String strSource, int beginIndex, int subLen) {

        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }

        if(subLen<0)
        {
            throw new StringIndexOutOfBoundsException(subLen);
        }

        if ((beginIndex + subLen) > strSource.length()) {
            throw new StringIndexOutOfBoundsException(beginIndex + subLen);
        }

        char[] values = strSource.toCharArray();

        return ((beginIndex == 0) && ((beginIndex + subLen) == values.length)) ? strSource
                : new String(values, beginIndex, subLen);
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     *
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * </p>
     *
     * @param str 要处理的字符串
     *
     * @return 除去空白的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trim(String str) {
        return trim(str, null, 0);
    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * @param str 要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     *
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trim(String str, String stripChars) {
        return trim(str, stripChars, 0);
    }

    /**
     * 除去字符串头部的空白，如果字符串是 <code>null</code> ，则返回 <code>null</code>。
     *
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * </p>
     *
     * @param str 要处理的字符串
     *
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     *         ，则返回 <code>null</code>
     */
    public static String trimStart(String str) {
        return trim(str, null, -1);
    }

    /**
     * 除去字符串头部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * @param str 要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trimStart(String str, String stripChars) {
        return trim(str, stripChars, -1);
    }

    /**
     * 除去字符串尾部的空白，如果字符串是 <code>null</code> ，则返回 <code>null</code>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     *         ，则返回 <code>null</code>
     */
    public static String trimEnd(String str) {
        return trim(str, null, 1);
    }

    /**
     * 除去字符串尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * @param str 要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trimEnd(String str, String stripChars) {
        return trim(str, stripChars, 1);
    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * @param str 要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @param mode
     *            <code>-1</code> 表示trimStart， <code>0</code> 表示trim全部，
     *            <code>1</code> 表示trimEnd
     *
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    private static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;

        // 扫描字符串头部
        if (mode <= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (Character.isWhitespace(str.charAt(start)))) {
                    start++;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(start)) != -1)) {
                    start++;
                }
            }
        }

        // 扫描字符串尾部
        if (mode >= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (Character.isWhitespace(str.charAt(end - 1)))) {
                    end--;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                    end--;
                }
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }

    /**
     * 数组转字符串
     * @param array 数组
     * @return
     */
    public static String ArrayToString(Object... array){
        return ArrayToString(null, array);
    }

    /**
     * 数组转字符串
     * @param separator 分隔符
     * @param array 数组
     * @return
     */
    public static String ArrayToString(String separator, Object... array){
        if(array == null || array.length == 0){
            return "";
        }

        String result = "";
        if(StringUtil.isNull(separator)){
            separator = ",";
        }

        for(Object o : array){
            result += o.toString() + separator;
        }

        return trimEnd(result, separator);
    }
}
