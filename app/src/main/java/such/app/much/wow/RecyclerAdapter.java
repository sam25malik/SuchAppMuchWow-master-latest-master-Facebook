package such.app.much.wow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {

    private ArrayList<Photo> mPhotos;

    public RecyclerAdapter(ArrayList<Photo> photos) {
        mPhotos = photos;
    }

    @Override
    public RecyclerAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PhotoHolder holder, int position) {
        Photo itemPhoto = mPhotos.get(position);
        holder.bindPhoto(itemPhoto);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemDescription;
        private TextView mLikes;
        private TextView mComments;
        private Photo mPhoto;

        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public PhotoHolder(View v) {
            super(v);

            mItemImage = (ImageView) v.findViewById(R.id.item_image);
            mItemDate = (TextView) v.findViewById(R.id.item_date);
            mItemDescription = (TextView) v.findViewById(R.id.item_description);
            mLikes = (TextView) v.findViewById(R.id.likes);
            mComments = (TextView) v.findViewById(R.id.comments);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
            Intent intent = new Intent(v.getContext().getApplicationContext(), PostImageViewActivity.class);
            intent.putExtra("pos", getPosition());
            v.getContext().startActivity(intent);
//            Context context = itemView.getContext();
//            Intent showPhotoIntent = new Intent(context, PhotoActivity.class);
//            showPhotoIntent.putExtra(PHOTO_KEY, mPhoto);
//            context.startActivity(showPhotoIntent);
        }

        public void bindPhoto(Photo photo) {
            mPhoto = photo;
            Long time = photo.getTime();
            //Picasso.with(mItemImage.getContext()).load(photo.getImage()).into(mItemImage);
            mItemDate.setText(Photo.toHumanReadable(time));
            if(photo.getText() == null)
                mItemDescription.setVisibility(View.GONE);
            else
                mItemDescription.setText(photo.getText());
            mLikes.setText(Integer.toString(photo.getLikes()) + " Likes");

            if(photo.getImageBitmap() != null){
                mItemImage.setImageBitmap(mPhoto.getImageBitmap());
            } else{
                photo.getParseFile().getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if(e == null){
                            Log.d("bindPhoto","Getting image bitmap");
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            mItemImage.setImageBitmap(bmp);
                            mPhoto.setImageBitmap(bmp);
                        }
                    }
                });
            }
        }
    }

}