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

        return inflater.inflate(R.layout.table_fragment, container, false);
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
        DateFormat headerFormatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat entryFormatter = new SimpleDateFormat("HH:mm:ss");

        Iterator it = coffee.keySet().iterator();
        while (it.hasNext()) {
            Date date = (Date) it.next();

            String key = headerFormatter.format(date);

            if (!map.containsKey(key)) {
                map.put(key, new LinkedList<String>());
            }
            String string = entryFormatter.format(date) + ": " + coffee.get(date).getLatitude() + "°N /" + coffee.get(date).getLongitude() + "°E";
            map.get(key).add(string);
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
            prepareListData();
            listAdapter.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    }
}
