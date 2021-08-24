package com.binarywaves.ghaneely.ghaneelycashing;


import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class MemoryCache {
    private static Map<String, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

    public static void clear() {
        cache.clear();
    }

    public Bitmap get(String id) {
        if (!cache.containsKey(id))
            return null;

        SoftReference<Bitmap> ref = cache.get(id);
        return ref.get();
    }

    public void put(String id, Bitmap bitmap) {
        cache.put(id, new SoftReference<>(bitmap));
    }
}