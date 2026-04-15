package storage;

import java.util.List;

public interface Storage<T> {
    List<T> load() throws Exception;
    void save(List<T> items) throws Exception;
}