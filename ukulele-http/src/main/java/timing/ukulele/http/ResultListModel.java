package timing.ukulele.http;

import java.util.List;

public class ResultListModel<T> {
    public List<T> data;
    public Integer httpCode;
    public String msg;
    public Long timestamp;
}