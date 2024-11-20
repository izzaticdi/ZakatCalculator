package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText weightInput, valueInput;
    private RadioGroup typeRadioGroup;
    private RadioButton radioKeep, radioWear;
    private Button calculateButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightInput = findViewById(R.id.editTextNumberDecimal);
        valueInput = findViewById(R.id.editTextNumberDecimal2);
        typeRadioGroup = findViewById(R.id.radioGroup);
        radioKeep = findViewById(R.id.radioKeep);
        radioWear = findViewById(R.id.radioWear);
        calculateButton = findViewById(R.id.button);
        resultTextView = findViewById(R.id.resultTextView);

        typeRadioGroup.clearCheck();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Zakat Calculator");

        // Set the button click listener for calculation
        calculateButton.setOnClickListener(v -> calculateZakat());
    }

    private void calculateZakat() {
        // Get input values
        String weightText = weightInput.getText().toString();
        String valueText = valueInput.getText().toString();

        // Check if inputs are empty
        if (weightText.isEmpty() || valueText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if no category is selected
        if (typeRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse input values
        double weight = Double.parseDouble(weightText);
        double value = Double.parseDouble(valueText);

        // Get selected gold type from RadioGroup
        String type = radioKeep.isChecked() ? "Keep" : "Wear";

        // Determine the threshold based on the gold type
        double threshold = type.equals("Keep") ? 85 : 200;

        // Step 1: Calculate the total value of gold
        double totalGoldValue = weight * value;

        // Step 2: Calculate "uruf" (gold weight minus threshold, set to 0 if negative)
        double uruf = weight - threshold;
        uruf = Math.max(uruf, 0);

        // Step 3: Calculate total gold value that is zakat payable
        double zakatPayableValue = uruf * value;

        // Step 4: Calculate total zakat (2.5% of zakat payable value)
        double totalZakat = zakatPayableValue * 0.025;

        // Display results in the TextView below the button
        String resultMessage = String.format(
                "Total Gold Value: RM %.2f\nZakat Payable Value: RM %.2f\nTotal Zakat: RM %.2f",
                totalGoldValue, zakatPayableValue, totalZakat);
        resultTextView.setText(resultMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;

        } else if (selected == R.id.menuSettings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (selected == R.id.menuShare) {
            shareAppUrl();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareAppUrl() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Check out the Zakat Calculator App: https://ZakatCalculator.com";
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Calculator App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}







