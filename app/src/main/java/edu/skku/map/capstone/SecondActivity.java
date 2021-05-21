package edu.skku.map.capstone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class SecondActivity extends AppCompatActivity {
    TextView resultText;
    String packageName = "com.kiddoware.kidsplace";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        resultText = (TextView) findViewById(R.id.textView);

        if (MainActivity.final_result == 1) {
            try{
                Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            catch (Exception e){
                String url = "market://details?id=" + packageName;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }

            Toast.makeText(this.getApplicationContext(),"Child!:)", Toast.LENGTH_SHORT).show();
            resultText.setText("Child");
        } else {
            Toast.makeText(this.getApplicationContext(),"Adult!:)", Toast.LENGTH_SHORT).show();
            resultText.setText("Adult");

            //어른이면 팝업 띄우고 앱 종료
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }
}
