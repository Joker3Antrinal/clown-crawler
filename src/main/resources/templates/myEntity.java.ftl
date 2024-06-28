package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#--若开启了lombok模式-->
<#if entityLombokModel>
import lombok.Data;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if swagger>
@Schema(name = "${entity}对象", title = "${entity}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass} <#if activeRecord> <${entity}> </#if> {

<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {

<#elseif entitySerialVersionUID>
public class ${entity} implements Serializable {

<#else>
public class ${entity} {

</#if>
<#--默认是开启了序列化模式，但仍然建议通过IDE或工具计算出一个准确的serialVersionUID-->
<#--<#if entitySerialVersionUID>-->
<#--    private static final long serialVersionUID = 1L;-->
<#--</#if>-->
<#----------  遍历字段并设置 BEGIN  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#if field.comment!?length gt 0>
        <#if swagger>
<#--    @ApiModelProperty("${field.comment}")-->
    @Schema(description = "${field.comment!}")
        <#else>
    /**
    * ${field.comment!}
    */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
        <#elseif idType??>
    @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.annotationColumnName}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- 字段填充设置 -->
        <#if field.convert>
    @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("${field.annotationColumnName}")
    </#if>
<#--    &lt;#&ndash; 乐观锁注解 &ndash;&gt;-->
<#--    <#if field.versionField>-->
<#--    @Version-->
<#--    </#if>-->
<#--    &lt;#&ndash; 逻辑删除注解 &ndash;&gt;-->
<#--    <#if field.logicDeleteField>-->
<#--    @TableLogic-->
<#--    </#if>-->
<#--    &lt;#&ndash; 日期类型格式化处理 &ndash;&gt;-->
<#--    <#if field.propertyType == "Date">-->
<#--    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")-->
<#--    </#if>-->
    private ${field.propertyType} ${field.propertyName};

</#list>
<#----------  遍历字段并设置 END  ---------->

<#--若未开启lombok模式-->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }
        <#if chainModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {

        <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {

        </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if chainModel>
        return this;
        </#if>
    }
    </#list>
</#if>
<#--常量字段-->
<#if entityColumnConstant>
    <#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";
    </#list>
</#if>
<#if activeRecord>
    @Override
    public Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }
</#if>
<#if !entityLombokModel>
    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index == 0> "${field.propertyName}=" + ${field.propertyName} +
        <#else> ", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
        "}";
    }
</#if>
}