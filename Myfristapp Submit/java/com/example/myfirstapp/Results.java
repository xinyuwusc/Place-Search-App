package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Results.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Results#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Results extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Results() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Results.
     */
    // TODO: Rename and change types and number of parameters
    public static Results newInstance(String param1, String param2) {
        Results fragment = new Results();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Button searchValue;
    private EditText keywordValue;
    private Spinner categoryValue;
    private EditText distanceValue;
    private RadioGroup placeValue;
    private RadioButton hereValue;
    private AutoCompleteTextView locationValue;


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
        final View view = inflater.inflate(R.layout.fragment_results, container, false);
        hereValue = (RadioButton) view.findViewById(R.id.here);
        System.out.println("hereValue:"+hereValue);
        placeValue = (RadioGroup) view.findViewById(R.id.place);
        System.out.println("placeValue:"+ placeValue);
        placeValue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                hereValue = (RadioButton) view.findViewById(checkedId);
                System.out.println("place:"+hereValue.getText());
            }
        });


//        rootView.findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EditText keywordText = (EditText) rootView.findViewById(R.id.keywordText);
//                keywordText.setText(null);
//                keywordText.setError(null);
//                EditText distanceText = (EditText) rootView.findViewById(R.id.distanceText);
//                distanceText.setText(null);
//                EditText inputLocationText = (EditText) rootView.findViewById(R.id.inputLocationText);
//                inputLocationText.setText(null);
//                inputLocationText.setEnabled(false);
//                inputLocationText.setError(null);
//                Spinner category_spinner = (Spinner) rootView.findViewById(R.id.category_spinner);
//                category_spinner.setSelection(0);
//                RadioButton option1 = (RadioButton) rootView.findViewById(R.id.option1);
//                option1.setChecked(true);
//                RadioButton option2 = (RadioButton) rootView.findViewById(R.id.option2);
//                option2.setChecked(false);
//            }
//        });

        searchValue = (Button) view.findViewById(R.id.search);
        searchValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordValue = (EditText) view.findViewById(R.id.keyword);
                final String KeyWord = keywordValue.getText().toString();
                categoryValue = (Spinner) view.findViewById(R.id.category);
                final String Category = categoryValue.getSelectedItem().toString();
                distanceValue = (EditText) view.findViewById(R.id.distance);
                final String distanceStr = distanceValue.getText().toString();
                double Distance;
                if(distanceStr.equals("")){
                    Distance = 10*1609.344;
                }else{
                    Distance = Double.parseDouble(distanceStr)*1609.344;
                }
                System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDD:"+Distance);
                locationValue = (AutoCompleteTextView) view.findViewById(R.id.location);
                final String Location = locationValue.getText().toString();

                System.out.println("KeyWord:"+KeyWord);
                System.out.println("Category:"+Category);
                System.out.println("distance:"+Distance);
                System.out.println("location:"+Location);

                final String urltransfer;
                if(hereValue.getText().equals("Current location")){
                    double herelat = 34.0223519;
                    double herelon = -118.285117;
                    urltransfer="http://xinyuw.us-east-2.elasticbeanstalk.com/?search=true&latitude="+herelat
                            +"&longitude="+herelon
                            +"&keyword="+ URLEncoder.encode(KeyWord)
                            +"&category="+URLEncoder.encode(Category)
                            +"&distance="+Distance;
                }else{
                    urltransfer="http://xinyuw.us-east-2.elasticbeanstalk.com/?search=true&inputaddress=true&keyword="+URLEncoder.encode(KeyWord)
                            +"&category="+URLEncoder.encode(Category)
                            +"&distance="+Distance
                            +"&address="+URLEncoder.encode(Location);
                }

                RequestQueue queue = Volley.newRequestQueue(getContext());
                JsonObjectRequest nearbyRequest = new JsonObjectRequest
                        (Request.Method.GET, urltransfer, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject responsenearby) {
                                System.out.println("urltransfer:"+urltransfer);
                                Intent intent = new Intent();
                                intent.setClass(view.getContext(), SearchresultsActivity.class);
                                intent.putExtra("nearbyjson", responsenearby.toString());
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("WRONG");
                            }
                        });
                queue.add(nearbyRequest);
            }
        });
        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
