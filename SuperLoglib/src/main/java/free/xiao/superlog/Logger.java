/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package free.xiao.superlog;

import android.text.TextUtils;

import free.xiao.superlog.formatter.message.object.ObjectFormatter;
import free.xiao.superlog.interceptor.Interceptor;
import free.xiao.superlog.internal.SystemCompat;
import free.xiao.superlog.internal.util.StackTraceUtil;
import free.xiao.superlog.printer.Printer;
import free.xiao.superlog.printer.PrinterSet;

import java.util.Arrays;


public class Logger {

    /**
     * The log configuration which you should respect to when logging.
     */
    private LogConfiguration logConfiguration;

    /**
     * The log printer used to print the logs.
     */
    private Printer printer;

    /**
     * Construct a logger.
     *
     * @param logConfiguration the log configuration which you should respect to when logging
     * @param printerSet          the log printer used to print the log
     */
    public Logger(LogConfiguration logConfiguration, PrinterSet printerSet) {
        this.logConfiguration = logConfiguration;
        this.printer = printerSet;
    }

    public Logger(LogConfiguration logConfiguration, Printer printer) {
        this.logConfiguration = logConfiguration;
        this.printer = printer;
    }

    /**
     * Log an object with specific log level.
     *
     * @param logLevel the specific log level
     * @param object   the object to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, Object object) {
        println(logLevel, tag, object);
    }

    /**
     * Log an array with specific log level.
     *
     * @param logLevel the specific log level
     * @param array    the array to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, Object[] array) {
        println(logLevel, tag, array);
    }

    /**
     * Log a message with specific log level.
     *
     * @param logLevel the specific log level
     * @param format   the format of the message to log, null if just need to concat arguments
     * @param args     the arguments of the message to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, String format, Object... args) {
        println(logLevel, tag, format, args);
    }

    /**
     * Log a message with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, String msg) {
        println(logLevel, tag, msg);
    }

    public void logFile(int fileName, String tag, String msg) {

    }

    /**
     * Log a message and a throwable with specific log level.
     *
     * @param logLevel the specific log level
     * @param msg      the message to log
     * @param tr       the throwable to be log
     * @since 1.4.0
     */
    public void log(int logLevel, String tag, String msg, Throwable tr) {
        println(logLevel, tag, msg, tr);
    }

    /**
     * Log a JSON string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param json the JSON string to log
     */
    public void json(String tag, String json) {
        if (LogLevel.DEBUG < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, logConfiguration.jsonFormatter.format(json));
    }

    /**
     * Log a XML string, with level {@link LogLevel#DEBUG} by default.
     *
     * @param xml the XML string to log
     */
    public void xml(String tag, String xml) {
        if (LogLevel.DEBUG < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(LogLevel.DEBUG, tag, logConfiguration.xmlFormatter.format(xml));
    }

    /**
     * Print an object in a new line.
     *
     * @param logLevel the log level of the printing object
     * @param object   the object to print
     */
    private <T> void println(int logLevel, String tag, T object) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        String objectString;
        if (object != null) {
            ObjectFormatter<? super T> objectFormatter = logConfiguration.getObjectFormatter(object);
            if (objectFormatter != null) {
                objectString = objectFormatter.format(object);
            } else {
                objectString = object.toString();
            }
        } else {
            objectString = "null";
        }
        printlnInternal(logLevel, tag, objectString);
    }

    /**
     * Print an array in a new line.
     *
     * @param logLevel the log level of the printing array
     * @param array    the array to print
     */
    private void println(int logLevel, String tag, Object[] array) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, Arrays.deepToString(array));
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param format   the format of the printing log, null if just need to concat arguments
     * @param args     the arguments of the printing log
     */
    private void println(int logLevel, String tag, String format, Object... args) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, formatArgs(format, args));
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    /*package*/ void println(int logLevel, String tag, String msg) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, msg);
    }

    /**
     * Print a log in a new line.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     * @param tr       a throwable object to log
     */
    private void println(int logLevel, String tag, String msg, Throwable tr) {
        if (logLevel < logConfiguration.logLevel) {
            return;
        }
        printlnInternal(logLevel, tag, ((msg == null || msg.length() == 0)
                ? "" : (msg + SystemCompat.lineSeparator))
                + logConfiguration.throwableFormatter.format(tr));
    }

    /**
     * Print a log in a new line internally.
     *
     * @param logLevel the log level of the printing log
     * @param msg      the message you would like to log
     */
    private void printlnInternal(int logLevel, String tag, String msg) {
        if (TextUtils.isEmpty(tag)) {
            tag = logConfiguration.tag;
        }
        String thread = logConfiguration.withThread
                ? logConfiguration.threadFormatter.format(Thread.currentThread())
                : null;
        String stackTrace = logConfiguration.withStackTrace
                ? logConfiguration.stackTraceFormatter.format(
                StackTraceUtil.getCroppedRealStackTrack(new Throwable().getStackTrace(),
                        logConfiguration.stackTraceOrigin,
                        logConfiguration.stackTraceDepth))
                : null;

        if (logConfiguration.interceptors != null) {
            LogItem log = new LogItem(logLevel, tag, thread, stackTrace, msg);
            for (Interceptor interceptor : logConfiguration.interceptors) {
                log = interceptor.intercept(log);
                if (log == null) {
                    // Log is eaten, don't print this log.
                    return;
                }

                // Check if the log still healthy.
                if (log.tag == null || log.msg == null) {
                    throw new IllegalStateException("Interceptor " + interceptor
                            + " should not remove the tag or message of a log,"
                            + " if you don't want to print this log,"
                            + " just return a null when intercept.");
                }
            }

            // Use fields after interception.
            logLevel = log.level;
            tag = log.tag;
            thread = log.threadInfo;
            stackTrace = log.stackTraceInfo;
            msg = log.msg;
        }

        printer.println(logLevel, tag, logConfiguration.withBorder
                ? logConfiguration.borderFormatter.format(new String[]{thread, stackTrace, msg})
                : ((thread != null ? (thread + SystemCompat.lineSeparator) : "")
                + (stackTrace != null ? (stackTrace + SystemCompat.lineSeparator) : "")
                + msg));
    }

    /**
     * Format a string with arguments.
     *
     * @param format the format string, null if just to concat the arguments
     * @param args   the arguments
     * @return the formatted string
     */
    private String formatArgs(String format, Object... args) {
        if (format != null) {
            return String.format(format, args);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0, N = args.length; i < N; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
            return sb.toString();
        }
    }

}
