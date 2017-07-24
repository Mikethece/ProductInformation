package com.example.rumeysal.productinformation;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static List<DateAndVacuum> your_array_list = new ArrayList<DateAndVacuum>();
    TextView cihazadi;
    ListView urunList;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog=new ProgressDialog(MainActivity.this);
        dialog.setMessage("Bekleyiniz :)");
        dialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference products = database.getReference("message");

        cihazadi  =(TextView)findViewById(R.id.UrunAdı);
        urunList=(ListView) findViewById(R.id.UrunList);

        DatabaseReference refProduct=FirebaseDatabase.getInstance().getReference().child("Products").child("Product1");
        refProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                your_array_list.clear();

                cihazadi.setText("Cihaz Adı: "+ dataSnapshot.child("ProductName").getValue().toString());
                String id = dataSnapshot.child("ID").getValue().toString();
                DateAndVacuum davID = new DateAndVacuum("ID: ", id);
                String unit = dataSnapshot.child("Unit").getValue().toString();
                DateAndVacuum davUnit = new DateAndVacuum("UNIT: : ", unit);
                    DateAndVacuum davVV = new DateAndVacuum("DATE    ", "VACUUM VALUE");

                your_array_list.add(davID);
                your_array_list.add(davUnit);
                your_array_list.add(davVV);

                for(DataSnapshot dsProcess : dataSnapshot.child("Processes").getChildren()){
                    String dp = dsProcess.child("Date").getValue().toString();
                    String vv = dsProcess.child("VacuumValue").getValue().toString();
                    DateAndVacuum dav = new DateAndVacuum(dp, vv);
                    your_array_list.add(dav);
                }
                dialog.cancel();
                UrunAdapter adapter = new UrunAdapter(MainActivity.this, R.layout.urun_adi, your_array_list );

                urunList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
