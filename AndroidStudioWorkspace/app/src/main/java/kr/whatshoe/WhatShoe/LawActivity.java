package kr.whatshoe.whatShoe;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class LawActivity extends AppCompatActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        type =  getIntent().getIntExtra("law",0);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(type);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_law, container, false);
            TextView lawText = (TextView)rootView.findViewById(R.id.law_text);
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            if(sectionNumber== 1) {
                Resources resources = getResources();
                if(resources!= null){
                    String uselaw = resources.getString(R.string.use_law);
                    lawText.setText(uselaw);
                    LinearLayout personTableLayout =(LinearLayout)rootView.findViewById(R.id.person_table_layout);
                    personTableLayout.setVisibility(View.GONE);
                }

            } else if(sectionNumber == 2){
                Resources resources = getResources();
                if(resources!= null){
                    String uselaw = resources.getString(R.string.person_law);

                    lawText.setText(uselaw);
                    LinearLayout personTableLayout =(LinearLayout)rootView.findViewById(R.id.person_table_layout);
                    personTableLayout.setVisibility(View.VISIBLE);
                }
            }
            return rootView;
        }
    }

}
