package com.example.peepdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.peepdemo.rocker.AimView;
import com.example.peepdemo.R;
import com.example.peepdemo.rocker.RockerView;

public class MainActivity extends AppCompatActivity implements RockerView.OnRockListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RockerView mRv = findViewById(R.id.rv);
        AimView mAv = findViewById(R.id.av);
        mRv.addOnRockListener(this);
        mRv.addOnRockListener(mAv);
    }

    @Override
    public void onRock(float ratioX, float ratioY) {
        Log.e("Rocker", (ratioX < 0 ? "Left" : "Right") + (ratioY < 0 ? ", Top" : ", Bottom") + ", RatioX : " + Math.abs(ratioX) + ", RatioY : " + Math.abs(ratioY));
    }

    @Override
    public void onReset() {
        Log.e("Rocker", "Reset");
    }
}
