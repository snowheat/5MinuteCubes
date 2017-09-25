package mecha.id.a5minutecubes;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.List;

import mecha.id.a5minutecubes.views.PixelGridView;

import static mecha.id.a5minutecubes.R.id.cubeLinearLayout;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "DailyCube";
    public int[][] cellChecked;
    private PixelGridView cubePixelGridView;
    private TextView productivityTextView,remainingCubesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("CUBE","onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cubePixelGridView = (PixelGridView) new PixelGridView(this);
        cubePixelGridView.setNumColumns(12);
        cubePixelGridView.setNumRows(20);

        //Restore preferences

        SharedPreferences dailyCubes = getSharedPreferences(PREFS_NAME,0);
        String dailyCubesDataJson = dailyCubes.getString("dailyCubesData","");

        if(dailyCubesDataJson == null){
            Log.e("CUBE","dailyCubesDataJson null");
        }

        if(dailyCubesDataJson == ""){
            Log.e("CUBE","dailyCubesDataJson ''");
        }

        Log.i("CUBE","Loaded daily cube data"+dailyCubesDataJson);
        int[][] dailyCubesDataArray = new Gson().fromJson(dailyCubesDataJson,int[][].class);
        Log.i("CUBE","become "+(new Gson().toJson(dailyCubesDataArray)));

        if(dailyCubesDataArray == null){
            Log.e("CUBE","dailyCubesDataArray null");
        }

        if(dailyCubesDataArray!=null){
            cubePixelGridView.setCellChecked(dailyCubesDataArray);
        }

        LinearLayout cubeLinearLayout = (LinearLayout) findViewById(R.id.cubeLinearLayout);
        cubeLinearLayout.addView(cubePixelGridView);

        cellChecked = cubePixelGridView.getCells();

        productivityTextView = (TextView) findViewById(R.id.productivityTextView);
        remainingCubesTextView = (TextView) findViewById(R.id.remainingCubesTextView);

        calculateProductivity();
        calculateRemainingCubes();

        cubePixelGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                calculateProductivity();
                calculateRemainingCubes();
                return false;
            }
        });

        Button productiveButton = (Button) findViewById(R.id.productiveButton);
        productiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cubePixelGridView.setTouchValue("PRODUCTIVE");
                cellChecked = cubePixelGridView.getCells();
            }

        });

        Button unProductiveButton = (Button) findViewById(R.id.unproductiveButton);
        unProductiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cubePixelGridView.setTouchValue("UNPRODUCTIVE");
                cellChecked = cubePixelGridView.getCells();
            }
        });




    }

    private void calculateRemainingCubes() {
        int remainingCubes = 0;

        for(int i=0;i<cubePixelGridView.getNumColumns();i++){
            for(int j=0;j<cubePixelGridView.getNumRows();j++){
                if (cellChecked[i][j]==0) {
                    remainingCubes += 1;
                }
            }
        }

        remainingCubesTextView.setText("Sisa "+String.valueOf(remainingCubes)+" cube");
        Log.i("CUBE","calculateRemainingCubes()");
    }

    private void calculateProductivity() {
        int productivity = 0,
        cubesUsed = 0,
        productivityCubesUsed =0;


        for(int i=0;i<cubePixelGridView.getNumColumns();i++){
            for(int j=0;j<cubePixelGridView.getNumRows();j++){
                if (cellChecked[i][j]!=0) {
                    cubesUsed += 1;
                    if(cellChecked[i][j]==2||cellChecked[i][j]==3){
                        productivityCubesUsed += 1;
                    }
                }
            }
        }

        productivity = Math.round( ((float)productivityCubesUsed/(float)cubesUsed*100) );


        productivityTextView.setText("Prod. : "+String.valueOf(productivity)+"%");
        Log.i("CUBE","calculateProductivity()");
    }

    @Override
    protected void onStart() {

        Log.i("CUBE","onStart()");

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cubePixelGridView.setCellChecked(cubePixelGridView.getCells());
        Log.i("CUBE","onResume() "+(new Gson().toJson(cubePixelGridView.getCells())));

    }

    @Override
    protected void onStop() {

        Log.i("CUBE","onStop()");
        saveDailyCubesData();
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        Log.i("CUBE","onBackPressed()");
        saveDailyCubesData();
        super.onBackPressed();
    }

    private void saveDailyCubesData(){
        String dailyCubesDataJsonString = new Gson().toJson(cellChecked);

        SharedPreferences dailyCubes = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = dailyCubes.edit();
        editor.putString("dailyCubesData",dailyCubesDataJsonString).apply();

        Log.i("CUBE",dailyCubesDataJsonString);

        dailyCubes = getSharedPreferences(PREFS_NAME,0);
        dailyCubesDataJsonString = dailyCubes.getString("dailyCubesData","");

        Log.i("CUBE","Saved daily cube data"+dailyCubesDataJsonString);
    }
}
