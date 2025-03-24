package com.example.unitconverter;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private EditText inputValue;
    private Spinner spinnerFrom, spinnerTo;
    private TextView resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        inputValue = findViewById(R.id.inputValue);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        Button convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        // Define unit categories
        String[] lengthUnits = {"inch", "foot", "yard", "mile", "cm", "km"};
        String[] weightUnits = {"pound", "ounce", "ton", "kg", "g"};
        String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

        // Merge all units into one array
        String[] allUnits = mergeArrays(lengthUnits, weightUnits, tempUnits);

        // Set adapters for spinners
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allUnits);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);

        // Convert button logic
        convertButton.setOnClickListener(v -> convertUnit());
    }

    private void convertUnit() {
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit = spinnerTo.getSelectedItem().toString();
        String inputStr = inputValue.getText().toString();
        //check if the input field is empty
        if (inputStr.isEmpty()) {
            resultText.setText("Please enter a value!");
            return;
        }
    //error validation
        double input;
        try {
            input = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            resultText.setText("Invalid number format!");
            return;
        }

        // Handle same unit conversion (no change needed)
        if (fromUnit.equals(toUnit)) {
            resultText.setText(" Source and destination units are the same. No conversion needed.");
            return;
        }

        // Perform the conversion
        double result = performConversion(fromUnit, toUnit, input);
        resultText.setText(" Converted Value: " + result);
    }

    private double performConversion(String fromUnit, String toUnit, double value) {
        // Convert to a base unit first
        double baseValue = toBaseUnit(fromUnit, value);
        return fromBaseUnit(toUnit, baseValue);
    }

    private double toBaseUnit(String unit, double value) {
        // Length Conversions to cm
        switch (unit) {
            case "inch": return value * 2.54;
            case "foot": return value * 30.48;
            case "yard": return value * 91.44;
            case "mile": return value * 160934; // Converted to cm
            case "cm": return value;
            case "km": return value * 100000; // Converted to cm

            // Weight Conversions to kg
            case "pound": return value * 0.453592;
            case "ounce": return value * 0.0283495;
            case "ton": return value * 907.185;
            case "kg": return value;
            case "g": return value / 1000;

            // Temperature Conversions
            case "Celsius": return value;
            case "Fahrenheit": return (value - 32) / 1.8; // Convert to Celsius
            case "Kelvin": return value - 273.15; // Convert to Celsius
        }
        return value;
    }

    private double fromBaseUnit(String unit, double value) {
        // Convert from base unit to target unit
        switch (unit) {
            case "inch": return value / 2.54;
            case "foot": return value / 30.48;
            case "yard": return value / 91.44;
            case "mile": return value / 160934;
            case "cm": return value;
            case "km": return value / 100000;

            case "pound": return value / 0.453592;
            case "ounce": return value / 0.0283495;
            case "ton": return value / 907.185;
            case "kg": return value;
            case "g": return value * 1000;

            case "Celsius": return value;
            case "Fahrenheit": return (value * 1.8) + 32;
            case "Kelvin": return value + 273.15;
        }
        return value;
    }

    private String[] mergeArrays(String[]... arrays) {
        int length = 0;
        for (String[] arr : arrays) length += arr.length;
        String[] result = new String[length];
        int pos = 0;
        for (String[] arr : arrays) {
            for (String value : arr) result[pos++] = value;
        }
        return result;
    }
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
      //  });
    //}
}
