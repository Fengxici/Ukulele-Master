package timing.ukulele.common.util.excel;

import timing.ukulele.common.util.DataUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * excel写操作
 */
public class ExcelWriter {
    /**
     * 目标输出流
     */
    private OutputStream stream;
    /**
     * 表头
     */
    private Map<String, String> fields;

    /**
     * 数据源model所有字段map
     */
    private static Map<String, Field> fieldMap = new HashMap<>();

    public ExcelWriter(HttpServletResponse response, LinkedHashMap<String, String> fields, String fileName, Class<?> clz) throws IOException {
        if (response == null || fields == null || fileName == null || clz == null)
            throw new IllegalArgumentException();
        getFieldMap(clz, fieldMap);
        this.fields = fields;
        response.setContentType("application/octet-stream;charset=GBK");
        response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "iso8859-1"));
        response.setCharacterEncoding("GBK");

        this.stream = response.getOutputStream();
        //写表头，生成指定名字的文件，返回客户端
        StringBuilder hb = new StringBuilder();
        for (Map.Entry<String, String> e : fields.entrySet()) {
            hb.append(e.getValue() + ",");
        }
        stream.write(hb.substring(0, hb.length() - 1).getBytes("GBK"));
        stream.flush();
    }

    /**
     * 往表格中插入记录
     */
    public <T> void write(T report) throws IllegalArgumentException, IOException, IllegalAccessException {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        for (String field : fields.keySet()) {
            Field f = fieldMap.get(field);
            f.setAccessible(true);
            Object value = f.get(report);
            if (value == null || DataUtil.isEmpty(value)) {
//                sb.append("\t");
                sb.append(",");
            } else if (f.getType() == Date.class) {
//                sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "\t");
                sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + ",");
            } else {
                sb.append(value.toString() + ",");
            }
        }
        if (DataUtil.isNotEmpty(sb)) {
            stream.write(sb.substring(0, sb.length() - 1).getBytes("GBK"));
            stream.flush();
        }
    }

    public void close() throws IOException {
        stream.close();
    }

    private static <T extends Object> void getFieldMap(Class<T> clz, Map<String, Field> result) {
        for (Field field : clz.getDeclaredFields()) {
            result.put(field.getName(), field);
        }
        if (clz.getSuperclass() != null) {
            getFieldMap(clz.getSuperclass(), result);
        }
    }
}
