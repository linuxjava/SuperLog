package free.xiao.log.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import free.xiao.superlog.SuperLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //日志输出
        SuperLog.d("test------------------");
        SuperLog.d(new Data());
        SuperLog.d(new String[]{"北京", "上海", "广州"});

        SuperLog.d("xiao1", "test------------------");
        SuperLog.d("xiao1", new Data());
        SuperLog.d("xiao1", new String[]{"北京", "上海", "广州"});

        //写文件
        SuperLog.writeFile("test------------------");
        SuperLog.writeFile(new Data());
        SuperLog.writeFile("xiao1", "test------------------");
        SuperLog.writeFile("xiao1", new Data());
    }

    public class Data {
        public String param1 = "123";
        public List<String> list = new ArrayList<>();

        public Data() {
            list.add("test1");
            list.add("test2");
            list.add("test3");
        }
    }
}
