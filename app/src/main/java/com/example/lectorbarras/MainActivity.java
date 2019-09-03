package com.example.lectorbarras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurarLector();
    }

    private void configurarLector(){
        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).initiateScan();
            }
        });
    }

    private void actualiarUITextViews(String resultadoScaneo, String formatoResultado){
        ((TextView)findViewById(R.id.tvFormat)).setText(formatoResultado);
        ((TextView)findViewById(R.id.tvResult)).setText(resultadoScaneo);
    }

    private void manipularResultado(IntentResult intentResult){
        if(intentResult != null){

            actualiarUITextViews(intentResult.getContents(),intentResult.getFormatName());
        }else{
            Toast.makeText(getApplicationContext(),"No se leyó nada",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        final IntentResult intentResult =
            IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        manipularResultado(intentResult);
    }

    public void copiarPortapapeles(View view) {

        TextView textop;
        textop = (TextView) findViewById(R.id.tvResult);
        String text = textop.getText().toString();

        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip;

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(getApplicationContext(),"Se copio correctamente",Toast.LENGTH_SHORT).show();
    }

    public void abrirResultado(View view) {
        TextView textop;
        textop = (TextView) findViewById(R.id.tvResult);
        String text = textop.getText().toString();

        if (urlValidator(text)){
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(text));
            startActivity(viewIntent);
        }else {
            Toast.makeText(getApplicationContext(),"URL NO VALIDA",Toast.LENGTH_SHORT).show();
        }


    }

    public static boolean urlValidator(String url)
    {
        /*validación de url*/
        try {
            new URL(url).toURI();
            return true;
        }
        catch (URISyntaxException exception) {
            return false;
        }
        catch (MalformedURLException exception) {
            return false;
        }
    }
}
