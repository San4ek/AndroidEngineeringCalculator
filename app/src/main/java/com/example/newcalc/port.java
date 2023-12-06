package com.example.newcalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class port extends AppCompatActivity {

    private static final StringBuilder str=new StringBuilder();

    private EditText text;
    private TextView result;

    private int selection=0;

    private final Map<String, String> history=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port);

        text=findViewById(R.id.text);
        result=findViewById(R.id.result);

        text.setShowSoftInputOnFocus(false);

        findViewById(R.id.one_button).setOnClickListener(v -> onCLick("1"));

        findViewById(R.id.two_button).setOnClickListener(v -> onCLick("2"));

        findViewById(R.id.three_button).setOnClickListener(v -> onCLick("3"));

        findViewById(R.id.four_button).setOnClickListener(v -> onCLick("4"));

        findViewById(R.id.five_button).setOnClickListener(v -> onCLick("5"));

        findViewById(R.id.six_button).setOnClickListener(v -> onCLick("6"));

        findViewById(R.id.seven_button).setOnClickListener(v -> onCLick("7"));

        findViewById(R.id.eight_button).setOnClickListener(v -> onCLick("8"));

        findViewById(R.id.nine_button).setOnClickListener(v -> onCLick("9"));

        findViewById(R.id.zero_button).setOnClickListener(v -> onCLick("0"));

        findViewById(R.id.procent_button).setOnClickListener(v -> onCLick("%"));

        findViewById(R.id.del_button).setOnClickListener(v -> onCLick("/"));

        findViewById(R.id.plus_button).setOnClickListener(v -> onCLick("+"));

        findViewById(R.id.minus_button).setOnClickListener(v -> onCLick("-"));

        findViewById(R.id.mul_button).setOnClickListener(v -> onCLick("*"));

        findViewById(R.id.dot_button).setOnClickListener(v -> {
            int length=str.length();
            if (length!=0) {
                char ch = str.charAt(length - 1);
                if (ch != '/' && ch != '.' && ch != '%' && ch != '+' && ch != '-' && ch != '*') {
                    onCLick(".");
                }
            }
        });

        findViewById(R.id.clr_button).setOnClickListener(v -> {
            str.replace(0,str.length(),"");
            selection=0;
            result.setText("");
            text.setText("");
        });

        findViewById(R.id.delete_button).setOnClickListener(v -> {
            if (text.hasFocus()) {
                selection=text.getSelectionStart();
                if (selection>0) {
                    str.deleteCharAt(--selection);
                    parse();
                    text.setSelection(selection);
                }
            } else {
                int strLength=str.length();
                if (strLength>0) {
                    str.deleteCharAt(str.length() - 1);
                    parse();
                    text.setSelection(str.length());
                }
            }

        });

        findViewById(R.id.res_button).setOnClickListener(v -> {
            if (!str.toString().equals("")) {
                String res=result.getText().toString();
                history.put(str.toString(), result.getText().toString());
                str.replace(0,str.length(),res);
                text.setText(res);
                Toast.makeText(this, "Saved to history", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parse() {
        Expression expression=new Expression(str.toString());
        try {
            result.setText("");
            result.setText(expression.evaluate().getStringValue());
        } catch (EvaluationException | ParseException | ArithmeticException e) {
            result.setText(e.getMessage());
        } catch (NumberFormatException ignored) {

        } finally {
            text.setText(str);
        }
    }

    private void onCLick(String s) {
        if (text.hasFocus()) {
            selection = getSelection();
        } else {
            selection=str.length();
        }
        str.insert(selection,s);
        parse();
        text.setSelection(selection+1);
    }

    private int getSelection() {
        return text.getSelectionStart();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("text", str.toString());
        outState.putString("result",result.getText().toString());
        outState.putString("history",history.toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        text.setText(Objects.requireNonNull(savedInstanceState.getString("text")));
        result.setText(Objects.requireNonNull(savedInstanceState.getString("result")));

        super.onRestoreInstanceState(savedInstanceState);
    }
}