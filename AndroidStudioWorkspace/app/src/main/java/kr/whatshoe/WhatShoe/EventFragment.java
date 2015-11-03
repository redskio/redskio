package kr.whatshoe.whatShoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment implements View.OnClickListener {


    public EventFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        ImageButton imageButton4 = (ImageButton)rootView.findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton4:
                getActivity().getSupportFragmentManager().beginTransaction().replace(kr.whatshoe.whatShoe.R.id.container, new EventDetailFragment()).commit();
                break;
        }
    }
}
