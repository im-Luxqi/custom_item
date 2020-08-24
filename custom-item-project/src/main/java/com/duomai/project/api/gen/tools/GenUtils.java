package com.duomai.project.api.gen.tools;

import com.duomai.project.api.gen.entity.CgGenTable;
import com.duomai.project.api.gen.entity.GenTableColumn;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 代码生成器 工具类
 */
public class GenUtils {

    /**
     * 初始化表信息
     */
    public static void initTable(CgGenTable table) {
        String tableName = table.getTableName();
        String packagePath = "com.duomai.project.product";
        String businessName = StringUtils.substring(tableName, tableName.lastIndexOf("_") + 1, tableName.length());
        String moduleName = StringUtils.substring(packagePath, packagePath.lastIndexOf(".") + 1, packagePath.length());

        table.setClassName(convertToCamelCase(tableName));
        table.setPackageName(packagePath);
        table.setModuleName(moduleName);
        table.setBusinessName(businessName);
        table.setFunctionName(table.getTableComment().replaceAll("(?:表)", ""));
    }

    /**
     * 初始化列属性字段
     */
    public static void initColumnField(GenTableColumn column, CgGenTable table) {
        String dataType = getDbType(column.getColumnType());
        String columnName = column.getColumnName();
        column.setTableId(table.getTableId());
        // 设置java字段名
        column.setJavaField(toCamelCase(columnName));
        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType)) {
            column.setJavaType(GenConstants.TYPE_STRING);
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.setJavaType(GenConstants.TYPE_DATE);
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            // 如果是浮点型
            String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
            if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
                column.setJavaType(GenConstants.TYPE_DOUBLE);
            }
            // 如果是整形
            else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
                column.setJavaType(GenConstants.TYPE_INTEGER);
            }
            // 长整形
            else {
                column.setJavaType(GenConstants.TYPE_LONG);
            }
        } else {
            column.setJavaType(CommonMap.javaTypeMap.get(dataType));
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    public static boolean arraysContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    public static String getDbType(String columnType) {
        if (StringUtils.indexOf(columnType, "(") > 0) {
            return StringUtils.substringBefore(columnType, "(");
        } else {
            return columnType;
        }
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }


    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}