/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.duomai.new_custom_base.framework.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * P6spy SQL 日志格式化
 */
public class P6spyLogFormat implements MessageFormattingStrategy {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
        if ("".equals(sql.trim()))
            return "";
        return this.format.format(new Date()) + " | SQL耗时 " + elapsed + "ms  | 连接信息 " + connectionId + "\t\n" +
                sql
//                        .replaceAll(" values ", "\r\nvalues\r\n\t")
//                        .replaceAll(" VALUES ", "\r\nVALUES\r\n\t")
//                        .replaceAll(" from ", "\r\nfrom\r\n\t")
//                        .replaceAll(" FROM ", "\r\nFROM\r\n\t")
//                        .replaceAll(" where ", "\rwhere\r\n\t")
//                        .replaceAll(" WHERE ", "\rWHERE\r\n\t")
//                        .replaceAll(" order by ", "\rorder by\r\n\t")
//                        .replaceAll(" ORDER BY ", "\rORDER BY\r\n\t")
//                        .replaceAll(" group by ", "\rgroup by\r\n\t")
//                        .replaceAll(" GROUP BY ", "\rGROUP BY\r\n\t")+";"
                ;
    }


}
