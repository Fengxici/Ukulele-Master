package timing.ukulele.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import timing.ukulele.web.deserializer.LocalDateDeserializer;
import timing.ukulele.web.deserializer.LocalDateTimeDeserializer;
import timing.ukulele.web.deserializer.LocalTimeDeserializer;
import timing.ukulele.web.serializer.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author fengxici
 */
@Configuration
public class WebDataConvertConfig implements WebMvcConfigurer {

    @Value("${ukulele.web.ignore-null-field}")
    private Boolean ignoreNullField;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        converters.add(jackson2HttpMessageConverter());
    }

    /**
     * 时间格式转换器,将Date类型统一转换为yyyy-MM-dd HH:mm:ss格式的字符串
     * 长整型转换成String
     * 忽略value为null时key的输出
     * 注释部分和未注释部分代码相当，有待研究二者区别
     */
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = serializingObjectMapper();
        // 序列化枚举值
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        //忽略value为null时key的输出
        if (ignoreNullField != null && ignoreNullField)
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化成json时，将所有的Long变成string，以解决js中的精度丢失。
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    /**
     * jackson2 json序列化 null字段输出为空串
     */
    private ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //序列化日期格式
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, simpleDateFormat));
        //反序列化日期格式
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        javaTimeModule.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String date = jsonParser.getText();
                try {
                    return simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        objectMapper.registerModule(javaTimeModule);

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

        return objectMapper;
    }


//    /**
//     * LocalDateTime序列化
//     */
//    private class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
//
//        @Override
//        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(value.format(DATETIME_FORMATTER));
//        }
//    }
//
//    /**
//     * LocalDateTime反序列化
//     */
//    private class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
//
//        @Override
//        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//            return LocalDateTime.parse(p.getValueAsString(), DATETIME_FORMATTER);
//        }
//    }
//
//    /**
//     * LocalDate序列化
//     */
//    private class LocalDateSerializer extends JsonSerializer<LocalDate> {
//
//        @Override
//        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(value.format(DATE_FORMATTER));
//        }
//    }
//
//    /**
//     * LocalDate反序列化
//     */
//    private class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
//
//        @Override
//        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//            return LocalDate.parse(p.getValueAsString(), DATE_FORMATTER);
//        }
//    }
//
//    /**
//     * LocalTime序列化
//     */
//    private class LocalTimeSerializer extends JsonSerializer<LocalTime> {
//
//        @Override
//        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(value.format(TIME_FORMATTER));
//        }
//    }
//
//    /**
//     * LocalTime反序列化
//     */
//    private class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
//
//        @Override
//        public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//            return LocalTime.parse(p.getValueAsString(), TIME_FORMATTER);
//        }
//    }
}
