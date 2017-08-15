package studios.kdc.soundboarding;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class TrackUploader extends AppCompatActivity {
    private TextView trackUploadName;
    private EditText trackUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_uploader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.trackUploadName = (TextView) findViewById(R.id.track_uploaded_name);
        this.trackUserName = (EditText) findViewById(R.id.track_name);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
           ab.setDisplayHomeAsUpEnabled(true);
        this.initializeSpinner();
        this.initializeUploadButton();
        this.initializeSaveButton();

    }

    private void initializeSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.groups);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DataController.getInstance().getGroupNames());
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    private void initializeSaveButton() {
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trackUserName.getText().toString().trim().isEmpty() &&
                        trackUserName.getText().toString().trim().toString().replace("\n","").isEmpty()){
                    Toast.makeText(getApplicationContext() ,"Name is required" , Toast.LENGTH_LONG).show();
                } else if (trackUploadName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext() ,"You must choose track to upload" , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext() ,"Track is successfully uploaded" , Toast.LENGTH_LONG).show();
                    //TODO PATH and GROUPNAME
                    DataController.getInstance().createTrack(trackUserName.getText().toString().trim(), "", "");
                    finish();
                }
            }
        });

    }
    private void initializeUploadButton() {
        ImageButton uploadButton = (ImageButton) findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_upload = new Intent();
                intent_upload.setType("audio/*.mp3");
                intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent_upload,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
               Uri uri= data.getData();
                if (uri.getScheme().equals("content")) {
                    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            trackUploadName.setText(cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)));
                            cursor.close();
                        }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}