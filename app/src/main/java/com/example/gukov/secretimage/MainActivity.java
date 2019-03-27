package com.example.gukov.secretimage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    DrawView mDrawView; // класс drawview

    Button btn_1, btn_2, btn_3, btn_4; // кнопки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // находим элементы
        mDrawView = (DrawView) findViewById(R.id.drawView);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);

        // устанавливаем слушатели
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
    }


    // действия по нажатию кнопок
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1: // кнопка 1 "Показать_быстро"
                mDrawView.setTransparent();
                chaneEnabledButton(0);
                break;
            case R.id.btn_2: // кнопка 2 "Скрыть_быстро"
                mDrawView.setOpaque();
                chaneEnabledButton(1);
                break;
            case R.id.btn_3: // кнопка 3 "Показать_плавно"
                mDrawView.setFadeOut();
                chaneEnabledButton(0);
                break;
            case R.id.btn_4: // кнопка 4 "Скрыть_плавно"
                mDrawView.setFadeIn();
                chaneEnabledButton(1);
                break;
            default:
                break;
        }
    }


    // смена доступности кнопок
    private void chaneEnabledButton(int direct) {
        if (direct == 0) {
            btn_1.setEnabled(false);
            btn_3.setEnabled(false);
            btn_2.setEnabled(true);
            btn_4.setEnabled(true);
        } else {
            btn_1.setEnabled(true);
            btn_3.setEnabled(true);
            btn_2.setEnabled(false);
            btn_4.setEnabled(false);
        }
    }

}