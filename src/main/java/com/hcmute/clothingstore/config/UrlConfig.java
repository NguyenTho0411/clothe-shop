package com.hcmute.clothingstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlConfig {

    public static String API_VERSION;

    @Value("${api.version}")
    public void setApiVersion(String apiVersion) { API_VERSION = "/" + apiVersion; }

    public static final String ROOT = "/";
    public static final String API_DOCS = "/v3/api-docs/**";
    public static final String SWAGGER_UI = "/swagger-ui/**";
    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    public static final String WS = "/ws/**";

    public static final String AUTH = "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    // … (các hằng mục khác nếu cần dùng)

    public static String[] PUBLIC_ENDPOINTS() {
        return new String[] { ROOT, API_DOCS, SWAGGER_UI, SWAGGER_UI_HTML };
    }

    public static String[] PUBLIC_WS_ENDPOINTS() { return new String[] { WS }; }

    public static String[] PUBLIC_GET_ENDPOINTS() {
        return new String[] {
                // thêm GET public nếu có
        };
    }

    public static String[] PUBLIC_POST_ENDPOINTS() {
        return new String[] {
                API_VERSION + AUTH + LOGIN,
                API_VERSION + AUTH + REGISTER
        };
    }

    public static String[] PUBLIC_PUT_ENDPOINTS() { return new String[] { }; }
}
