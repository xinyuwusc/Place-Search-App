package com.example.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Photos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Photos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Photos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   public String placeId = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Photos() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Photos.
     */
    // TODO: Rename and change types and number of parameters
    public static Photos newInstance(String param1, String param2) {
        Photos fragment = new Photos();
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


    private GeoDataClient geoDataClient;
    private List<PlacePhotoMetadata> photosDataList;
    public ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    public ImageView[] imageViews;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String placeid = GetId();
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        ImageView imageView0 = (ImageView) view.findViewById(R.id.place_image0);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.place_image1);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.place_image2);
        ImageView imageView3 = (ImageView) view.findViewById(R.id.place_image3);
        ImageView imageView4 = (ImageView) view.findViewById(R.id.place_image4);
        ImageView imageView5 = (ImageView) view.findViewById(R.id.place_image5);
        ImageView imageView6 = (ImageView) view.findViewById(R.id.place_image6);
        ImageView imageView7 = (ImageView) view.findViewById(R.id.place_image7);
        ImageView imageView8 = (ImageView) view.findViewById(R.id.place_image8);
        ImageView imageView9 = (ImageView) view.findViewById(R.id.place_image9);
        imageViews = new ImageView[]{imageView0,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9};
        geoDataClient = Places.getGeoDataClient(getActivity(), null);
        final Task<PlacePhotoMetadataResponse> photoResponse = geoDataClient.getPlacePhotos(placeid);
        photoResponse.addOnCompleteListener
                (new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        photosDataList = new ArrayList<>();
                        PlacePhotoMetadataResponse photos = task.getResult();
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if(photoMetadataBuffer.getCount()>0){
                            for(int i = 0; i<photoMetadataBuffer.getCount(); i++){
                                photosDataList.add(photoMetadataBuffer.get(i).freeze());
                                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                                getPhoto(photoMetadata,i);
                            }
                        }
                        photoMetadataBuffer.release();
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

    public void SetId(String placeid){
        placeId = placeid;
    }

    public String GetId(){
        return placeId;
    }

    private void getPhoto(PlacePhotoMetadata photoMetadata, final int i){
        Task<PlacePhotoResponse> photoResponse = geoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap photoBitmap = photo.getBitmap();
                imageViews[i].invalidate();
                imageViews[i].setImageBitmap(photoBitmap);
            }
        });
    }
}
