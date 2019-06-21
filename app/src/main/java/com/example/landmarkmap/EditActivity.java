package com.example.landmarkmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_LANDMARK = 4;

    private EditText editTextName;
    private EditText editTextComment;
    private Button buttonConfirmEdit;


    private View.OnClickListener onConfirmClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (editTextName.getText().toString().length() > 0) {
                Intent createdEditedIntent = new Intent();
                if (getIntent().getIntExtra("request", 0) != REQUEST_CODE_EDIT_LANDMARK) {
                    Bundle point = getIntent().getExtras();
                    createdEditedIntent.putExtras(point);
                }
                createdEditedIntent.putExtra("name", editTextName.getText().toString());
                createdEditedIntent.putExtra("comment", editTextComment.getText().toString());
                setResult(RESULT_OK, createdEditedIntent);
                finish();
            } else {
                Toast.makeText(EditActivity.this, getString(R.string.no_name_toast), Toast.LENGTH_LONG).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle(R.string.title_activity_edit);

        editTextName = findViewById(R.id.editTextLandmarkName);
        editTextComment = findViewById(R.id.editTextLandmarkComment);
        buttonConfirmEdit = findViewById(R.id.buttonConfirmEdit);

        buttonConfirmEdit.setOnClickListener(onConfirmClickListener);

        if (getIntent().getIntExtra("request", 0) == REQUEST_CODE_EDIT_LANDMARK) {
            editTextName.setText(getIntent().getStringExtra("name"));
            editTextComment.setText(getIntent().getStringExtra("comment"));
        }
    }
}
