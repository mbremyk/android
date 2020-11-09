package com.example.e4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.lytFragments, new ListFragment(), "list");
        ft.add(R.id.lytFragments, new ImageFragment(), "images");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(0, R.id.previous, 0, R.string.previous);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mi = menu.add(0, R.id.next, 0, R.string.next);
        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.previous:
                ((ImageFragment)getFragmentManager().findFragmentByTag("images")).previous();
                break;
            case R.id.next:
                ((ImageFragment)getFragmentManager().findFragmentByTag("images")).next();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListClick(int index) {
        ImageFragment f = (ImageFragment) getFragmentManager().findFragmentByTag("images");
        f.setImageAndDescription(index);
    }
}