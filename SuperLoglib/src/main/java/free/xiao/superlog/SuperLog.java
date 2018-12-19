/*
 * Copyright 2015 Elvis Hew
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

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import free.xiao.superlog.flattener.ClassicFlattener;
import free.xiao.superlog.internal.util.GsonUtils;
import free.xiao.superlog.printer.AndroidPrinter;
import free.xiao.superlog.printer.Printer;
import free.xiao.superlog.printer.file.FilePrinter;
import free.xiao.superlog.printer.file.naming.DateFileNameGenerator;

import java.io.File;

public class SuperLog {
    private static final String TAG = "SuperLog";//默然tag
    private static Logger androidLogger;
    private static Logger fileLogger;
    private static boolean isLogEnable = true;
    //log过期天数
    private static final int LOG_RETENTION_PERIOD = 7;

    static {
        if (isLogEnable) {
            LogConfiguration androidLoggerConfig = new LogConfiguration.Builder()
                    .tag(TAG)                   // Specify TAG
                    .t()                                                // Enable thread info, disabled by default
                    .st(3)                                              // Enable stack trace info with depth 2, disabled by default
                    .b()                                                // Enable border, disabled by default
                    // .jsonFormatter(new MyJsonFormatter())               // Default: DefaultJsonFormatter
                    // .xmlFormatter(new MyXmlFormatter())                 // Default: DefaultXmlFormatter
                    // .throwableFormatter(new MyThrowableFormatter())     // Default: DefaultThrowableFormatter
                    // .threadFormatter(new MyThreadFormatter())           // Default: DefaultThreadFormatter
                    // .stackTraceFormatter(new MyStackTraceFormatter())   // Default: DefaultStackTraceFormatter
                    // .borderFormatter(new MyBoardFormatter())            // Default: DefaultBorderFormatter
                    // .addObjectFormatter(AnyClass.class,                 // Add formatter for specific class of object
                    //     new AnyClassObjectFormatter())                  // Use Object.toString() by default
                    // .addInterceptor(new WhitelistTagsFilterInterceptor( // Add whitelist tags filter
                    //     "whitelist1", "whitelist2", "whitelist3"))
                    // .addInterceptor(new MyInterceptor())                // Add a log interceptor
                    .build();

            androidLogger = new Logger(androidLoggerConfig, new AndroidPrinter());


            LogConfiguration fileLoggerConfig = new LogConfiguration.Builder()
                    .tag(TAG).build();
            Printer filePrinter = new FilePrinter                      // Printer that print the log to the file system
                    .Builder(new File(Environment.getExternalStorageDirectory(), "CRGTXlog").getPath())       // Specify the path to save log file
                    .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log")
                    //.backupStrategy(new MyBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024)
                    .flattener(new ClassicFlattener())                  // Default: DefaultFlattener
                    .setFileLogRetentionPeriod(LOG_RETENTION_PERIOD)
                    .build();
            fileLogger = new Logger(fileLoggerConfig, filePrinter);
        }
    }

    ////////////////////////////控制输出log，支持简单字符串、object、json格式等输出//////////////////////////////////////

    /**
     * 格式化输出JavaBean
     *
     * @param object
     */
    public static void d(Object object) {
        if (isLogEnable) {
            if (object == null) {
                throw new RuntimeException("object can't null");
            }
            String json = GsonUtils.toJson(object);
            json(json);
        }
    }

    public static void d(String tag, Object object) {
        if (isLogEnable) {
            if (object == null) {
                throw new RuntimeException("object can't null");
            }
            String json = GsonUtils.toJson(object);
            json(tag, json);
        }
    }

    /**
     * 格式化输出数组类型
     *
     * @param array
     */
    public static void d(Object[] array) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, "", array);
        }
    }

    public static void d(String tag, Object[] array) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, tag, array);
        }
    }

    /**
     * 格式化输出Throwable
     *
     * @param tr
     */
    public static void d(Throwable tr) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, "", "", tr);
        }
    }

    public static void d(String tag, Throwable tr) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, tag, "", tr);
        }
    }

    /**
     * 格式化输出msg
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, "", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isLogEnable) {
            androidLogger.log(LogLevel.DEBUG, tag, msg);
        }
    }

    /**
     * 格式化输出json
     *
     * @param json
     */
    public static void json(String json) {
        androidLogger.json("", json);
    }

    public static void json(String tag, String json) {
        androidLogger.json(tag, json);
    }

    /**
     * 使用系统的Log输出(非格式化输出)
     *
     * @param msg
     */
    public static void print(String msg) {
        if (isLogEnable) {
            Log.d(TAG, msg);
        }
    }

    ////////////////////////////////////////写日志文件///////////////////////////////////////////////////
    public static void writeFile(String msg) {
        if (isLogEnable) {
            if (!TextUtils.isEmpty(msg)) {
                print(msg);
                fileLogger.log(LogLevel.DEBUG, "", msg);
            }
        }
    }

    public static void writeFile(String tag, String msg) {
        if (isLogEnable) {
            if (!TextUtils.isEmpty(msg)) {
                print(msg);
                fileLogger.log(LogLevel.DEBUG, tag, msg);
            }
        }
    }

    public static void writeFile(Object object) {
        if (isLogEnable) {
            if (object != null) {
                d(object);
                fileLogger.log(LogLevel.DEBUG, "", GsonUtils.toJson(object));
            }
        }
    }

    public static void writeFile(String tag, Object object) {
        if (isLogEnable) {
            if (object != null) {
                d(object);
                fileLogger.log(LogLevel.DEBUG, tag, GsonUtils.toJson(object));
            }
        }
    }


}
