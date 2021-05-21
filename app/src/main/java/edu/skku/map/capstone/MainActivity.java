package edu.skku.map.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;

import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import javax.xml.parsers.ParserConfigurationException;



public class MainActivity extends AppCompatActivity {
    Button btn1, btn2;
    static String foldername;
    static int final_result = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.button);
        btn2 = (Button) findViewById(R.id.button2);
        foldername = this.getFilesDir().getAbsolutePath() +"/TestLog";

        try {
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

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Result Detecting From SingleDrag&Drop   :   "+detectFromDragAndDrop());
                System.out.println("Result Detecting From SingleTap   :   "+detectFromSingleTap()); //1이면 child
                System.out.println("Result Detecting From DoubleTap   :   "+detectFromDoubleTap());
                if(detectFromDragAndDrop()+detectFromSingleTap()+detectFromDoubleTap()>1) {
                    System.out.println("Final Detecting Result   :   Child");
                    final_result = 1;
                }
                else {
                    System.out.println("Final Detecting Result   :   Adult");
                    final_result = 0;
                }
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    private int detectFromDragAndDrop(){
        Interpreter singledragdrop = getTfliteInterpreter("singledrag.tflite");
        int result = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/TestLog/computedDragandDrop.txt"));
            String str = null;
            int cnt = 0;
            int sum = 0; //child라고 판별한 횟수

            while((str = br.readLine())!=null){
                String[] input = str.split(",");
                float[] input2 = new float[5];
                for(int i=0; i<5; i++){
                    try {
                        input2[i] = Float.parseFloat(input[i]);
                    }catch(NumberFormatException e){
                        break;
                    }catch (Exception e){
                        break;
                    }
                }
                float[][] output = new float[1][1];
                singledragdrop.run(input2, output);

                if(cnt < 3){
                    System.out.println("machin_learing_singleDragAndDrop_output-----"+output[0][0]);
                    if(output[0][0]>0.5){
                        sum++;
                    }
                    cnt++;
                }else{
                    break;
                }
            }
            if(sum > 1){
                result = 1;
            }else{
                result = 0;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int detectFromSingleTap(){
        Interpreter tap = getTfliteInterpreter("tap.tflite");
        int result = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/TestLog/computedSingleTap.txt"));
            String str = null;
            int cnt = 0;
            int sum = 0; //child라고 판별한 횟수

            while((str = br.readLine())!=null){
                String[] input = str.split(",");
                float[] input2 = new float[2];
                for(int i=0; i<2; i++){
                    try {
                        input2[i] = Float.parseFloat(input[i]);
                    }catch(NumberFormatException e){
                        break;
                    }catch (Exception e){
                        break;
                    }
                }
                float[][] output = new float[1][1];
                tap.run(input2, output);

                if(cnt < 3){
                    System.out.println("machin_learing_sinlgeTap_output-----"+output[0][0]);
                    if(output[0][0]>0.5){
                        sum++;
                    }
                    cnt++;
                }else{
                    break;
                }
            }
            if(sum > 1){
                result = 1;
            }else{
                result = 0;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int detectFromDoubleTap(){
        Interpreter doubletap = getTfliteInterpreter("doubletap.tflite");
        int result = 0;
        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir()+"/TestLog/computedDoubleTap.txt"));
            String str = null;
            int cnt = 0;
            int sum = 0; //child라고 판별한 횟수

            while((str = br.readLine())!=null){
                String[] input = str.split(",");
                float[] input2 = new float[7];
                for(int i=0; i<7; i++){
                    try {
                        input2[i] = Float.parseFloat(input[i]);
                    }catch(NumberFormatException e){
                        break;
                    }catch (Exception e){
                        break;
                    }
                }
                float[][] output = new float[1][1];
                doubletap.run(input2, output);

                if(cnt < 3){
                    System.out.println("machin_learing_doubleTap_output-----"+output[0][0]);
                    if(output[0][0]>0.5){
                        sum++;
                    }
                    cnt++;
                }else{
                    break;
                }
            }
            if(sum > 1){
                result = 1;
            }else{
                result = 0;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"File not found",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(MainActivity.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}