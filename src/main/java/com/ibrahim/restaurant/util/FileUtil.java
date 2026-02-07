package com.ibrahim.restaurant.util;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

public class FileUtil {
    public static MediaType getMediaType(Resource resource) {
        return MediaTypeFactory.getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
    }
}
