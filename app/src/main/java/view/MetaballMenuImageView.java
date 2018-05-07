package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.kys_31.figureinformation.R;


/**
 * Created by Melvin Lobo on 11/29/2015.
 *
 * Just a basic extension of the Imageview class to support selected and non-selected Images
 */
public class MetaballMenuImageView extends ImageView {

    private int mnDefaultImage = 0;
    private int mnSelectedImage = 0;
    private boolean mbSelected = false;

    public MetaballMenuImageView(Context context)    {
        super(context);
        init(context, null);
    }

    public MetaballMenuImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    public MetaballMenuImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs ) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MetaballMenuImageView, 0, 0);
            mnDefaultImage = a.getResourceId(R.styleable.MetaballMenuImageView_defaultImage, 0);
            mnSelectedImage = a.getResourceId(R.styleable.MetaballMenuImageView_selectedImage, 0);
            a.recycle();
        }

        if(mnDefaultImage != 0)
            setImageResource(mnDefaultImage);
    }

    public void setSelected(boolean bSelected) throws IllegalArgumentException {
        setEnabled(!bSelected);
        if((mnSelectedImage != 0) && (mnDefaultImage != 0)) {
            mbSelected = bSelected;
            setImageResource((mbSelected) ? mnSelectedImage : mnDefaultImage);
        }
        else {
            throw new IllegalArgumentException("The default or selected image references are not provided");
        }
    }

    public boolean isSelected() {
        return mbSelected;
    }
}
