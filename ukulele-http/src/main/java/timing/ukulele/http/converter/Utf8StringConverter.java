package timing.ukulele.http.converter;

import retrofit2.Converter;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 对含中文的字符串进行utf8编码
 */
public final class Utf8StringConverter<T> implements Converter<T, String> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    @Override
    public String convert(T value) throws IOException {
        String v = String.valueOf(value);
//        如果含中文则进行utf8编码
        if ((v.length() != v.getBytes().length))
            return new String(String.valueOf(value).getBytes(), UTF_8);
        return v;
    }
}