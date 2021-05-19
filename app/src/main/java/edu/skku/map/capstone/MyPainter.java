package edu.skku.map.capstone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyPainter extends View {
    String foldername =getContext().getFilesDir().getAbsolutePath() +"/TestLog";
    final static String filename = "DragandDrop.txt";

    private Paint paint = new Paint();
    //여러가지의 그리기 명령을 모았다가 한번에 출력해주는
    //버퍼역할을 담당한다..
    private Path path = new Path();

    private int x,y;

    public MyPainter(Context context){
        super(context);
    }
    public MyPainter(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }


    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
        paint.setColor(Color.YELLOW);
        //STROKE속성을 이용하여 테두리...선...
        paint.setStyle(Paint.Style.STROKE);
        //두께
        paint.setStrokeWidth(15);
        //path객체가 가지고 있는 경로를 화면에 그린다...
        canvas.drawPath(path,paint);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int)event.getX();
        y = (int)event.getY();
        String contents;
        long on, off, move;

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                on = System.currentTimeMillis()*1000;
                contents = "X="+x+" Y="+y+" T="+on+"\n";
                WriteTextFile(foldername, filename, contents);
            //    Toast.makeText(getContext(), contents, Toast.LENGTH_SHORT).show();
                path.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                move = System.currentTimeMillis()*1000;
                x = (int)event.getX();
                y = (int)event.getY();
                contents = "X="+x+" Y="+y+" T="+move+"\n";
                WriteTextFile(foldername, filename, contents);
           //     Toast.makeText(getContext(), contents, Toast.LENGTH_SHORT).show();
                path.lineTo(x,y);
                break;

            case MotionEvent.ACTION_UP:
                off = System.currentTimeMillis()*1000;
                contents = "X="+x+" Y="+y+" T="+off+"\n";
                WriteTextFile(foldername, filename, contents);
                WriteTextFile(foldername, filename, "\n");
            //    Toast.makeText(getContext(), contents, Toast.LENGTH_SHORT).show();
                break;
        }
        //View의 onDraw()를 호출하는 메소드...
        invalidate();
        return true;
    }

    //텍스트내용을 경로의 텍스트 파일에 쓰기
    public void WriteTextFile(String foldername, String filename, String contents){
        try{
            File dir = new File (foldername);
            //디렉토리 폴더가 없으면 생성함
            if(!dir.exists()){
                dir.mkdirs();
            }
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(foldername+"/"+filename, true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
      //      Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();

        }catch (IOException e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

}
