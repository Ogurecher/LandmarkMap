package com.example.landmarkmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearchQuery;
    private ListView list;

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                HashMap<String, MarkerOptions> markers = (HashMap<String, MarkerOptions>) getIntent().getExtras().get("markers");

                ArrayList<MarkerOptions> result = new ArrayList<>();
                String query = editTextSearchQuery.getText().toString();

                for (HashMap.Entry<String, MarkerOptions> entry : markers.entrySet()) {
                    MarkerOptions marker = entry.getValue();

                    if ((marker.getTitle().contains(query)) || (marker.getSnippet().contains(query))) {
                        result.add(marker);
                    }
                }
                if (result.size() > 0) {
                    ListViewAdapter adapter = new ListViewAdapter(result, R.layout.list_item, SearchActivity.this);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            long viewId = view.getId();

                            if (viewId == R.id.buttonLocate) {
                                MarkerOptions marker = (MarkerOptions) list.getItemAtPosition(position);

                                Intent toMap = new Intent();
                                toMap.putExtra("marker", marker);
                                setResult(RESULT_OK, toMap);
                                finish();
                            }
                        }
                    });
                } else {
                    list.setAdapter(null);
                }
                return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.title_activity_search);

        list = findViewById(R.id.listView);
        editTextSearchQuery = findViewById(R.id.editTextSearchQuery);
        editTextSearchQuery.setOnEditorActionListener(onEditorActionListener);
    }
}
