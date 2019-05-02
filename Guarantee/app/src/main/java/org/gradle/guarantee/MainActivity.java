package org.gradle.guarantee;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    final Database db = new Database(this);
    Pattern pattern;
    Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";
        final TextView warrantyNo = (TextView) findViewById(R.id.warrantyNoTxt);
        final TextView itemName = (TextView) findViewById(R.id.itemNameTxt);
        final TextView warrDate = (TextView) findViewById(R.id.dateTxt);
        final Button addWarranty = (Button) findViewById(R.id.addWarrBtn);
        final Button searchWarranty = (Button) findViewById(R.id.searchWarrantyBtn);

        alertDialogBuilder = new AlertDialog.Builder(this);

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

    /**
     * Validate date format with regular expression
     * @param date date address for validation
     * @return true valid date format, false invalid date format
     */
    public boolean validate(final String date){
        matcher = pattern.matcher(date);

        if(matcher.matches()){
            matcher.reset();

            if(matcher.find()){
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        } else{
                            return true;
                        }
                    } else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        } else{
                            return true;
                        }
                    }
                } else{
                    return true;
                }
            } else{
                return false;
            }
        } else{
            return false;
        }
    }
}
