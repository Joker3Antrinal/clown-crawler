package com.joker.just4fun.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringBootConfiguration;

/**
 *
 * Swagger文档配置
 *
 * @author Joker.Y
 * @version 1.0
 * @since 2024/3/13
 */
@SpringBootConfiguration
/*权限认证设置*/
@SecurityScheme(
        name = "Swagger-JWT",//认证方案名称（自定义）
        type = SecuritySchemeType.HTTP,//认证类型，当前为http认证
        description = "Swagger权限认证令牌",//描述信息
        in = SecuritySchemeIn.HEADER,//代表在http请求头部
        scheme = "bearer",//认证方案，如：Authorization: bearer token信息
        bearerFormat = "JWT"//表示使用 JWT 格式作为 Bearer Token 的格式
)
/*这里用注解的形式代替创建Bean的形式*/
@OpenAPIDefinition(
        /*API的描述信息、联系人信息、授权许可信息等*/
        info = @Info(
                title = "Swagger3.0(OpenAPI)接口文档",//Api接口文档标题（必填）
                description = "接口文档",//Api接口文档描述
                version = "1.0.1",//Api接口版本
                termsOfService = "http://doc.xiaominfo.com",//Api接口的服务条款地址
                contact = @Contact(
                        name = "Joker.Y",//文档发布者名称
                        email = "18584564865@163.com",//文档发布者邮箱
                        url = "https://github.com/Joker3Antrinal"//文档发布者网站
                ),
                license = @License(
                        name = "Apache 2.0",//授权名称
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"//授权信息URL
                )
        ),
        /*API接口文档服务地址*/
        servers = {
                @Server(url = "http://127.0.0.1:7021/clownCrawler/", description = "swagger server 1"),
//                @Server(url = "http://127.0.0.1:7022/clownCrawler/", description = "swagger server 2"),
        },
        security = @SecurityRequirement(name = "Swagger-JWT"),//引入定义好的权限认证方案
        externalDocs = @ExternalDocumentation(description = "访问此链接以获取更多", url = "https://swagger.io/")
)
public class SwaggerConfig {
/*
    @Bean
    public OpenAPI customOpenAPI() {

        /*contact:构建API的联系人信息，用于描述API开发者的联系信息，包括名称、邮箱、URL等* /
        Contact contact = new Contact()
                .name("Joker.Y")//文档发布者名称
                .email("18584564865@163.com")//文档发布者邮箱
                .url("https://github.com/Joker3Antrinal")//文档发布者网站
                .extensions(new HashMap<String, Object>());//使用Map配置信息（如key为"name","email","url"）

        /*license:用于描述API的授权许可信息，包括名称、URL等；假设当前的授权信息为Apache 2.0的开源标准* /
        License license = new License()
                .name("Apache 2.0")//授权名称
                .url("https://www.apache.org/licenses/LICENSE-2.0.html")//授权信息URL
                .identifier("Apache-2.0")//授权许可标识
                .extensions(new HashMap<String, Object>());//使用Map配置信息（如key为"name","url","identifier"）

        /*info:创建Api接口文档的描述信息、联系人信息(contact)、授权许可信息(license)* /
        Info info = new Info()
                .title("Swagger3.0(OpenAPI)示例文档")//Api接口文档标题（必填）
                .description("测试文档")//Api接口文档描述
                .version("1.0.1")//Api接口版本
                .termsOfService("https://example.com/")//Api接口的服务条款地址
                .contact(contact)//联系人信息
                .license(license);//授权许可信息
        /*返回信息* /
        return new OpenAPI()
                .openapi("3.0.1")//OpenAPI版本(默认)
                .info(info);//配置Swagger3.0描述信息
    }
*/
}
