package com.diro.ift2255.service;

import java.util.List;

public interface IService<T> {

    List<T> getAll();

    T getById(String id);  

    void create(T entity);

    void update(String id, T entity);

    void delete(String id);
}