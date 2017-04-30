package com.educareapps.mylibrary;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class CustomTouchEvent {
	private static final int NONE = 0;
	private static final int SWIPE = 1;
	private int mode = NONE;
	private float startX;
	private float stopX;
	private static final int TRESHOLD = 100;

	private float x1,x2;
	static final int MIN_DISTANCE = 150;

	OnFling_right_left listener;

	public CustomTouchEvent(OnFling_right_left listener){
		this.listener=listener;
	}
	
	public void onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(ApplicationMode.devMode){
			//gestureDetector.onTouchEvent(event);
			switch(event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					x1 = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					x2 = event.getX();
					float deltaX = x2 - x1;
					float deltax2=x1-x2;
					if (Math.abs(deltaX) > MIN_DISTANCE)
					{
						listener.flingRL_happened(true);
						//Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
					}
					else if( deltax2>0 && Math.abs(deltax2) > MIN_DISTANCE)
					{
						listener.flingRL_happened(false);
						// consider as something else - a screen tap for example
					}
					break;
			}

		}else {

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_POINTER_DOWN:
					// This happens when you touch the screen with two fingers
					mode = SWIPE;
					// You can also use event.getY(1) or the average of the two
					startX = event.getX(0);
					break;

				case MotionEvent.ACTION_POINTER_UP:
					// This happens when you release the second finger
					mode = NONE;
					if (Math.abs(startX - stopX) > TRESHOLD) {
						if (startX > stopX) {
							// Swipe right-left
							listener.flingRL_happened(false);
							System.out.println("Swipe up");
						} else {
							// Swipe left-right
							listener.flingRL_happened(true);
							System.out.println("Swipe down");
						}
					}
					this.mode = NONE;
					break;

				case MotionEvent.ACTION_MOVE:
					if (mode == SWIPE) {
						stopX = event.getX(0);
					}
					break;
			}
		}
	}
	
	public interface OnFling_right_left{
		public void flingRL_happened(boolean editOn);
		
	}



	private final class GestureListener extends GestureDetector.SimpleOnGestureListener {



		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			//while in developer mode left to right will work only
			listener.flingRL_happened(true);
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
	
}
