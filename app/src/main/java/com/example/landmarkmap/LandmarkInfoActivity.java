package com.example.landmarkmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LandmarkInfoActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_LANDMARK = 4;
    private static final int RESULT_CODE_LANDMARK_DELETED = 1;
    private static final int RESULT_CODE_LANDMARK_EDITED = 2;

    private String markerId;
    private TextView textViewInfoName;
    private TextView textViewInfoComment;
    private Button buttonInfoEdit;
    private Button buttonInfoDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_info);
        setTitle(R.string.title_activity_landmark_info);

        markerId = getIntent().getStringExtra("id");

        textViewInfoName = findViewById(R.id.textViewInfoName);
        textViewInfoComment = findViewById(R.id.textViewInfoComment);
        buttonInfoEdit = findViewById(R.id.buttonInfoEdit);
        buttonInfoDelete = findViewById(R.id.buttonInfoDelete);

        textViewInfoName.setText(getIntent().getStringExtra("name"));
        textViewInfoComment.setText(getIntent().getStringExtra("comment"));

        buttonInfoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(LandmarkInfoActivity.this, EditActivity.class);
                editIntent.putExtra("request", REQUEST_CODE_EDIT_LANDMARK);
                editIntent.putExtra("name", getIntent().getStringExtra("name"));
                editIntent.putExtra("comment", getIntent().getStringExtra("comment"));
                startActivityForResult(editIntent, REQUEST_CODE_EDIT_LANDMARK);
            }
        });
        buttonInfoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent();
                deleteIntent.putExtra("id", markerId);
                setResult(RESULT_CODE_LANDMARK_DELETED, deleteIntent);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE_EDIT_LANDMARK) : {
                if (resultCode == EditActivity.RESULT_OK) {
                    Intent toMap = new Intent();
                    Bundle bundle = data.getExtras();
                    toMap.putExtras(bundle);
                    toMap.putExtra("id", markerId);
                    setResult(RESULT_CODE_LANDMARK_EDITED, toMap);
                    finish();
                }
                break;
            }
        }
    }
}
