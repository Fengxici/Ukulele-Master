package timing.ukulele.persistence.model;

/**
 * @author fengxici
 */
public interface IdModel<T> {
    void setId(T id);

    T getId();
}
