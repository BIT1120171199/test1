package chapter.android.aweme.ss.com.homework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Text;

import java.lang.reflect.GenericArrayType;

/**
 * 作业2：一个抖音笔试题：统计页面所有view的个数
 * Tips：ViewGroup有两个API
 * {@link android.view.ViewGroup #getChildAt(int) #getChildCount()}
 * 用一个TextView展示出来
 */
public class Exercises2 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public int getAllChildViewCount(View view) {
        //todo 补全你的代码
        int i=0;
        if (view != null) {
            int count = 0;
            if (!(view instanceof ViewGroup)) {
                return 1;
            }
            ViewGroup group = (ViewGroup) view;
            for (; i <group.getChildCount(); i++) {
                count += getAllChildViewCount(group.getChildAt(i));
            }
            return count;
        }
        throw new RuntimeException("The view isn't null.");
    }
    }