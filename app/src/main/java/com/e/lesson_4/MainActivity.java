package com.e.lesson_4;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.lesson_4.auth.PhoneActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TaskAdapter adapter;
    List<Task> list;
    File file;
    Context context;
    String message;
    String sure;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isShown = preferences.getBoolean("isShown", false);

        if (!isShown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
            return;
        }
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(new Intent(this, PhoneActivity.class));
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, FormActivity.class), 100);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerview=navigationView.getHeaderView(0);
        View header=headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        ImageView imageView=headerview.findViewById(R.id.imageView);
        InitList();
        initFile();
    }


    @AfterPermissionGranted(199)
    private void initFile() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            final File folder = new File(Environment.getExternalStorageDirectory(), "Asina/Media");
            if (!folder.exists()) folder.mkdirs();
            File file = new File(folder, "task.txt");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(folder, "image.jpg");
                    try {
                        FileUtils.copyURLToFile(new URL("https://klike.net/uploads/posts/2019-01/1547368042_15.jpg"), file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else {
            EasyPermissions.requestPermissions(this, "Для записи нужно разрешение",
                    199, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void InitList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.e("TAG", "onItemClicked");
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("task", list.get(pos));
                startActivityForResult(intent, 100);
            }

            @Override
            public void onItemLongClick(int pos) {
                Log.e("TAG", "onItemLongClicked");
                showAlert(list.get(pos));
            }
        });
    }

    private void showAlert(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы хотите удалить")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        list.remove(task);
                        App.getDatabase().taskDao().delete(task);
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 100) {
            list.clear();
            list.addAll(App.getDatabase().taskDao().getAll());
            adapter.notifyDataSetChanged();

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sort) {
            Collections.sort(list, new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    return task.getTitle().compareTo(t1.getTitle());
                }
            });
            adapter.notifyDataSetChanged();
        }



            if (id == R.id.Settings) {
                SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
                preferences.edit().clear().commit();

                adapter.list.clear();


                adapter.notifyDataSetChanged();
                return true;


            }else if(id==R.id.action_sign_out){
                FirebaseAuth.getInstance().signOut();
                finish();
            }
            return super.onOptionsItemSelected(item);
        }
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {
                startActivity(new Intent(this, LottieActivity.class));


            } else if (id == R.id.nav_tools) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public void onRequestPermissionsResult ( int requestCode, String[] permissions,
        int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }


    }
