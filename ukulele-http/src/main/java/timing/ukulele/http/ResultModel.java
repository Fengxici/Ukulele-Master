package timing.ukulele.http;

public class ResultModel<T> {
    public T data;
    public Integer httpCode;
    public String msg;
    public Long timestamp;
}
