package com.ashwinchat.jobfinder.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogFormatter extends Formatter {
    private static LogFormatter instance = new LogFormatter();

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(record.getMillis()))).append(" - ");
        builder.append(record.getLevel()).append(" - ");
        builder.append(record.getSourceClassName()).append(".");
        builder.append(record.getSourceMethodName()).append(" - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public static void applyFormat(Logger logger) {
        logger.setUseParentHandlers(false);
        LogFormatter formatter = LogFormatter.getInstance();
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
    }

    public static LogFormatter getInstance() {
        return instance;
    }

}
