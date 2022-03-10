package com.example.winterdestination;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DestinationActivity extends AppCompatActivity{
    TextView name;
    TextView description;
    ImageView imageView;
    HashMap<String, Object> content;
    Button btnVoice, btnRead, btnMap;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        btnVoice = findViewById(R.id.btnVoice);
        btnRead = findViewById(R.id.btnRead);
        btnMap = findViewById(R.id.btnMap);
        Intent intent = getIntent();
        content = (HashMap<String, Object>)intent.getSerializableExtra("content");
        loadContent();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Log.e("TTS","Language not supported");
            }else {
                    Log.e("TTS","Initialization failed");
                }
            }
        });
    }

    public void listen(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
        if (intent.resolveActivity(getPackageManager())!=null)
            startActivityForResult(intent,10);
        else
            Toast.makeText(DestinationActivity.this, "Your device doesn't support speech recognition", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String command = result.get(0);
                if (command.equalsIgnoreCase("read"))
                    btnRead.performClick();
                else if (command.equalsIgnoreCase("show in map"))
                    btnMap.performClick();
                else
                    Toast.makeText(DestinationActivity.this, "Command not recognized", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goBack(View view){
        finish();
        startActivity(new Intent(DestinationActivity.this, MainActivity.class));
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }*/

    public void showMap(View view){
        LatLng latLng = new LatLng(Double.parseDouble( content.get("longitude").toString()),Double.parseDouble( content.get("latitude").toString()));
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("latLng",latLng);
        intent.putExtra("name",content.get("name").toString());
        startActivity(intent);
    }

    public void read(View view){
        String text = name.getText().toString() + "\n" + description.getText().toString();
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    @Override
    protected void onDestroy() {
        if (tts!=null){
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    private void loadContent() {
        name.setText(content.get("name").toString());
        description.setText(content.get("description").toString());
        Glide.with(this)
                .load(content.get("image").toString())
                .into(imageView);
    }
}