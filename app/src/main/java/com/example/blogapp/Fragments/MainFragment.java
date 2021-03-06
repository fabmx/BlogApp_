package com.example.blogapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blogapp.Adapters.CourtAdapter;
import com.example.blogapp.Models.Court;
import com.example.blogapp.R;



import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView rvCourts;
    CourtAdapter courtAdapter;
    List<Court> mData;

    public MainFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences appSettingsPref = this.getActivity().getSharedPreferences("AppSettingsPref", 0);
        Boolean isNightModeOn = appSettingsPref.getBoolean("NightMode", false);

        if(isNightModeOn){

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        rvCourts = fragmentView.findViewById(R.id.rv_court);
        rvCourts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCourts.setHasFixedSize(true);

        initmDataCourts();
        setupCourtAdapter();

        return fragmentView ;
    }


    @Override
    public void onStart() {
        super.onStart();

        courtAdapter = new CourtAdapter(getActivity(), mData);
        rvCourts.setAdapter(courtAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupCourtAdapter() {

        courtAdapter = new CourtAdapter(getActivity(), mData);
        rvCourts.setAdapter(courtAdapter);
    }

    private void initmDataCourts() {

        mData = new ArrayList<>();
        mData.add(new Court("Villa Dora Pamphili", null, 4, R.mipmap.pamphili, 41.88798, 12.4329906));
        mData.add(new Court("Via Dei Buonvisi", null, 4, R.mipmap.via_dei_buonvisi, 41.8523207, 12.4222515));
        mData.add(new Court("Parco del Colle Oppio", null, 4, R.mipmap.colosseo, 41.8914268, 12.4937339));
        mData.add(new Court("Largo Cevasco", null, 4, R.mipmap.tor_3_teste2, 41.8816002, 12.5823006));
        mData.add(new Court("Largo Passamonti", null, 4,R.mipmap.largo_settimio_passamonti, 41.8882651, 12.4332069));
        mData.add(new Court("Via Martini", null, 4, R.mipmap.via_martini, 41.8515119, 12.5869594));
        mData.add(new Court("Salita di San Gregorio", null, 4,R.mipmap.salita_san_gregorio, 41.8844865, 12.4888822));
        mData.add(new Court("Via Masia", null, 3, R.mipmap.via_masia, 41.8572773, 12.5774213));
    }
}