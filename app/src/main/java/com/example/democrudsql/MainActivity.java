package com.example.democrudsql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText edmalop, edtenlop, edsiso;
    Button btnthem, btnsua, btnxoa, btnxem;
    ListView lv;
    ArrayList<String> myList;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase mydatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edmalop = findViewById(R.id.editMa);
        edtenlop = findViewById(R.id.edtenlop);
        edsiso = findViewById(R.id.edsiso);
        btnthem = findViewById(R.id.btninsert);
        btnsua = findViewById(R.id.btnupdate);
        btnxoa = findViewById(R.id.btnDelete);
        btnxem = findViewById(R.id.btnshow);

        lv = findViewById(R.id.lv);
        myList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(myAdapter);

        mydatabase = openOrCreateDatabase("qlsv_2.db", MODE_PRIVATE, null);

        try {
            String sql = "CREATE TABLE IF NOT EXISTS lop (malop TEXT primary key, tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edmalop.getText().toString();
                String tenlop = edtenlop.getText().toString();
                String sisoStr = edsiso.getText().toString();
                int siso = 0;
                try {
                    siso = Integer.parseInt(sisoStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_LONG).show();
                    return;
                }

                ContentValues myvalues = new ContentValues();
                myvalues.put("malop", malop);
                myvalues.put("tenlop", tenlop);
                myvalues.put("siso", siso);

                String msg = "";
                if (mydatabase.insert("lop", null, myvalues) == -1) {
                    msg = "Loi them du lieu";
                } else {
                    msg = "Them du lieu thanh cong";
                }

                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = edmalop.getText().toString();
                int n = mydatabase.delete("lop", "malop = ?", new String[]{malop});
                String msg = "";

                if (n == 0) {
                    msg = "ko co du lieu di xoa";
                } else {
                    msg = "Xoa thanh cong";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sisoStr = edsiso.getText().toString();
                int siso = 0;
                try {
                    siso = Integer.parseInt(sisoStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_LONG).show();
                    return;
                }

                String malop = edmalop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("siso", siso);
                int n = mydatabase.update("lop", myvalue, "malop = ?", new String[]{malop});
                String msg = "";
                if (n == 0) {
                    msg = "ko co du lieu de sua";
                } else {
                    msg = "Sua thanh cong";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

        btnxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myList.clear();
                Cursor c = mydatabase.query("lop", null, null, null, null, null, null);
                if (c != null) {
                    while (c.moveToNext()) {
                        String data = c.getString(0) + "-" + c.getString(1) + "-" + c.getInt(2);
                        myList.add(data);
                    }
                    c.close();
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mydatabase != null && mydatabase.isOpen()) {
            mydatabase.close();
        }
        super.onDestroy();
    }
}