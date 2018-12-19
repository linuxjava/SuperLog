# SuperLog

项目源自于[xLog](https://github.com/elvishew/xLog)，工作项目使用了该项目作为日志输出模块，格式化输出很好很方便，但是在实际
的使用中也遇到了一些无法满足的需求和bug，因此SuperLog是基于xLog的二次开发，内容如下：
项目特点
* 原有xlog中的Logger和xLog类太臃肿，很多方法其实并没什么作用，对此进行优化；
* 修复xlog无法支持自定义tag；
* 添加方法支持日志文件设置过期天数；
* 日志文件支持自定义名字；
* 修复文件写日志的bug；

## 使用
### 添加依赖
Add it in your root build.gradle at the end of repositories:
```xml
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
 Add the dependency
```xml
dependencies {
    implementation 'com.github.linuxjava:SuperLog:1.0'
}
```

### 配置参考
配置可以参考SuperLog中的设置
```xml
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
```
### 方法
* Logcat格式化输出：
```xml
/**
 * 格式化输出JavaBean
 *
 * @param object
 */
public static void d(Object object)

/**
 * 格式化输出数组类型
 *
 * @param array
 */
public static void d(Object[] array)

/**
 * 格式化输出Throwable
 *
 * @param tr
 */
public static void d(Throwable tr)

/**
* 格式化输出msg
*
* @param msg
*/
public static void d(String msg)

/**
* 格式化输出json
*
* @param json
*/
public static void json(String json)

/**
 * 使用系统的Log输出(非格式化输出)
 *
 * @param msg
 */
public static void print(String msg)    
```
以上所有方法都包含另一版本，支持自定义TAG输出。

![image](https://github.com/elvishew/XLog/blob/master/images/classic_log.png)

* 写日志文件：
```xml
public static void writeFile(String msg)

public static void writeFile(Object object)
```
以上所有方法都包含另一版本，支持自定义TAG输出。