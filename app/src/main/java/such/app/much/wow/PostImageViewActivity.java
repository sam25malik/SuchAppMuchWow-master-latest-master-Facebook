package such.app.much.wow;

import com.imagezoom.ImageViewTouch;
import com.imagezoom.ImageViewTouchBase;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PostImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        Integer pos = getIntent().getExtras().getInt("pos");
        Bitmap bitmap = HomeActivity.mPhotos.get(pos).getImageBitmap();

        ImageViewTouch mImageView = (ImageViewTouch) findViewById(R.id.imageViewTouch);
        mImageView.setDisplayType( ImageViewTouchBase.DisplayType.FIT_TO_SCREEN );

        mImageView.setImageBitmap(bitmap);
    }
}
