package com.qustom.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class QustomDialogBuilder extends AlertDialog.Builder{

	/** The custom_body layout */
	private View mDialogView;
	/** optional dialog title layout */
	private TextView mTitle;
	/** optional alert dialog image */
	private ImageView mIcon;
	/** optional message displayed below title if title exists*/
	private TextView mMessage;
	/** optional list of items displayed below title if title exists*/
	/** The colored holo divider. You can set its color with the setDividerColor method */
	private View mDivider;
	/** The color displayed when pressing a button or list item. Defaults to 'holo blue light' #33b5e5*/
	private ColorDrawable mPressedColor;
	
    public QustomDialogBuilder(Context context) {
        super(context);

        mDialogView = View.inflate(context, R.layout.qustom_dialog_layout, null);
        setView(mDialogView);

        mTitle = (TextView) mDialogView.findViewById(R.id.qustom_alertTitle);
        mMessage = (TextView) mDialogView.findViewById(R.id.qustom_message);
        mIcon = (ImageView) mDialogView.findViewById(R.id.qustom_icon);
        mDivider = mDialogView.findViewById(R.id.qustom_titleDivider);
        
        mPressedColor = new ColorDrawable(Color.parseColor("#33b5e5"));
	}

    /** 
     * Use this method to color the divider between the title and content.
     * Will not display if no title is set.
     * 
     * @param colorString for passing "#ffffff"
     */
    public QustomDialogBuilder setDividerColor(String colorString) {
    	mDivider.setBackgroundColor(Color.parseColor(colorString));
    	return this;
    }
 
    @Override
    public QustomDialogBuilder setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    public QustomDialogBuilder setTitleColor(String colorString) {
    	mTitle.setTextColor(Color.parseColor(colorString));
    	return this;
    }

    @Override
    public QustomDialogBuilder setMessage(int textResId) {
        mMessage.setText(textResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setMessage(CharSequence text) {
        mMessage.setText(text);
        return this;
    }
    
    @Override
    public QustomDialogBuilder setIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }
    
    /**
     * This allows you to specify a custom layout for the area below the title divider bar
     * in the dialog. As an example you can look at example_ip_address_layout.xml and how
     * I added it in TestDialogActivity.java
     * 
     * @param resId  of the layout you would like to add
     * @param context
     */
    public QustomDialogBuilder setCustomView(int resId, Context context) {
    	View customView = View.inflate(context, resId, null);
    	((FrameLayout) mDialogView.findViewById(R.id.qustom_customPanel)).addView(customView);
    	return this;
    }

    /**
     * This shows the dialog, and sets the button and list view pressed colors. 
     * Moves the dialog's ListView around, because otherwise it appears above the title of the dialog.
     */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
    public AlertDialog show() {
    	if (mTitle.getText().equals("")){mDialogView.findViewById(R.id.qustom_topPanel).setVisibility(View.GONE);}
    	AlertDialog dialog = super.create();
    	dialog.show();
    	
    	for(int mButtonNumber=-3; mButtonNumber<0; mButtonNumber++){
    		Button mButton = dialog.getButton(mButtonNumber);
    		if (mButton == null){
    			continue;
    		}else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    			mButton.setBackground(this.getPressedDrawable());
    		}else{
				mButton.setBackgroundDrawable(this.getPressedDrawable());
			}
    	}
    	
    	ListView dialogListView = (ListView) dialog.getListView();
    	if(dialogListView != null){
	    	dialogListView.setSelector(getPressedDrawable());
	    	
	    	ViewGroup dialogListViewParent = (LinearLayout) dialogListView.getParent();
	    	dialogListViewParent.removeView(dialogListView);
	    	
	    	ViewGroup dialogLayout = (ViewGroup) dialogListViewParent.getParent();
	    	dialogLayout.removeView(dialogListViewParent);
	    	
	    	ViewGroup qustomContentPanel = (ViewGroup) mDialogView.findViewById(R.id.qustom_contentPanel);
	    	qustomContentPanel.addView(dialogListView);
	    	
	    	if(this.mMessage.getText().length()==0){
	    		qustomContentPanel.removeView(this.mMessage);
	    	}
    	}
    	return dialog;
    }
    
    public QustomDialogBuilder setAllColor(String colorString){
    	setTitleColor(colorString);
    	setDividerColor(colorString);
    	setPressedColor(colorString);
    	return this;
    }
    
    public QustomDialogBuilder setPressedColor(String colorString){ 
    	this.mPressedColor = new ColorDrawable(Color.parseColor(colorString));
    	return this;
    }
    
	/**
	 * Returns a StateListDrawable that's transparent, unless pressed, in which case it's mPressedColor.
	 * mPressedColor is set in setButtonPressColor and setAllColor.
	 * getPressedDrawable is used to set the background for both the dialog's ListView's selector,
	 * and for the background of the dialog's buttons.
	 */
    private StateListDrawable getPressedDrawable(){
    	StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_pressed	},this.mPressedColor);
		states.addState(new int[] {android.R.attr.state_focused	}, new ColorDrawable(Color.parseColor("#00000000")));
		states.addState(new int[] {								}, new ColorDrawable(Color.parseColor("#00000000")));
		states.setAlpha(153);
		return states;
    }
}