package com.e.lesson_4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class FormActivity extends AppCompatActivity {
    public static final String RESULT_KEY = "task";
    private EditText editTitle;
    private EditText editDesc;
    private  Task task;
    boolean isShow=false;
    Button button_red;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTitle=findViewById(R.id.editTitle);
        editDesc=findViewById(R.id.editDesc);
        button_red=findViewById(R.id.button_red);
        task = (Task) getIntent().getSerializableExtra(RESULT_KEY);
        if (task!=null) {
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDesc());
            isShow = true;
           button_red.setText("Редактировать");


        }


    }

//    protected void onPause() {
//       super.onPause();
//     SharedPreferences.Editor settings = getSharedPreferences("preferences", MODE_PRIVATE).edit();
//        settings.putString("title", editTitle.getText().toString());
//        settings.putString("desc", editDesc.getText().toString());
//      settings.apply();
// }
//
//    @Override
//   protected void onResume() {
//        SharedPreferences sharedPreferences = getSharedPreferences("preferences1", MODE_PRIVATE);
//       editTitle.setText(sharedPreferences.getString("title", ""));
//       String desc = sharedPreferences.getString("desc", "");
//        editDesc.setText(desc);
//        super.onResume();
//    }





    public void onSave(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        if(task!=null) {
            task.setTitle(title);
            task.setDesc(desc);
            isShow=true;

            App.getDatabase().taskDao().update(task);


        }else {
            task=new Task(title,desc);
            App.getDatabase().taskDao().insert(task);
            setResult(RESULT_OK);
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(findViewById(R.id.editTitle));
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(5)
                    .playOn(findViewById(R.id.editDesc));

        }
         if ( title.matches("")) {
             Toast.makeText(this, "Поля пустая ", Toast.LENGTH_SHORT).show();
             return;
         }
         finish();
         }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
