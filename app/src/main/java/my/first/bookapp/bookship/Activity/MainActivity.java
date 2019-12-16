package my.first.bookapp.bookship.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import my.first.bookapp.bookship.Adapter.ViewPagerAdapter;
import my.first.bookapp.bookship.Fragment.FavouriteFragment;
import my.first.bookapp.bookship.Fragment.GenresFragment;
import my.first.bookapp.bookship.Fragment.OnDeviceFragment;
import my.first.bookapp.bookship.Fragment.SearchFragment;
import com.first.bookapp.bookship.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private MenuItem prevMenuItem;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager);

        final BottomNavigationView bottomView = findViewById(R.id.bottom_navigation);
        bottomView.setOnNavigationItemSelectedListener(navListner);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(prevMenuItem != null){
                    prevMenuItem.setChecked(false);
                } else {
                    bottomView.getMenu().getItem(0).setChecked(false);
                }
                bottomView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_genres:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.nav_search:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.nav_favourites:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.nav_ondevice:
                    viewPager.setCurrentItem(3);
                    break;
            }

            return false;
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GenresFragment());
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(new FavouriteFragment());
        adapter.addFragment(new OnDeviceFragment());
        viewPager.setAdapter(adapter);
    }
}
