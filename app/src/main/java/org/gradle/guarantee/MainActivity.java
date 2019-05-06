package org.gradle.guarantee;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    private static final String TAG = "MainActivity";
    private DatePickerDialog.OnDateSetListener aDateSetListener;
    final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView warrantyNo = (TextView) findViewById(R.id.warrantyNoTxt);
        final TextView itemName = (TextView) findViewById(R.id.itemNameTxt);
        final TextView warrDate = (TextView) findViewById(R.id.dateTxt);
        final Button addWarranty = (Button) findViewById(R.id.addWarrBtn);
        final Button searchWarranty = (Button) findViewById(R.id.searchWarrantyBtn);

        alertDialogBuilder = new AlertDialog.Builder(this);

        warrDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        aDateSetListener,
                        year,
                        month,
                        day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        aDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                warrDate.setText(date);
            }
        };

        addWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aWarrNo = warrantyNo.getText().toString();
                String aItemName = itemName.getText().toString();
                String aWarrDate = warrDate.getText().toString();

                if ((aWarrNo.matches("")) || (aItemName.matches("")) || (aWarrDate.matches(""))) {
                    Toast.makeText(MainActivity.this, "You have to fill all the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int insertingID = Integer.parseInt(aWarrNo);
                boolean isAdded = false;

                List<Warranty> warrs = db.getAllWarranties();
                if (warrs.isEmpty()) {
                    db.addWarranty(new Warranty(insertingID , aItemName, aWarrDate));
                    showDialog("Warranty Inserted Successfully!", "Keep Warranty");
                    return;
                } else {
                    for (Warranty currWarr : warrs) {
                        if (insertingID == currWarr.getID()) isAdded = true;
                    }

                    if (!isAdded) {
                        db.addWarranty(new Warranty(insertingID, aItemName, aWarrDate));
                        showDialog("Warranty Inserted Successfully!", "Keep Warranty");
                        return;
                    } else {
                        Toast.makeText(MainActivity.this, "This Warranty is already inserted!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        searchWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, SearchActivity.class));
            }
        });
    }

    public void showDialog(String pMessage, String pTitle) {
        alertDialogBuilder.setMessage(pMessage);
        alertDialogBuilder.setTitle(pTitle);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearFields();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void clearFields() {
        TextView warrantyNo = (TextView) findViewById(R.id.warrantyNoTxt);
        TextView itemName = (TextView) findViewById(R.id.itemNameTxt);
        TextView warrDate = (TextView) findViewById(R.id.dateTxt);
        warrantyNo.setText("");
        itemName.setText("");
        warrDate.setText("");
    }
}