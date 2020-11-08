package com.duomai.project.api.taobao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @description
 * @create by 王星齐
 * @date 2020-10-09 16:43
 */
@Accessors(chain = true)
@Data
public class MemCacheData<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    T data;
    long timeout;

    public MemCacheData(long cacheTime) {
        this.timeout = new Date().getTime() + cacheTime * 1000;
    }

    public MemCacheData() {
    }
}
