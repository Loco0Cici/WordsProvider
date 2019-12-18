package com.example.wordsprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "ContentProviderClient";

    private static final String AUTHORITY = "com.example.lishuqi.assess.wordsprovider";
    private static final Uri WORD_URI = Uri.parse("content://" + AUTHORITY + "/word");

    ContentResolver contentResolver;

    private EditText etWord;
    private EditText etMean;
    private EditText etSample;

    private TextView textAdd;
    private TextView textDel;
    private TextView textUpdate;
    private TextView textQuery;

    private ListView listview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity onCreate");
        contentResolver = getContentResolver();

        etWord = (EditText)findViewById(R.id.et_word);
        etMean = (EditText)findViewById(R.id.et_mean);
        etSample = (EditText)findViewById(R.id.et_sample);

        textAdd = (TextView)findViewById(R.id.button_add);
        textAdd.setOnClickListener(this);
        textDel = (TextView)findViewById(R.id.button_delete);
        textDel.setOnClickListener(this);
        textUpdate = (TextView)findViewById(R.id.button_update);
        textUpdate.setOnClickListener(this);
        textQuery = (TextView)findViewById(R.id.button_query);
        textQuery.setOnClickListener(this);

        listview = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_query) {
            Cursor cursor = contentResolver.query(WORD_URI, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    SimpleCursorAdapter simpleCursorAdapter = new
                            SimpleCursorAdapter(MainActivity.this, // 上下文
                            R.layout.layout_item, // Item布局
                            cursor, // Cursor 查询出来的游标 这里面有数据库里面的数据
                            new String[]{"word", "meaning", "sample"}, // 从哪里来，指的是 查询出数据库列名
                            new int[]{R.id.tv_item_word, R.id.tv_item_mean, R.id.tv_item_sample}, // 到哪里去，指的是，把查询出来的数据，赋值给Item布局 的控件
                            SimpleCursorAdapter.NO_SELECTION);

                    // 给ListView设置使用SimpleCursorAdapter适配器
                    listview.setAdapter(simpleCursorAdapter);

                    String word = cursor.getString(cursor.getColumnIndex("word"));
                    String meaning = cursor.getString(cursor.getColumnIndex("meaning"));
                    String sample = cursor.getString(cursor.getColumnIndex("sample"));
                    Log.d(getClass().getSimpleName(), "word = " + word + ", meaning = " + meaning + ", sample = " + sample);
                }
            }
        } else if (id == R.id.button_add) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_id", 2);
            contentValues.put("word", etWord.getText().toString());
            contentValues.put("meaning", etMean.getText().toString());
            contentValues.put("sample", etSample.getText().toString());
            contentResolver.insert(WORD_URI, contentValues);
        } else if (id == R.id.button_delete) {
            contentResolver.delete(WORD_URI, "word = ?", new String[]{etWord.getText().toString()});
        } else if (id == R.id.button_update) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("meaning", etMean.getText().toString());
            contentValues.put("sample", etSample.getText().toString());
            contentResolver.update(WORD_URI, contentValues, "word = ?", new String[]{etWord.getText().toString()});
        }
    }
}
