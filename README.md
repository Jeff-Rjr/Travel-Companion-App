# Travel-Companion-App

A simple Android application that helps international travellers convert essential values across three categories — **Currency**, **Fuel Efficiency**, and **Temperature**.

## Overview

The Travel Companion App was built as a mobile application development exercise using **Android Studio Panda 2**. The app allows users to select a conversion category, choose source and destination units, enter a value, and instantly see the converted result.

---

## Features

- **Currency Conversion** — Convert between USD, AUD, EUR, JPY, and GBP using fixed 2026 rates
- **Fuel Efficiency & Distance Conversion** — Convert between mpg, km/L, Gallons, Liters, Nautical Miles, and Kilometers
- **Temperature Conversion** — Convert between Celsius, Fahrenheit, and Kelvin

---

## Conversion Reference

### Currency (Fixed 2026 Rates)

| From | To | Rate |
|------|----|------|
| 1 USD | AUD | × 1.55 |
| 1 USD | EUR | × 0.92 |
| 1 USD | JPY | × 148.50 |
| 1 USD | GBP | × 0.78 |

> All currency conversions use USD as the base. The app converts the input to USD first, then to the destination currency.

---

### Fuel Efficiency & Distance

| From | To | Factor |
|------|----|--------|
| 1 mpg | km/L | × 0.425 |
| 1 Gallon (US) | Liters | × 3.785 |
| 1 Nautical Mile | Kilometers | × 1.852 |

> Note: Cross-category conversions (e.g. mpg to Liters) are not supported and will return 0.

---

### Temperature

| Conversion | Formula |
|------------|---------|
| Celsius → Fahrenheit | F = (C × 1.8) + 32 |
| Fahrenheit → Celsius | C = (F − 32) / 1.8 |
| Celsius → Kelvin | K = C + 273.15 |

> All temperature conversions use Celsius as the intermediate base unit.

## How It Works

### 1. Category Selection
When the user selects a category from `spinnerCategory`, the `updateUnitSpinners()` method is called automatically via `OnItemSelectedListener`. This reloads the `spinnerFrom` and `spinnerTo` dropdowns with the appropriate unit options for that category.

### 2. Conversion Flow
When the Convert button is pressed, `doConversion()` is called:

```
User presses Convert
        ↓
Read input value from EditText
        ↓
Check if From and To units are the same → display input as-is
        ↓
Call the appropriate conversion method based on category
        ↓
Display result formatted to 4 decimal places
```

### 3. Conversion Methods

**Currency — `convertCurrency()`**
Uses a two-step USD base approach:
```
Input → toUSD() → fromUSD() → Result
```

**Fuel — `convertFuel()`**
Uses direct if-statement pair matching:
```
if (from == "mpg" && to == "km/L") → multiply by 0.425
```

**Temperature — `convertTemperature()`**
Uses a two-step Celsius base approach:
```
Input → convert to Celsius → convert to target unit → Result
```

## Notes

- Currency rates are **fixed** for this task and do not reflect live exchange rates
- Cross-category fuel conversions (e.g. mpg → Liters) are not supported and will return `0.0000`
- The `EditText` is restricted to `numberDecimal` input type, so only numerical values can be entered
