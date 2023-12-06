package com.example.newcalc;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class main extends AppCompatActivity {

    private static final StringBuilder str=new StringBuilder();

    private EditText text;
    private TextView result;

    private int selection=0;

    private final Map<String, String> history=new HashMap<>();

    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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

        findViewById(R.id.zero_button).setOnClickListener(v -> {
            int pos = text.getSelectionStart();
            System.out.println(pos);
            if (pos == 0) {
                int length = str.length();
                if (pos < length) {
                    char ch = str.charAt(pos);
                    if (ch != '0') {
                        onCLick("0");
                    }
                } else if (pos == length) {
                    onCLick("0");
                }
            } else {
                char ch;
                boolean isNoNull = false;
                while (pos > 0 && ((ch = str.charAt(pos - 1)) >= '0' && ch <= '9' || ch == '.')) {
                    System.out.println(ch);
                    if (ch != '0') {
                        isNoNull = true;
                        break;
                    }
                    --pos;
                }
                if (isNoNull || pos==str.length()) {
                    onCLick("0");
                }
            }
        });

        findViewById(R.id.procent_button).setOnClickListener(v -> onCLick("%"));

        findViewById(R.id.del_button).setOnClickListener(v -> check("/"));

        findViewById(R.id.plus_button).setOnClickListener(v -> check("+"));

        findViewById(R.id.minus_button).setOnClickListener(v -> check("-"));

        findViewById(R.id.mul_button).setOnClickListener(v -> check("*"));

        if (this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE) {

            findViewById(R.id.cos_button).setOnClickListener(v -> {
                ++count;
                onCLick("cos(");
            });

            findViewById(R.id.sin_button).setOnClickListener(v -> {
                ++count;
                onCLick("sin(");
            });

            findViewById(R.id.left_bracket_button).setOnClickListener(v -> {
                ++count;
                onCLick("(");
            });

            findViewById(R.id.right_bracket_button).setOnClickListener(v -> {
                if (count>0) {
                    --count;
                    onCLick(")");
                }
            });

            findViewById(R.id.tg_button).setOnClickListener(v -> {
                ++count;
                onCLick("tan(");
            });

            findViewById(R.id.pow_button).setOnClickListener(v -> onCLick("^"));

            findViewById(R.id.log_button).setOnClickListener(v -> {
                ++count;
                onCLick("log(");
            });

            findViewById(R.id.e_button).setOnClickListener(v -> {
                int pos = text.getSelectionStart();
                if (pos > 0) {
                    char ch = str.charAt(pos-1);
                    if (ch != '0' &&
                            ch != '1' &&
                            ch != '2' &&
                            ch != '3' &&
                            ch != '4' &&
                            ch != '5' &&
                            ch != '6' &&
                            ch != '7' &&
                            ch != '8' &&
                            ch != '9' &&
                            ch != '%' &&
                            ch != '.' &&
                            ch != 'e' &&
                            ch != 'π' &&
                            ch!=')') {
                        onCLick("e");
                    }
                } else {
                    onCLick("e");
                }
            });

            findViewById(R.id.pi_button).setOnClickListener(v -> onCLick("π"));

            findViewById(R.id.history_button).setOnClickListener(v -> {});
        }

        findViewById(R.id.dot_button).setOnClickListener(v -> {
            int pos = text.getSelectionStart();
            System.out.println(pos);
            int min=pos;
            int max=pos;
            boolean isDot=false;
            while (min>=1 && ((str.charAt(min-1)>='0' && str.charAt(min-1)<='9') || str.charAt(min-1)=='.')) {
                if (str.charAt(min-1)=='.') {
                    isDot = true;
                    break;
                }
                --min;
            }
            while (max<=str.length()-1 && ((str.charAt(max)>='0' && str.charAt(max)<='9') || str.charAt(max)=='.')) {
                if (str.charAt(max)=='.') {
                    isDot = true;
                    break;
                }
                ++max;
            }
            if (!isDot) {
                onCLick(".");
            }
        });

        findViewById(R.id.clr_button).setOnClickListener(v -> {
            str.replace(0,str.length(),"");
            selection=0;
            count=0;
            result.setText("");
            text.setText("");
        });

        findViewById(R.id.delete_button).setOnClickListener(v -> {
            if (text.hasFocus()) {
                selection=text.getSelectionStart();
                if (selection>0) {
                    if (str.charAt(--selection)=='(') {
                        --count;
                    } else if (str.charAt(selection)==')') {
                        ++count;
                    }
                    str.deleteCharAt(selection);
                    parse();
                    text.setSelection(selection);
                }
            } else {
                int strLength=str.length();
                if (strLength>0) {
                    int pos=str.length()-1;
                    if (str.charAt(pos)=='(') {
                        --count;
                    } else if (str.charAt(pos)==')') {
                        ++count;
                    }
                    str.deleteCharAt(pos);
                    parse();
                    text.setSelection(str.length());
                }
            }

        });

        findViewById(R.id.res_button).setOnClickListener(v -> {
            if (!str.toString().equals("") && !result.getText().toString().equals("Error")) {
                String res=result.getText().toString();
                history.put(str.toString(), result.getText().toString());
                str.replace(0,str.length(),res);
                text.setText(res);
                Toast.makeText(this, "Saved to history", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void parse() {
        Expression expression=new Expression(str.toString().replace("π","pi").replace("%","/100"));
        try {
            result.setText("");
            double value = expression.evaluate().getNumberValue().doubleValue();
            int i=0;
            while (value>999999) {
                value /= 10;
                ++i;
            }
            if (i==0) {
                result.setText(String.valueOf(value));
            } else {
                result.setText(Math.round(value)+"*10^"+i);
            }
        } catch (EvaluationException | ParseException | ArithmeticException ignored) {
            result.setText("Error");
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

    private void check(String s) {
        int length=str.length();
        if (length==0 && s.equals("-")) {
            onCLick("-");
        } else if (length!=0) {
            char ch = str.charAt(length - 1);
            if (ch != '/' && ch != '.' && ch != '%' && ch != '+' && ch != '-' && ch != '*' && ch!='(') {
                onCLick(s);
            }
        }
    }
}
