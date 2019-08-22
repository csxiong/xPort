/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.csxiong.library.integration;

import android.app.Application;
import android.content.Context;
import android.support.v4.util.LruCache;

import javax.inject.Inject;

import dagger.Lazy;
import me.csxiong.library.utils.XPreconditions;
import retrofit2.Retrofit;

/**
 * @Desc : Api提供者
 * @Author : csxiong create on 2019/7/16
 */
public class ApiProvider {

    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Application mApplication;

    LruCache<String, Object> mCache;

    private final String CACHE_KEY_RETROFIT = "RETROFIT";

    @Inject
    public ApiProvider() {
        mCache = new LruCache<>(10);
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    public synchronized <T> T get(Class<T> service) {
        XPreconditions.checkNotNull(service, "retrofit service不允许为空");
        if (mCache.get(CACHE_KEY_RETROFIT + service.getCanonicalName()) == null) {
            mCache.put(CACHE_KEY_RETROFIT + service.getCanonicalName(), mRetrofit.get().create(service));
        }
        return (T) mCache.get(CACHE_KEY_RETROFIT + service.getCanonicalName());
    }

    public Context getContext() {
        return mApplication;
    }
}
