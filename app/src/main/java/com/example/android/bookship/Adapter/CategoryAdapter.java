package com.example.android.bookship.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.android.bookship.Fragment.FavouriteFragment;
import com.example.android.bookship.Fragment.GenresFragment;
import com.example.android.bookship.Fragment.OnDeviceFragment;
import com.example.android.bookship.Fragment.SearchFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    public CategoryAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new GenresFragment();
        } else if(position == 1) {
            return new SearchFragment();
        } else if(position == 2){
            return new FavouriteFragment();
        } else{
            return new OnDeviceFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
          return "Genres";
        } else if(position == 1){
            return "Search";
        } else if(position == 2){
            return "Favourites";
        } else{
            return "On Device";
        }
    }
}
