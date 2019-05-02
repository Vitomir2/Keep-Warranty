package org.gradle.guarantee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final TextView warrNo = (TextView) findViewById(R.id.warrantyNoTxt);
        final TextView showWarrBox = (TextView) findViewById(R.id.resultView);
        final Button searchWarranty = (Button) findViewById(R.id.searchBtn);
        final Button backBtn = (Button) findViewById(R.id.backBtn);
        final Database db = new Database(this);
        final List<Warranty> warrs = db.getAllWarranties();

        searchWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aWarrNo = warrNo.getText().toString();

                if (aWarrNo.matches("")) {
                    Toast.makeText(SearchActivity.this, "You have to fill Warranty No. Field!", Toast.LENGTH_SHORT).show();
                    return;
                }

                showWarrBox.setText("");
                int searchingID = Integer.parseInt(aWarrNo);

                if (warrs.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "There are no registered warranties.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String log = "";
                    for (Warranty currWarr : warrs) {
                        if (searchingID == currWarr.getID()) {
                            log = "ID: " + currWarr.getID() + "\n" +
                                  "Item Name: " + currWarr.getItemName() + "\n" +
                                  "Validity Date: "  + currWarr.getDate();
                        }
                    }

                    showWarrBox.setText(log);
                    Toast.makeText(SearchActivity.this, "Warranty is found!", Toast.LENGTH_SHORT).show();
                    clearWarrantyField();
                    return;
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                clearResultView();
            }
        });
    }

    public void clearWarrantyField() {
        TextView warrantyNo = (TextView) findViewById(R.id.warrantyNoTxt);
        warrantyNo.setText("");
    }

    public void clearResultView() {
        TextView resView = (TextView) findViewById(R.id.resultView);
        resView.setText("");
    }
}