package com.example.gukov.secretimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class DrawView extends View {

    private static final int MAX_COUNT = 1728; // см описание mItemPic
    private static int FADEOUT = 0;
    private static int FADEIN = 1;

    private Paint p;
    private Rect mRect;

    private boolean viewFlg = true;

    private Bitmap bitmap;

    private int mOpacity = 255; // прозрачность, 255 полостью непрозрачный

    private int mWidth; // ширина View;
    private int mHeight; // высота View;

    private int direct; // направление
    private int mItemPic = 0; // сколько обработать блоков


    public DrawView(Context context) {
        super(context);
    }


    // определяем битмап в конструкторе
    // андройд из файла 240x320 делает битмап в двое большего размера по пикселям
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        p = new Paint();
        mOpacity = 255;
        mRect = new Rect(0,940,20,960);
        // фоновая картинка
        Bitmap bitmap_original = BitmapFactory.decodeResource(getResources(), R.drawable.paris);
        // получаем картинку
        bitmap = Bitmap.createScaledBitmap(bitmap_original, 720, 960, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // размер view
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        // фоновая заливка
        canvas.drawARGB(80,120,240,320);

        p.setColor(Color.RED);
        canvas.drawBitmap(bitmap,0,0,p);

        // покрывающий квадрат
        p.setStyle(Paint.Style.FILL);
        p.setARGB(mOpacity,0,211,255);
        if (viewFlg) {
            fullFill(canvas);
        } else {
            fullOpacity(canvas,direct);
        }
    }


    // сплошная быстрая заливка
    private void fullFill(Canvas canvas) {
        for (int i = 1; i <= 36; i++) {
            for (int j = 48; j >= 1; j--) {
                canvas.drawRect(mRect,p);
                canvas.translate(0, -20);
            }
            canvas.translate(20, 960);
        }
    }


    // сплошная заливка со сдвигом
    private void fullOpacity(Canvas canvas,int direct) {
        //int opacity = mOpacity;
        int opacity = 0;
        if (direct == FADEIN) opacity = 255;
        if (direct == FADEOUT) opacity = 0;

        int stopId = 0; // параметр останавливающий цикл
        for (int i = 1; i <= mWidth / 20; i++) {
            for (int j = mHeight / 20; j >= 1; j--) {
                p.setARGB(opacity, 0, 211, 255);
                canvas.drawRect(mRect,p);
                canvas.translate(0, -20);
                // если сдвиг больше чем обрабатываемый элемент
                // то в зависимости от направления остальные квадраты рисуем прозрачными или закрашеными
                if ((stopId > mItemPic) && (direct == FADEOUT)) {
                    opacity = 255;
                }
                if ((stopId > mItemPic)  && (direct == FADEIN)) {
                    opacity = 0;
                }

                stopId++;
            }
            canvas.translate(20, 960);
        }
    }


    // непрозрачный
    public void setOpaque() {
        // ставим альфу в 255 - т.к. непрозрачный цвет
        mOpacity = 255;
        invalidate();
    }


    // прозрачный
    public void setTransparent() {
        // ставим альфу в 0 - т.к. прозрачный цвет
        mOpacity = 0;
        invalidate();
    }


    // делаем прозрачной
    public void setFadeOut() {
        viewFlg = false;
        direct = FADEOUT;
        mOpacity = 0;
        new MyTack(FADEOUT).execute();

    }


    // делаем непрозрачной
    public void setFadeIn() {
        viewFlg = false;
        direct = FADEIN;
        mOpacity = 255;
        new MyTack(FADEIN).execute();
    }


    // меняем картинку с помошью асинхронного потока
    class MyTack extends AsyncTask<Void,Void,Void> {
        private int direct; // направление изменения прозрачности
        public MyTack(int direct){
            this.direct = direct;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (mItemPic < MAX_COUNT){
                mItemPic++;
                try {
                    TimeUnit.MICROSECONDS.sleep(6500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // обновляем view
            // вызываем обновление в основном потоке
            invalidate();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(),"Операция завершена",Toast.LENGTH_SHORT).show();
            viewFlg = true;
            mItemPic = 0;
        }
    }

}