package com.example.shinchonton_backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(
        info = @Info(
                title = "신촌쿵야 API",
                description = "신촌쿵야 백엔드 API 명세",
                version = "v1"
        ),
        servers = @Server(url = "/", description = "현재 접속 서버")
)
@SecurityScheme(
        name = SwaggerConfig.MEMBER_ID_SCHEME,
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-Member-Id",
        description = "로그인/JWT 도입 전 사용하는 임시 회원 ID 헤더"
)
public class SwaggerConfig {

    public static final String MEMBER_ID_SCHEME = "memberId";

    public static final String[] PUBLIC_PATHS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/openapi.yaml"
    };

}
