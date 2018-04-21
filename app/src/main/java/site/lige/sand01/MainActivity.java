package site.lige.sand01;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static test.AXMLPrinter.mains;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Button button1=(Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        final TextView textView=(TextView)findViewById(R.id.status);
        final TextView view=(TextView)findViewById(R.id.view);
        //utton2 Dynamic anaysis
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
                Intent dyInten=new Intent(MainActivity.this,DynamicActivity.class);
                startActivity(dyInten);
            }
        });
        //Unzip Button
        Button unzipButton = (Button)findViewById(R.id.begin_unzip);
        unzipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unzip("/storage/sdcard1/app-debug.apk","/storage/sdcard1/unzipFile/");
                Log.d(TAG, "onClick: Unzip finished");
            }
        });
        //XML decode Button
        Button XMLPrinter=(Button)findViewById(R.id.XML_Printer);
        XMLPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO:Decompile
                try{
                    mains();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Button read=(Button)findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "/storage/sdcard1/unzipFile/AndroidManifest.txt";

                String res="";

                try{

                    FileInputStream fin = new FileInputStream(fileName);

                    int length = fin.available();

                    byte [] buffer = new byte[length];

                    fin.read(buffer);

                    res = new String(buffer,"UTF-8");

                    fin.close();

                }catch(Exception e){

                    e.printStackTrace();

                }

                view.append(res);
            }
        });

    }

    //unzip function
    private static void Unzip(String zipFile, String targetDir) {
        int BUFFER = 1024*1024*10; //这里缓冲区我们使用10MB，
        String strEntry; //保存每个zip的条目名称

        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {

                try {
                    Log.i("Unzip: ","="+ entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
        }
    }
}
