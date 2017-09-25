package mecha.id.a5minutecubes.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mecha.id.a5minutecubes.MainActivity;
import mecha.id.a5minutecubes.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static mecha.id.a5minutecubes.R.id.cubeLinearLayout;

public class PixelGridView extends View {
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint(),
            greenPaint = new Paint(),
            redPaint = new Paint(),
            blackLinePaint = new Paint(),
            thickBlackPaint = new Paint();
    private int[][] cellChecked;

    private int touchValue = 2;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setARGB(200,0,0,0);

        greenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greenPaint.setARGB(200,54,172,93);

        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setARGB(200,255,0,0);

        thickBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        thickBlackPaint.setStrokeWidth(4);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setTouchValue(String touchValueParam){
        switch (touchValueParam){
            case "SUPERPRODUCTIVE":
                touchValue = 3;
                break;
            case "PRODUCTIVE":
                touchValue = 2;
                break;
            case "UNPRODUCTIVE":
                touchValue = 1;
                break;
            default:
                touchValue = 2;
                break;
        }

    }

    public int[][] getCells(){
        return cellChecked;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("CUBE","tai bau");
        calculateDimensions();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }


        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        if(cellChecked == null){
            cellChecked = new int[numColumns][numRows];
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Log.i("CUBE","onDraw()"+(new Gson().toJson(cellChecked)));
        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]!=0) {
                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            getCubePaint(cellChecked[i][j]));
                }
            }
        }

        for (int i = 0; i <= numColumns; i++) {

            if(i==6){
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, thickBlackPaint);
            }else{
                canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackLinePaint);
            }
        }

        for (int i = 0; i <= numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackLinePaint);
        }
    }

    private Paint getCubePaint(int i) {
        Paint cubePaint = new Paint();

        switch(i){
            case 1:
                cubePaint = blackPaint;
                break;
            case 2:
                cubePaint = greenPaint;
                break;
            case 3:
                cubePaint = greenPaint;
                break;
            default:
                cubePaint = greenPaint;
                break;
        }

        return cubePaint;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            if(cellChecked[column][row]!=0){
                cellChecked[column][row] = 0;
            }else{
                cellChecked[column][row] = touchValue;
            }

            invalidate();
        }

        return true;
    }

    public void setCellChecked(int[][] cellCheckedParam) {
        cellChecked = cellCheckedParam;
        Log.i("CUBE","setCellChecked : "+(new Gson().toJson(cellChecked)));
    }
}