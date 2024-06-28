package com.joker.just4fun.g_utils

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.config.DataSourceConfig
import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.OutputFile
import com.baomidou.mybatisplus.generator.config.PackageConfig
import com.baomidou.mybatisplus.generator.config.StrategyConfig
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import lombok.RequiredArgsConstructor

import java.util.regex.Matcher
import java.util.regex.Pattern

@Deprecated
/**
 *
 * Mybatis代码自动生成
 *
 * @author Joker.Y
 * @since 2024/2/29
 * @version 1.0
 */
@RequiredArgsConstructor
class MybatisGenerator {

    //数据库参数
    private String dbIp
    private String dbName
    private String dbUrl
    private String username
    private String password

    //指定包名
    private String packageName
    //指定模块名
    private String moduleName
    //创建者
    private String author

    private static final Scanner SCANNER = new Scanner(System.in)

    static void main(String[] args) {
        def generator = new com.joker.just4fun.utils.MybatisGenerator()
        generator.generateCode()
    }

    void generateCode() {
        println "====================Ready to generate code...===================="
        this.author = scannerNext("请输入作者：")
        this.dbIp = scannerNext("请输入数据库IP：")
        this.dbName = scannerNext("请指定数据库：")
        this.dbUrl = "jdbc:mysql://${dbIp}:3306/${dbName}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useAffectedRows=true"
        this.username = scannerNext("请输入数据库用户名：")
        this.password = scannerNext("请输入数据库密码：")
        String input = ""
        input = verifyPath(scannerNext("请输入包名，基础包路径com.joker.just4fun无需键入："))
        this.packageName = "com.joker.just4fun" + input
        input = verifyPath(scannerNext("请输入模块名："))
        this.moduleName = input
        input = scannerNext("请输入数据表名，多个以半角逗号','分隔：")
        String[] tableNames = input.split(",")
        generateByTables(tableNames)
    }

    /**
     * 根据表自动生成
     * @param packageName 包名
     * @param tableNames 表名
     */
    private void generateByTables(String... tableNames) {
        //配置数据源
        DataSourceConfig dataSourceConfig = getDataSourceConfig(dbUrl, username, password)
        //策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames)
        //全局变量配置
        GlobalConfig globalConfig = getGlobalConfig()
        //包名配置
        PackageConfig packageConfig = getPackageConfig()
        //自动生成
        autoGenerator(dataSourceConfig, strategyConfig, globalConfig, packageConfig)
    }

    /**
     * 配置装载
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig 策略配置
     * @param globalConfig 全局配置
     * @param packageConfig 包路径配置
     */
    private static void autoGenerator(DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig,
                                      GlobalConfig globalConfig, PackageConfig packageConfig) {
        if (null == dataSourceConfig || null == strategyConfig
                || null == globalConfig || null == packageConfig){
            return
        }
        AutoGenerator generator = new AutoGenerator(dataSourceConfig)
        try {
            generator.strategy(strategyConfig)
                    .global(globalConfig)
                    .packageInfo(packageConfig)
                    .execute()
            println "====================Code generated successfully.===================="
        }catch (Exception e){
            printf("====================Failed to generated code!!====================", e)
        }
    }

    /**
     * 路径配置
     * @return
     */
    private PackageConfig getPackageConfig() {
        PackageConfig.Builder builder = new PackageConfig.Builder()
        PackageConfig config = builder.parent(packageName)//设置包路径
                .moduleName(moduleName.replace(".", ""))//设置模块路径
                .entity("entity${moduleName}")//设置表实体路径
                .xml("mapper${moduleName}")//设置mapper.xml文件路径
                .mapper("mapper${moduleName}")//设置mapper映射文件路径
                .service("service${moduleName}")//设置service层路径
                .serviceImpl("service.impl${moduleName}")//设置service实现路径
                .controller("controller${moduleName}")//设置controller路径
                .pathInfo(Collections.singletonMap(
                        OutputFile.xml, System.getProperty("user.dir")
                        + "/src/main/resources/mapper"
                        + moduleName.replace(".", "/")))//将xml文件生成在resources下
                .build()
        return config
    }

    /**
     * 全局配置
     * @return
     */
    private GlobalConfig getGlobalConfig() {
        GlobalConfig.Builder builder = new GlobalConfig.Builder()
        String outputDir = System.getProperty("user.dir") + "/src/main/groovy/"
        GlobalConfig config = builder.author(author)//设置作者
                .outputDir(outputDir)//设置输出路径
                .enableSwagger()//开启Swagger模式
                .disableOpenDir()//设置生成后不展开目录
                .build()
        return config
    }

    /**
     * 策略配置
     * @param tableNames
     * @return
     */
    private static StrategyConfig getStrategyConfig(String... tableNames) {
        StrategyConfig.Builder builder = new StrategyConfig.Builder()
        StrategyConfig config = builder.addInclude(tableNames).build()
        config = config
                .entityBuilder()//表实体策略设置
                .enableLombok()
//                .enableChainModel()
                .enableTableFieldAnnotation()
//                .enableActiveRecord()
                .idType(IdType.ASSIGN_UUID)
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .serviceBuilder()//service层策略设置
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .mapperBuilder()//mapper映射策略设置
//                .superClass(BaseMapper.class)
                .enableBaseResultMap()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .controllerBuilder()//控制层策略设置
                .formatFileName("%sController")
                .enableRestStyle()
                .build()
        return config
    }

    /**
     * 数据源配置
     * @param url
     * @param username
     * @param password
     * @return
     */
    private static DataSourceConfig getDataSourceConfig(String url, String username, String password) {
        return new DataSourceConfig.Builder(url, username, password).build()
    }

    /**
     * 控制台输入内容读取并打印提示信息
     * @param message
     * @return
     */
    static String scannerNext(String message) {
        println message
        String nextLine = SCANNER.nextLine()
        while (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return SCANNER.next()
        }
        return nextLine
    }

    /**
     * 校验路径
     * @param path
     * @return
     */
    static String verifyPath(String path){
        Pattern pattern = Pattern.compile("[a-zA-Z]")
        Matcher matcher = pattern.matcher(path)
        if (matcher.find()){
            path = path.startsWith(".") ? path : "." + path
        }else {
            path = ""
        }
        return path
    }

}
