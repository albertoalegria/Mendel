package com.albertoalegria.mendel.services;

import java.util.List;

/**
 * @author Alberto Alegria
 */
public interface ModelService<T> {
    List<T> getAll();
    List<T> create(T t);
    List<T> delete(long id);
    T getById(long id);
}
