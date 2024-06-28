package com.joker.just4fun.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Mybatis代码自动生成
 *
 * @author Joker.Y
 * @since 2024/2/29
 * @version 1.0
 */
public class MybatisGenerator {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================Ready to generate code...====================");
        String dbIp = scannerNext("请输入数据库IP：");
        String dbName = scannerNext("请指定数据库：");
        String dbUrl = "jdbc:mysql://" + dbIp + ":3306/" + dbName + "?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useAffectedRows=true";
        String username = scannerNext("请输入数据库用户名：");
        String password = scannerNext("请输入数据库密码：");
        try {
            FastAutoGenerator.create(dbUrl, username, password)
                    .globalConfig((scanner, builder) -> {
                        builder.author(scanner.apply("请输入作者："))//设置作者
                                .outputDir(System.getProperty("user.dir") + "/src/main/java/")//设置输出路径
                                .enableSwagger()//开启Swagger模式
                                .disableOpenDir();//设置生成后不展开目录
                    })
                    .packageConfig((scanner, builder) -> {
                        String packageName = verifyPath(scanner.apply("请输入包名，基础包路径com.joker.just4fun无需键入："));
                        String moduleName = verifyPath(scanner.apply("请输入模块名："));
                        builder.parent("com.joker.just4fun" + packageName)//设置包路径
                                .moduleName(moduleName.replaceFirst("\\.", ""))//设置模块路径
                                .entity("entity" + moduleName)//设置表实体路径
                                .xml("mapper" + moduleName)//设置mapper.xml文件路径
                                .mapper("mapper" + moduleName)//设置mapper映射文件路径
                                .service("service" + moduleName)//设置service层路径
                                .serviceImpl("service.impl" + moduleName)//设置service实现路径
                                .controller("controller" + moduleName)//设置controller路径
                                .pathInfo(Collections.singletonMap(
                                        OutputFile.xml, System.getProperty("user.dir")
                                                + "/src/main/resources/mapper"
                                                + moduleName.replace(".", "/")));//将xml文件生成在resources下
                    })
                    .strategyConfig((scanner, builder) -> {
                        builder.addInclude(scanner.apply("请输入数据表名，多个以半角逗号','分隔：").split(","))
                                .entityBuilder()//表实体策略设置
                                .enableLombok()
//                            .enableChainModel()
                                .enableTableFieldAnnotation()
//                            .enableActiveRecord()
                                .idType(IdType.ASSIGN_UUID)
                                .naming(NamingStrategy.underline_to_camel)
                                .columnNaming(NamingStrategy.underline_to_camel)
                                .serviceBuilder()//service层策略设置
                                .formatServiceFileName("%sService")
                                .formatServiceImplFileName("%sServiceImpl")
                                .mapperBuilder()//mapper映射策略设置
//                            .superClass(BaseMapper.class)
                                .enableBaseResultMap()
                                .formatMapperFileName("%sMapper")
                                .formatXmlFileName("%sMapper")
                                .controllerBuilder()//控制层策略设置
                                .formatFileName("%sController")
                                .enableRestStyle();
                    })
                    //使用自定义模版修改生成文件默认内容
                    .templateEngine(new FreemarkerTemplateEngine())
                    .templateConfig(builder -> builder.entity("templates/myEntity.java"))
                    .execute()
            ;
        }catch (Exception e){
            System.out.println("====================Failed to generated code: " + e.getMessage() + "====================");
        }
        System.out.println("====================Code generated successfully.====================");
    }

    /**
     * 控制台输入内容读取并打印提示信息
     * @param message
     * @return
     */
    public static String scannerNext(String message) {
        System.out.println(message);
        String nextLine = SCANNER.nextLine();
        if (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return SCANNER.next();
        }
        return nextLine;
    }

    /**
     * 校验路径
     * @param path
     * @return
     */
    public static String verifyPath(String path){
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()){
            path = path.startsWith(".") ? path : "." + path;
        }else {
            path = "";
        }
        return path;
    }
}
