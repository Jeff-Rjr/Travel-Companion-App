package com.example.travel_companion_app;

// import necessary packages
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // declare UI widgets
    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText editTextValue;
    Button buttonConvert;
    TextView textViewResult;

    // declare category options for spinner category
    String[] categories = {"Currency", "Fuel Efficiency", "Temperature"};

    // declare unit options per category for spinners from and to unit
    String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuelUnits     = {"mpg", "km/L", "Gallon (US)", "Liters", "Nautical Mile", "Kilometers"};
    String[] tempUnits     = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link UI elements
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom     = findViewById(R.id.spinnerFrom);
        spinnerTo       = findViewById(R.id.spinnerTo);
        editTextValue   = findViewById(R.id.editTextValue);
        buttonConvert   = findViewById(R.id.buttonConvert);
        textViewResult  = findViewById(R.id.textViewResult);

        // create array adapter for spinner categories
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // upon selecting category, update the unit spinners
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // convert button
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doConversion();
            }
        });
    }

    // method to update from/to spinners based on selected category
    void updateUnitSpinners(int categoryIndex) {
        String[] units;
        if (categoryIndex == 0) {
            units = currencyUnits;
        } else if (categoryIndex == 1) {
            units = fuelUnits;
        } else {
            units = tempUnits;
        }

        // create array adapter to select from/to which unit
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
    }

    // method to perform conversion
    void doConversion() {
        String inputNum = editTextValue.getText().toString().trim();
        String fromUnit = spinnerFrom.getSelectedItem().toString();
        String toUnit   = spinnerTo.getSelectedItem().toString();
        int category    = spinnerCategory.getSelectedItemPosition();

        // if unit from and unit to are the same
        if (fromUnit.equals(toUnit)) {
            textViewResult.setText(String.format("%.2f %s", Double.parseDouble(inputNum), toUnit));
            return;
        }

        // if unit from and unit to are different
        double inputValue = Double.parseDouble(inputNum);
        double result;

        if (category == 0) {
            result = convertCurrency(inputValue, fromUnit, toUnit);
        } else if (category == 1) {
            result = convertFuel(inputValue, fromUnit, toUnit);
        } else {
            result = convertTemperature(inputValue, fromUnit, toUnit);
        }

        textViewResult.setText(String.format("%.2f %s", result, toUnit));
    }

    // CURRENCY CONVERSION
    double convertCurrency(double value, String from, String to) {
        // convert from source to USD
        double inUSD = toUSD(value, from);
        // convert from USD to destination
        return fromUSD(inUSD, to);
    }

    double toUSD(double value, String unit) {
        switch (unit) {
            case "AUD": return value / 1.55;
            case "EUR": return value / 0.92;
            case "JPY": return value / 148.50;
            case "GBP": return value / 0.78;
            default:    return value; // USD
        }
    }

    double fromUSD(double usd, String unit) {
        switch (unit) {
            case "AUD": return usd * 1.55;
            case "EUR": return usd * 0.92;
            case "JPY": return usd * 148.50;
            case "GBP": return usd * 0.78;
            default:    return usd; // USD
        }
    }

    // FUEL EFFICIENCY & DISTANCE CONVERSION
    double convertFuel(double value, String from, String to) {
        if (from.equals(to)) return value;

        // convert everything to base unit first, then to target

        // fuel efficiency
        if (from.equals("mpg") && to.equals("km/L"))   return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg"))   return value / 0.425;

        // volume
        if (from.equals("Gallon (US)") && to.equals("Liters"))      return value * 3.785;
        if (from.equals("Liters") && to.equals("Gallon (US)"))      return value / 3.785;

        // distance
        if (from.equals("Nautical Mile") && to.equals("Kilometers")) return value * 1.852;
        if (from.equals("Kilometers") && to.equals("Nautical Mile")) return value / 1.852;
        return 0;
    }

    // TEMPERATURE CONVERSION
    double convertTemperature(double value, String from, String to) {
        // convert from source to Celsius first
        double celsius;
        if (from.equals("Fahrenheit")) {
            celsius = (value - 32) / 1.8;
        } else if (from.equals("Kelvin")) {
            celsius = value - 273.15;
        } else {
            celsius = value; // Celsius
        }

        // convert from Celsius to target
        if (to.equals("Fahrenheit")) {
            return (celsius * 1.8) + 32;
        } else if (to.equals("Kelvin")) {
            return celsius + 273.15;
        } else {
            return celsius; // Celsius
        }
    }
}