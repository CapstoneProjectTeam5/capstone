package edu.skku.map.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;

import edu.skku.map.capstone.MyPainter;
import edu.skku.map.capstone.R;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    static String foldername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.button);
        foldername = this.getFilesDir().getAbsolutePath() +"/TestLog";
        //final String filename1 = "DragandDrop.txt";
        //final String filename2 = "SingleTap.txt";
        //final String filename3 = "DoubleTap.txt";
        try {
            /*
            File init_file1 = new File (foldername+"/"+filename1);
            File init_file2 = new File (foldername+"/"+filename2);
            File init_file3 = new File (foldername+"/"+filename3);
            if(init_file1.exists())
                init_file1.delete();
            if(init_file2.exists())
                init_file2.delete();
            if(init_file3.exists())
                init_file3.delete();

             */
            File dir = new File(foldername);
            File[] childFileList = dir.listFiles();
            if (dir.exists()) {
                for (File childFile : childFileList) {
                    childFile.delete();
                }
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "파일 초기화 실패", Toast.LENGTH_SHORT).show();
        }

        MyPainter mp = new MyPainter(this);
        mp = (MyPainter) findViewById(R.id.mypainter);
        mp.setBackgroundDrawable(getResources().getDrawable(R.drawable.pic1));
        MyPainter2 mp2 = new MyPainter2(getApplicationContext());
        mp2 = (MyPainter2) findViewById(R.id.mypainter2);
        mp2.setBackgroundDrawable(getResources().getDrawable(R.drawable.pic2));
        MyPainter3 mp3 = new MyPainter3(getApplicationContext());
        mp3 = (MyPainter3) findViewById(R.id.mypainter3);
        mp3.setBackgroundDrawable(getResources().getDrawable(R.drawable.pic3));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Compute();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}