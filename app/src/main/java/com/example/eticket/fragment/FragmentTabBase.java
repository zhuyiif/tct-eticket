package com.example.eticket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.eticket.ListViewAdapter;
import com.example.eticket.Person;
import com.example.eticket.R;
import com.example.eticket.Utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class FragmentTabBase extends Fragment {

    public ListView listView;


    @Override
    public void onStart() {
        super.onStart();


    }


    @Nullable
    public List<Person> readListFromFile(){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getActivity().getAssets()
                    .open("persons.txt")));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(stringBuilder.toString(), new TypeToken<List<Person>>(){}
                    .getType());
        }catch (IOException exception){
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }
}
