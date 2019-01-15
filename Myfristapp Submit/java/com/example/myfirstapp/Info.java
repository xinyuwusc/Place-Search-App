package com.example.myfirstapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Info extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Info.
     */
    // TODO: Rename and change types and number of parameters
    public static Info newInstance(String param1, String param2) {
        Info fragment = new Info();
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

    public String placeId = "";
    public static double latitude;
    public static double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        String placeid = GetId();
        final String detailurl = "http://xinyuw.us-east-2.elasticbeanstalk.com/?detail=true&placeid="+ URLEncoder.encode(placeid);
        System.out.println("detailurl:"+detailurl);
        JsonObjectRequest detailRequest = new JsonObjectRequest
                (Request.Method.GET, detailurl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responsedetail) {
                        try {
                            JSONObject detailobj = responsedetail.getJSONObject("result");
                            JSONObject geometry = detailobj.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            String lat = location.getString("lat");
                            String lng = location.getString("lng");
                            latitude = Double.parseDouble(lat);
                            longitude = Double.parseDouble(lng);
                            if(detailobj.has("name")){
                                String strname = detailobj.getString("name");
                                TextView name = (TextView)getActivity().findViewById(R.id.name);
                                name.setText(strname);
                            }else{
                                TextView namelabel = (TextView)view.findViewById(R.id.name);
                                namelabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("formatted_address")){
                                String straddress = detailobj.getString("formatted_address");
                                TextView Address = (TextView) view.findViewById(R.id.Address);
                                Address.setText(straddress);
                            }else{
                                TextView addresslabel = (TextView)view.findViewById(R.id.addresslabel);
                                addresslabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("international_phone_number")){
                                String strphone = detailobj.getString("international_phone_number");
                                TextView Phone = (TextView)view.findViewById(R.id.Phone);
                                Phone.setText(strphone);
                            }else{
                                TextView phonelabel = (TextView) getView().findViewById(R.id.phonelabel);
                                phonelabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("price_level")){
                                int intprice = detailobj.getInt("price_level");
                                String strprice = "";
                                for(int i = 0; i<intprice; i++){
                                    strprice += "$";
                                }
                                TextView Price = (TextView)view.findViewById(R.id.Price);
                                Price.setText(strprice);
                            }else {
                                TextView pricelabel = (TextView) view.findViewById(R.id.pricelabel);
                                pricelabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("rating")){
                                float strratig = (float) detailobj.getDouble("rating");
                                RatingBar Rating = (RatingBar) view.findViewById(R.id.Rating);
                                Rating.setRating(strratig);
                            }else{
                                TextView ratinglabel = (TextView) view.findViewById(R.id.ratinglabel);
                                ratinglabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("url")){
                                String strurl = detailobj.getString("url");
                                TextView Google = (TextView)view.findViewById(R.id.Google);
                                Google.setText(strurl);
                            }else {
                                TextView googlelabel = (TextView) view.findViewById(R.id.googlelabel);
                                googlelabel.setVisibility(View.GONE);
                            }
                            if(detailobj.has("website")){
                                String strwebsite = detailobj.getString("website");
                                TextView Website = (TextView)view.findViewById(R.id.Website);
                                Website.setText(strwebsite);
                            }else{
                                TextView websitelabel = (TextView) view.findViewById(R.id.websitelabel);
                                websitelabel.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("WRONG");
                    }
                });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(detailRequest);
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

    public void SetId(String placeid){
        placeId = placeid;
    }

    public String GetId(){
        return placeId;
    }

    public static double SetLat(){
        return latitude;
    }

    public static double SetLng(){
        return longitude;
    }


}
