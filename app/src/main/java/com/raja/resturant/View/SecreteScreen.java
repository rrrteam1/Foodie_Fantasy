package com.raja.resturant.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.raja.resturant.Controll.LocalDatabase.CartManagement;
import com.raja.resturant.Controll.LocalDatabase.DatabaseHelper;
import com.raja.resturant.Controll.LocalDatabase.MySQLiteManager;
import com.raja.resturant.Controll.LocalDatabase.SessionManagement;
import com.raja.resturant.Controll.LocalDatabase.UserManagement;
import com.raja.resturant.Controll.MyFunctionBox;
import com.raja.resturant.MainActivity;
import com.raja.resturant.R;

import java.util.Arrays;

public class SecreteScreen extends AppCompatActivity implements View.OnClickListener {

    private MySQLiteManager mySQLiteManager;
    private MyFunctionBox myFunctionBox;
    private AppCompatEditText sec_et;
    private TextView sec_tv_msg;
    private CardView sec_execute_btn, sec_clear_btn;
    private TableLayout tableLayout;
    public DatabaseHelper dbHelper;
    public UserManagement userManagement;
    public CartManagement cartManagement;
    public SessionManagement sessionManagement;
    private Cursor cursor;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secrete_screen);


        myFunctionBox = new MyFunctionBox(SecreteScreen.this);
        mySQLiteManager = new MySQLiteManager(SecreteScreen.this);

        dbHelper = new DatabaseHelper(SecreteScreen.this);
        db = dbHelper.getWritableDatabase();
        userManagement = new UserManagement(db);
        cartManagement = new CartManagement(db);
        sessionManagement = new SessionManagement(SecreteScreen.this);

        sec_et = (AppCompatEditText) findViewById(R.id.sec_et_id);
        tableLayout = (TableLayout) findViewById(R.id.sec_tablayou_id);
        sec_tv_msg = (TextView) findViewById(R.id.sec_tv_msg_id);
        sec_execute_btn = (CardView) findViewById(R.id.sec_cv_execute_id);
        sec_clear_btn = (CardView) findViewById(R.id.sec_cv_clear_id);

        sec_execute_btn.setOnClickListener(this);
        sec_clear_btn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sec_cv_execute_id){
            mExecuteCommand();
        }else if(v.getId() == R.id.sec_cv_clear_id){
            sec_et.setText("");
            sec_tv_msg.setText("");
            tableLayout.removeAllViews();
        }
    }

    private void mExecuteCommand() {
        String sql = sec_et.getText().toString().trim();
        executeCommand(sql);

    }
    private void executeCommand(String command) {
        String finalQuery = command;

        try {

            cursor = db.rawQuery(finalQuery, null);
            if (cursor.getCount() > 0){
                addRow(cursor.getColumnNames());

                // Add data rows
                while (cursor.moveToNext()) {
                    addRow(cursorToStringArray());
                }
            }


            cursor.close();
        } catch (Exception e) {
            sec_tv_msg.setText("Error: " + e.getMessage());
        }
    }
    private void addRow(String[] data) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        for (String value : data) {
            TextView textView = new TextView(this);
            textView.setText(value);
            textView.setPadding(5, 5, 5, 5); // Adjust padding as needed
            tableRow.addView(textView);
        }

        tableLayout.addView(tableRow);
    }

    private String[] cursorToStringArray() {
        int numColumns = cursor.getColumnCount();
        String[] data = new String[numColumns];
        for (int i = 0; i < numColumns; i++) {
            data[i] = cursor.getString(i);
        }
        return data;
    }
}