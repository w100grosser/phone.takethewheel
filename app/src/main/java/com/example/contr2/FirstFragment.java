package com.example.contr2;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.contr2.databinding.ActivityMainBinding;
import com.example.contr2.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;


import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    UDP server;
    ExecutorService exec;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentFirstBinding.inflate(inflater, container, false);

        MainActivity activityMain = (MainActivity) getActivity();
        {
//        assert activityMain != null;
            server = activityMain.server;
            exec = activityMain.exec;
        }
        EditText mEdit   = (EditText) binding.textInputEditText;
        EditText mEdit1   = (EditText) binding.textInputEditText1;
        EditText mEdit2   = (EditText) binding.textInputEditText2;
        Button clickButton = (Button) binding.button;
        Button clickButton1 = (Button) binding.button1;
        JoystickView joystick = (JoystickView) binding.joystickView;
        byte[] data={1,127,127};

        mEdit.setText("192.168.1.63");
        mEdit1.setText("1");

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // do whatever you want
//                server.send("trash");
//                byte[] data = {(byte) axis};
//                System.out.println(axis);
                if(mEdit1.getText().toString().equals("0")){
                    int axis = (int)(((Math.cos(Math.toRadians((double)angle)) * Math.abs((double)strength)) + 100)/200.0 * 8000.0);

                    byte[] data = {(byte)0,
                            (byte)((axis >> 8) & 0xff),
                            (byte)((axis >> 0) & 0xff)};
                    Runnable task = new Runnable(){
                        public void run(){
                            server.send(data );
                            Snackbar.make(binding.getRoot(), "server.send(data )", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    };
                    exec.submit(task);
                }else{
                    int axis = (int)(((Math.cos(Math.toRadians((double)angle)) * Math.abs((double)strength)) + 100)/200.0 * 255.0);
                    byte[] data = {(byte)1 , (byte) (axis)};
                    Runnable task = new Runnable(){
                        public void run(){
                            server.send(data );

                            Snackbar.make(binding.getRoot(), "server.send(data )", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    };
                    exec.submit(task);
                }
//                ((RunnableImpl) task).server1 = server;
//                ((RunnableImpl) task).v = "trash";
//                exec.submit(server.send("trash".getBytes()));
            }

        });


        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String host = mEdit.getText().toString();
                Integer port = Integer.valueOf(mEdit2.getText().toString())  ;
                Snackbar.make(binding.getRoot(), (String.valueOf(v.getId()) ), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                activityMain.server = new UDP(host, port, 50);
                server = activityMain.server;
            }
        });


        clickButton1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String host = mEdit.getText().toString();
                Snackbar.make(binding.getRoot(), (String.valueOf(v.getId()) ), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                activityMain.navController.navigate(R.id.action_FirstFragment_to_SecondFragment);

            }
        });

        return binding.getRoot();



    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}