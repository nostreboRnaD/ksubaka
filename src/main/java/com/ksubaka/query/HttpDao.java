package com.ksubaka.query;

import java.util.List;

public interface HttpDao<T extends Media> {

    public List<T> get(String title);
}
