package top.iwill.testmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import view.SDKTestActivity;

/**
 * @author Dev.Zhou
 * @date 2018/6/12
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mJumpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initListeners();
    }

    private void findViews() {
        mJumpBtn = findViewById(R.id.jump);
    }

    private void initListeners() {
        mJumpBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SDKTestActivity.class));
    }
}
