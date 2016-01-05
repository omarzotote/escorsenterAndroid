package encalient.es.scorecenter.Common.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

import encalient.es.scorecenter.R;
import es.encalient.ProtoMatchDTO;


public class MatchMapFragment extends SupportMapFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static MatchMapFragment newInstance(ProtoMatchDTO.MatchDTO match) {
        MatchMapFragment fragment = new MatchMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.match_map_fragment, container);
        return fragmentView;
    }
}
