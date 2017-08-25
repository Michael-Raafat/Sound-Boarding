package studios.kdc.soundboarding;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class GroupCreator extends AppCompatActivity {
    private EditText grpName;
    private int chosenColor;
    private TextView currentChosenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null)
           ab.setDisplayHomeAsUpEnabled(true);
        this.grpName = (EditText) findViewById(R.id.grp_name);
        this.chosenColor = getResources().getColor(R.color.grey);
        this.currentChosenColor = (TextView) findViewById(R.id.track_chosen_color);
        this.setOnClickListenerToColorPickerButton();
        this.setOnClickListenerToSaveButton();
    }
    private void setOnClickListenerToSaveButton(){
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(grpName.getText().toString().trim().isEmpty() &&
                        grpName.getText().toString().trim().replace("\n","").isEmpty()){
                    Toast.makeText(getApplicationContext() ,"Name is required" , Toast.LENGTH_LONG).show();
                }else {
                    DataController.getInstance().createGroup(getApplicationContext(), grpName.getText().toString(), chosenColor) ;
                    finish();
                }
            }
        });
    }
    private void setOnClickListenerToColorPickerButton(){
        ImageButton colorPickerButton = (ImageButton) findViewById(R.id.color_chooser);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });
    }

    private void showColorPicker(){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        chosenColor = selectedColor;
                        currentChosenColor.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
