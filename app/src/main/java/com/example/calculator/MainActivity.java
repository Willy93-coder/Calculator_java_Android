package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.calculator.databinding.ActivityMainBinding;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int result;
    private boolean isNewOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        result = 0;
        isNewOperation = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void buttonClicked(View view) {
        Button btn = (Button) view;
        String btnTag = btn.getTag().toString();
        String dataCalc = binding.tvResult.getText().toString();
        dataCalc = dataCalc + btnTag;
        boolean isDigit = Pattern.matches("[0-9]+", dataCalc);
        if (isDigit && isNewOperation) {
            System.out.println("Numero: " + btnTag);
            dataCalc = btnTag;
            isNewOperation = false;
        }
        binding.tvResult.setText(dataCalc);
        validateFirstNum(dataCalc);
        validateOperation(dataCalc);
    }

    // Validamos que el primer sea un num entre 0-9 para poder hacer una operacion
    private void validateFirstNum(String dataCalc) {
        String regexFirstNum = "^[0-9]+";
        boolean validateFirstNum = Pattern.matches(regexFirstNum, dataCalc);
        if (validateFirstNum) {
            enableOperationsButtons();
        } else {
            disableOperatorsButtons();
        }
    }

    // Validamos la construccion de la operacion
    private void validateOperation(String dataCalc) {
        String regex = "^[0-9]+[+\\-*/][0-9]$";
        boolean validateOperation = Pattern.matches(regex, dataCalc);
        if (validateOperation) {
            binding.equalButton.setEnabled(true);
        }
    }

    // Funcion para habilitar los botones de operacion
    private void enableOperationsButtons() {
        binding.divisionButton.setEnabled(true);
        binding.multiplyButton.setEnabled(true);
        binding.minusButton.setEnabled(true);
        binding.plusButton.setEnabled(true);
    }

    // Funcion para deshabilitar los botones de operacion
    private void disableOperatorsButtons() {
        binding.divisionButton.setEnabled(false);
        binding.multiplyButton.setEnabled(false);
        binding.minusButton.setEnabled(false);
        binding.plusButton.setEnabled(false);
    }

    // Funcion para resetear la operacion
    public void clearOperation(View view) {
        binding.tvResult.setText("");
        binding.equalButton.setEnabled(false);
        disableOperatorsButtons();
        isNewOperation = false;
    }

    // Funcion para mostrar el resultado
    public void showResult(View view) {
        if (Integer.parseInt(calcOperation()) < 0) {
            clearOperation(view);
        } else {
            binding.tvResult.setText(calcOperation());
            binding.equalButton.setEnabled(false);
            enableOperationsButtons();
            isNewOperation = true;
        }
    }

    // Funcion para calcular el resultado
    private String calcOperation() {
        String operation = binding.tvResult.getText().toString();
        boolean isSum = Pattern.matches("\\d+\\+\\d+", operation);
        boolean isSubtract = Pattern.matches("\\d+-\\d+", operation);
        boolean isMulti = Pattern.matches("\\d+\\*\\d+", operation);
        boolean isDiv = Pattern.matches("\\d+/\\d+", operation);
        String[] splitOperation = operation.split("[+\\-*/]");


        if (isSum) result = Integer.parseInt(splitOperation[0]) + Integer.parseInt(splitOperation[1]);
        if (isSubtract) {
            if (Integer.parseInt(splitOperation[1]) > Integer.parseInt(splitOperation[0])) {
                Toast.makeText(this, getString(R.string.toast_message), Toast.LENGTH_LONG).show();
                result = 0;
            } else {
                result = Integer.parseInt(splitOperation[0]) - Integer.parseInt(splitOperation[1]);
            }
        }
        if (isMulti) result = Integer.parseInt(splitOperation[0]) * Integer.parseInt(splitOperation[1]);
        if (isDiv) {
            if (Integer.parseInt(splitOperation[1]) != 0) {
                result = (Integer.parseInt(splitOperation[0]) / Integer.parseInt(splitOperation[1]));
            }
        }

        return String.valueOf(result);
    }


}