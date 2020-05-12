package timing.ukulele.common.util;

import timing.ukulele.common.DataType;
import timing.ukulele.common.exception.DataParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 类型转换辅助工具类
 */
public final class TypeParseUtil {
    private TypeParseUtil() {
    }

    /**
     * 转换核心实现方法
     *
     * @param value  value
     * @param type   目标数据类型
     * @param format 输入/输出字符串格式
     * @return Object
     */
    public static Object convert(Object value, Class<?> type, String format) {
        return convert(value, type.getName(), format);
    }

    private static final String MESSAGE = "Could not convert %1$s to %2$s";
    private static final String SUPPORT = "Convert from %1$s to %2$s not currently supported";

    /**
     * 转换核心实现方法
     *
     * @param value  value
     * @param type   目标数据类型
     * @param format 输入/输出字符串格式
     * @return Object
     */
    private static Object convert(Object value, String type, String format) {
        Locale locale = Locale.getDefault();
        if (value == null) {
            return null;
        } else if (value.getClass().getName().equalsIgnoreCase(type)
                || value.getClass().getSimpleName().equalsIgnoreCase(type)) {
            return value;
        } else if ("Object".equalsIgnoreCase(type) || "java.lang.Object".equals(type)) {
            return value;
        } else if (value instanceof String) {
            return string2Object(value, type, format, locale);
        } else if (value instanceof BigDecimal) {
            return decimal2Obj(value, type, locale);
        } else if (value instanceof Double) {
            return double2Obj(value, type, locale);
        } else if (value instanceof Float) {
            return float2Obj(value, type, locale);
        } else if (value instanceof Long) {
            return long2Obj(value, type, locale);
        } else if (value instanceof Integer) {
            return integer2Obj(value, type, locale);
        } else if (value instanceof java.util.Date) {
            return date2Obj(value, type, format);
        } else if (value instanceof Date) {
            return sqlDate2Obj(value, type, format);
        } else if (value instanceof Timestamp) {
            return time2Obj(value, type, format);
        } else if (value instanceof Boolean) {
            return bool2Obj(value, type);
        } else if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return value.toString();
        } else {
            throw new DataParseException(String.format(SUPPORT, value.getClass().getName(), type));
        }
    }

    // 布尔类型
    private static Object bool2Obj(Object value, String type) {
        String fromType = "Boolean";
        Boolean bol = (Boolean) value;
        if ("Boolean".equalsIgnoreCase(type) || DataType.BOOLEAN.equalsIgnoreCase(type)) {
            return bol;
        } else if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return bol.toString();
        } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            if (bol) {
                return Integer.parseInt("1");
            } else {
                return Integer.parseInt("0");
            }
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 时间类型
    private static Object time2Obj(Object value, String type, String format) {
        String fromType = "Timestamp";
        Timestamp time = (Timestamp) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            if (format == null || format.length() == 0) {
                return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(time);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(new java.util.Date(time.getTime()));
            }
        } else if ("Date".equalsIgnoreCase(type) || DataType.DATE.equalsIgnoreCase(type)) {
            return new java.util.Date(time.getTime());
        } else if ("java.sql.Date".equalsIgnoreCase(type)) {
            return new Date(time.getTime());
        } else if ("Time".equalsIgnoreCase(type) || DataType.TIME.equalsIgnoreCase(type)) {
            return new Time(time.getTime());
        } else if ("Timestamp".equalsIgnoreCase(type) || DataType.TIMESTAMP.equalsIgnoreCase(type)) {
            return value;
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 日期类型
    private static Object sqlDate2Obj(Object value, String type, String format) {
        String fromType = "Date";
        Date dte = (Date) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            if (format == null || format.length() == 0) {
                return dte.toString();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(new java.util.Date(dte.getTime()));
            }
        } else if ("Date".equalsIgnoreCase(type) || DataType.DATE.equalsIgnoreCase(type)) {
            return new java.util.Date(dte.getTime());
        } else if ("java.sql.Date".equalsIgnoreCase(type)) {
            return value;
        } else if ("Time".equalsIgnoreCase(type) || DataType.TIME.equalsIgnoreCase(type)) {
            throw new DataParseException("Conversion from " + fromType + " to " + type + " not currently supported");
        } else if ("Timestamp".equalsIgnoreCase(type) || DataType.TIMESTAMP.equalsIgnoreCase(type)) {
            return new Timestamp(dte.getTime());
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 时间类型
    private static Object date2Obj(Object value, String type, String format) {
        String fromType = "Date";
        java.util.Date dte = (java.util.Date) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            if (format == null || format.length() == 0) {
                return dte.toString();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(dte);
            }
        } else if ("Date".equalsIgnoreCase(type) || DataType.DATE.equalsIgnoreCase(type)) {
            return value;
        } else if ("java.sql.Date".equalsIgnoreCase(type)) {
            return new Date(dte.getTime());
        } else if ("Time".equalsIgnoreCase(type) || DataType.TIME.equalsIgnoreCase(type)) {
            return new Time(dte.getTime());
        } else if ("Timestamp".equalsIgnoreCase(type) || DataType.TIMESTAMP.equalsIgnoreCase(type)) {
            return new Timestamp(dte.getTime());
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 整型
    private static Object integer2Obj(Object value, String type, Locale locale) {
        String fromType = "Integer";
        Integer integer = (Integer) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return getNf(locale).format(integer.toString());
        } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
            return Double.parseDouble(integer.toString());
        } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
            return Float.parseFloat(integer.toString());
        } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
            String str = integer.toString();
            BigDecimal retBig = new BigDecimal(integer.toString());
            int iscale = str.indexOf(".");
            int keylen = str.length();
            if (iscale > -1) {
                iscale = keylen - (iscale + 1);
                return retBig.setScale(iscale, RoundingMode.HALF_DOWN);
            } else {
                return retBig.setScale(0, RoundingMode.HALF_DOWN);
            }
        } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
            return Long.parseLong(integer.toString());
        } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            return value;
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 长整型
    private static Object long2Obj(Object value, String type, Locale locale) {
        String fromType = "Long";
        Long lng = (Long) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return getNf(locale).format(lng.toString());
        } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
            return Double.parseDouble(lng.toString());
        } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
            return Float.parseFloat(lng.toString());
        } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
            return new BigDecimal(lng.toString());
        } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
            return value;
        } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            return Integer.parseInt(lng.toString());
        } else if ("Date".equalsIgnoreCase(type) || DataType.DATE.equalsIgnoreCase(type)) {
            return new java.util.Date(lng);
        } else if ("java.sql.Date".equalsIgnoreCase(type)) {
            return new Date(lng);
        } else if ("Time".equalsIgnoreCase(type) || DataType.TIME.equalsIgnoreCase(type)) {
            return new Time(lng);
        } else if ("Timestamp".equalsIgnoreCase(type) || DataType.TIMESTAMP.equalsIgnoreCase(type)) {
            return new Timestamp(lng);
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    private static Object float2Obj(Object value, String type, Locale locale) {
        String fromType = "Float";
        Float flt = (Float) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return getNf(locale).format(flt.toString());
        } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
            return new BigDecimal(flt.toString());
        } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
            return Double.parseDouble(flt.toString());
        } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
            return value;
        } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
            return Long.parseLong(flt.toString());
        } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            return Integer.parseInt(flt.toString());
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 双精度浮点型
    private static Object double2Obj(Object value, String type, Locale locale) {
        String fromType = "Double";
        Double dbl = (Double) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return getNf(locale).format(dbl.toString());
        } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
            return value;
        } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
            return Float.parseFloat(dbl.toString());
        } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
            return Long.parseLong(dbl.toString());
        } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            return Integer.parseInt(dbl.toString());
        } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
            return new BigDecimal(dbl.toString());
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 数字型
    private static Object decimal2Obj(Object value, String type, Locale locale) {
        String fromType = "BigDecimal";
        BigDecimal bigD = (BigDecimal) value;
        if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
            return getNf(locale).format(bigD.toString());
        } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
            return value;
        } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
            return Double.parseDouble(bigD.toString());
        } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
            return Float.parseFloat(bigD.toString());
        } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
            return Long.parseLong(bigD.toString());
        } else if ("Integer".equals(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
            return Integer.parseInt(bigD.toString());
        } else {
            throw new DataParseException(String.format(SUPPORT, fromType, type));
        }
    }

    // 字符型
    private static Object string2Object(Object value, String type, String format, Locale locale) {
        String fromType = "String";
        String str = (String) value;
        try {
            if ("String".equalsIgnoreCase(type) || DataType.STRING.equalsIgnoreCase(type)) {
                return value;
            } else if (str.length() == 0) {
                return null;
            } else if ("Boolean".equalsIgnoreCase(type) || DataType.BOOLEAN.equals(type)) {
                if ("TRUE".equalsIgnoreCase(str)) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } else if ("Double".equalsIgnoreCase(type) || DataType.DOUBLE.equalsIgnoreCase(type)) {
                Number tempNum = getNf(locale).parse(str.replaceAll(",", ""));
                return Double.parseDouble(tempNum.toString());
            } else if ("BigDecimal".equalsIgnoreCase(type) || DataType.BIGDECIMAL.equalsIgnoreCase(type)) {
                BigDecimal retBig = new BigDecimal(str.replaceAll(",", ""));
                int iscale = str.indexOf(".");
                int keylen = str.length();
                if (iscale > -1) {
                    iscale = keylen - (iscale + 1);
                    return retBig.setScale(iscale, RoundingMode.HALF_DOWN);
                } else {
                    return retBig.setScale(0, RoundingMode.HALF_DOWN);
                }
            } else if ("Float".equalsIgnoreCase(type) || DataType.FLOAT.equalsIgnoreCase(type)) {
                Number tempNum = getNf(locale).parse(str.replaceAll(",", ""));
                return Float.parseFloat(tempNum.toString());
            } else if ("Long".equalsIgnoreCase(type) || DataType.LONG.equalsIgnoreCase(type)) {
                NumberFormat nf = getNf(locale);
                nf.setMaximumFractionDigits(0);
                Number tempNum = nf.parse(str.replaceAll(",", ""));
                return Long.parseLong(tempNum.toString());
            } else if ("Integer".equalsIgnoreCase(type) || DataType.INTEGER.equalsIgnoreCase(type)) {
                NumberFormat nf = getNf(locale);
                nf.setMaximumFractionDigits(0);
                Number tempNum = nf.parse(str.replaceAll(",", ""));
                return Integer.parseInt(tempNum.toString());
            } else if ("Date".equalsIgnoreCase(type) || DataType.DATE.equalsIgnoreCase(type)) {
                if (format == null || format.length() == 0) {
                    String separator = String.valueOf(str.charAt(4));
                    StringBuilder pattern;
                    if (separator.matches("\\d*")) {
                        pattern = new StringBuilder("yyyyMMdd HH:mm:ss");
                        format = pattern.substring(0, str.length());
                    } else {
                        pattern = new StringBuilder("yyyy").append(separator).append("MM").append(separator)
                                .append("dd HH:mm:ss");
                        format = pattern.substring(0, str.length());
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(str);
            } else if ("java.sql.Date".equalsIgnoreCase(type)) {
                if (format == null || format.length() == 0) {
                    try {
                        return Date.valueOf(str);
                    } catch (Exception e) {
                        try {
                            DateFormat df ;
                            if (locale != null) {
                                df = DateFormat.getDateInstance(3, locale);
                            } else {
                                df = DateFormat.getDateInstance(3);
                            }
                            java.util.Date fieldDate = df.parse(str);
                            return new Date(fieldDate.getTime());
                        } catch (ParseException e1) {
                            throw new DataParseException(String.format(MESSAGE, str, type), e);
                        }
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                java.util.Date fieldDate = sdf.parse(str);
                return new Date(fieldDate.getTime());
            } else if ("Timestamp".equalsIgnoreCase(type) || DataType.TIMESTAMP.equalsIgnoreCase(type)) {
                if (str.length() == 10) {
                    str = str + " 00:00:00";
                }
                if (format == null || format.length() == 0) {
                    try {
                        return Timestamp.valueOf(str);
                    } catch (Exception e) {
                        try {
                            DateFormat df;
                            if (locale != null) {
                                df = DateFormat.getDateTimeInstance(3, 3, locale);
                            } else {
                                df = DateFormat.getDateTimeInstance(3, 3);
                            }
                            java.util.Date fieldDate = df.parse(str);
                            return new Timestamp(fieldDate.getTime());
                        } catch (ParseException e1) {
                            throw new DataParseException(String.format(MESSAGE, str, type), e);
                        }
                    }
                }
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    java.util.Date fieldDate = sdf.parse(str);
                    return new Timestamp(fieldDate.getTime());
                } catch (ParseException e) {
                    throw new DataParseException(String.format(MESSAGE, str, type), e);
                }
            } else {
                throw new DataParseException(String.format(SUPPORT, fromType, type));
            }
        } catch (Exception e) {
            throw new DataParseException(String.format(MESSAGE, str, type), e);
        }
    }

    private static NumberFormat getNf(Locale locale) {
        NumberFormat nf ;
        if (locale == null) {
            nf = NumberFormat.getNumberInstance();
        } else {
            nf = NumberFormat.getNumberInstance(locale);
        }
        nf.setGroupingUsed(false);
        return nf;
    }

    /**
     * 转换为布尔值
     *
     * @param obj obj
     * @return boolean
     */
    public static Boolean convertToBoolean(Object obj) {
        return (Boolean) convert(obj, "Boolean", null);
    }

    /**
     * 转换为整型
     *
     * @param obj obj
     * @return integer
     */
    public static Integer convertToInteger(Object obj) {
        return (Integer) convert(obj, "Integer", null);
    }

    /**
     * 转换为字符串
     *
     * @param obj obj
     * @return string
     */
    public static String convertToString(Object obj) {
        return (String) convert(obj, "String", null);
    }

    /**
     * 转换为字符串
     *
     * @param obj          obj
     * @param defaultValue defaultValue
     * @return string
     */
    public static String convertToString(Object obj, String defaultValue) {
        Object s = convert(obj, "String", null);
        if (s != null) {
            return (String) s;
        } else {
            return "";
        }
    }

    /**
     * 转换为长整型
     *
     * @param obj obj
     * @return Long
     */
    public static Long convertToLong(Object obj) {
        return (Long) convert(obj, "Long", null);
    }

    /**
     * 转换为双精度型
     *
     * @param obj obj
     * @return bouble
     */
    public static Double convertToDouble(Object obj) {
        return (Double) convert(obj, "Double", null);
    }

    /**
     * 转换为浮点型
     *
     * @param obj obj
     * @return double
     */
    public static Double convertToFloat(Object obj) {
        return (Double) convert(obj, "Float", null);
    }

    /**
     * 转换为数值型
     *
     * @param obj   obj
     * @param scale scale
     * @return bigdecimal
     */
    public static BigDecimal convertToBigDecimal(Object obj, int scale) {
        return ((BigDecimal) convert(obj, "BigDecimal", null)).setScale(scale, RoundingMode.HALF_DOWN);
    }

    /**
     * 转换为日期型
     *
     * @param obj    obj
     * @param format format
     * @return date
     */
    public static java.util.Date convertToDate(Object obj, String format) {
        return (java.util.Date) convert(obj, "Date", format);
    }

    /**
     * 转换为日期型
     *
     * @param obj    obj
     * @param format format
     * @return date
     */
    public static Date convertToSqlDate(Object obj, String format) {
        return (Date) convert(obj, "java.sql.Date", format);
    }

    /**
     * 转换为日期型
     *
     * @param obj    obj
     * @param format format
     * @return timestamp
     */
    public static Timestamp convertToTimestamp(Object obj, String format) {
        return (Timestamp) convert(obj, "Timestamp", format);
    }
}

