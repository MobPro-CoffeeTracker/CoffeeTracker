package ch.hslu.mobpro.coffeetracker.fragments;


import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hslu.mobpro.coffeetracker.R;
import ch.hslu.mobpro.coffeetracker.coffee.CoffeeService;
import ch.hslu.mobpro.coffeetracker.coffee.ICoffeeManager;

public class Table_fragment extends Fragment {

    private ICoffeeManager coffeeManager;
    private CoffeeConnecter coffeeConnecter = new CoffeeConnecter();

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader = new ArrayList<>();
    private HashMap<String, List<String>> listDataChild = new HashMap<>();


    public Table_fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent coffee = new Intent(getActivity(), CoffeeService.class);
        getActivity().startService(coffee);
        getActivity().bindService(coffee, coffeeConnecter, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(coffeeConnecter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        View view = inflater.inflate(R.layout.table_fragment, container, false);

        Button load = (Button) view.findViewById(R.id.load_coffee);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareListData();
                listAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        expListView = (ExpandableListView) getActivity().findViewById(R.id.coffee_list_view);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        Map<Date, Location> coffee = coffeeManager.getAllCoffee();

        HashMap<String, List<String>> map = new HashMap<>();

        Iterator it = coffee.keySet().iterator();
        while (it.hasNext()) {
            Date date = (Date) it.next();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String key = formatter.format(date);

            if (!map.containsKey(key)) {
                map.put(key, new LinkedList<String>());
            }
            map.get(key).add(coffee.get(date).toString());
        }

        listDataHeader.clear();
        listDataChild.clear();

        listDataHeader.addAll(map.keySet());
        listDataChild.putAll(map);
    }

    private class CoffeeConnecter implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            coffeeManager = ((CoffeeService.CoffeeBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
