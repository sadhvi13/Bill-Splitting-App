package com.example.android.billsplittingapp;

import android.support.v7.widget.RecyclerView;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.GestureDetector;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<PersonGallery> images;
    private Context mContext;
    String fromWich;
    GlobalVariables appState;
    String userID;

    public GalleryAdapter(Context context, List<PersonGallery> images,String fromWich,String user_id) {
        mContext = context;
        this.images = images;
        this.fromWich = fromWich ;
        this.userID = user_id;
        appState = (GlobalVariables)context.getApplicationContext();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail,imgDelete;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            imgDelete = (ImageView)view.findViewById(R.id.img_gallery_delete);

            if(fromWich.equals("myGallery")){
                imgDelete.setVisibility(View.VISIBLE);
            }else {
                imgDelete.setVisibility(View.GONE);
            }

        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PersonGallery image = images.get(position);

        int radius = 30; // corner radius, higher value = more rounded
        int margin = 10; // crop margin, set to 0 for corners with no crop


        Glide.with(mContext)
                .load("http://phpstack-127383-542740.cloudwaysapps.com/public/uploads/gallary/"+image.getImage())
                .thumbnail(0.1f)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.thumbnail);

        if(holder.imgDelete.getVisibility()==View.VISIBLE){

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setCancelable(false);
                    dialog.setMessage("Are you sure you want to delete this image ?" );
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                if(appState.getNetworkCheck()){
                                    deleteImage(image.getId(),position);
                                }else {
                                    Toast.makeText(mContext, "" + mContext.getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                            .setNegativeButton("NO ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Action for "Cancel".
                                }
                            });

                    final AlertDialog alert = dialog.create();
                    alert.show();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void deleteImage(String id, final int position) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id",userID);
        jsonObject.put("image_id",id);

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Deleting....");
        progressDialog.show();

        Log.v("dele_img_input",jsonObject+"");

        RequestQueue queue = Volley.newRequestQueue(mContext);
        Log.v("urldeleteimg", Utils.DELETE_MY_IMAGE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.DELETE_MY_IMAGE, jsonObject,

                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.v("delete_img_res",response.toString());

                        progressDialog.dismiss();
                        String success,mssg;
                        try {
                            success = response.getString("success");
                            mssg = response.getString("message");

                            if(success.equals("true")){

                                images.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(mContext, mssg, Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(mContext, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


        };
        queue.add(jsonObjReq);
    }

}

