package com.duomai.new_custom_base.framework.consolelog;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.text.DateFormat;
import java.util.Date;

/**
 * 利用rewrite把日志推送到页面
 *
 */
@Plugin(name = "ConsoleLogPolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class ConsoleLogPolicy implements RewritePolicy {

    @PluginFactory
    public static ConsoleLogPolicy factory() {
        return new ConsoleLogPolicy();
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        ConsoleLog loggerMessage = new ConsoleLog(
                source.getMessage().getFormattedMessage(),
                DateFormat.getDateTimeInstance().format(new Date(source.getTimeMillis())),
                source.getSource().getFileName(),
                source.getSource().getLineNumber(),
                source.getThreadName(),
                source.getLevel().name());
        ConsoleLogQueue.getInstance().push(loggerMessage);
        return source;
    }
}