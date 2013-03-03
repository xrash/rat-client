package sh.xra.controllers;

import sh.xra.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.util.Log;
import android.content.Context;
import android.os.Vibrator;

import sh.xra.Handler;
import sh.xra.Options;

import android.view.WindowManager.LayoutParams;

public class TouchController extends Controller
{
    private Timer timer = new Timer();

    private Vibrator vibrator;

    private View leftButton;
    private View rightButton;

    private int lastX;
    private int lastY;
    private long lastDown;
    private byte lastPressedButton;

    private boolean holdingButton = false;
    private boolean inMovement = false;
    private boolean didWheel = false;

    final private int TIME_TO_MOVE = 100;

    public void onCreate(Bundle savedInstanceState)
    {
        this.setContentView(R.layout.touch_controller);
        super.onCreate(savedInstanceState); 
        this.leftButton = findViewById(R.id.left_button);
        this.rightButton = findViewById(R.id.right_button);
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onResume()
    {
        super.onResume();

        if (Options.getBrightnessReduction()) {
            LayoutParams lp = this.getWindow().getAttributes();
            lp.screenBrightness = (float) 0.01;
            this.getWindow().setAttributes(lp); 
        }
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        int eventAction = event.getAction();
        int pointerCount = event.getPointerCount();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                this.lastX = (int) event.getX();
                this.lastY = (int) event.getY();
                this.lastDown = event.getEventTime();
                this.lastPressedButton = this.getPressedButton(event);
                this.timer.count();
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();

                int x = (currentX - this.lastX);
                int y = (currentY - this.lastY);

                this.lastX = currentX;
                this.lastY = currentY;

                if (x == 0 && y == 0) {
                    break;
                }

                if ((event.getEventTime() - this.lastDown) < 200) {
                    if (x >= -2 && x <= 2 && y >= -2 && y <= -2) {
                        break;
                    }
                }

                if (!this.inMovement) {
                    this.inMovement = true;
                    this.timer.interrupt();
                }

                if (pointerCount == 1 && !this.didWheel) {
                    this.handler.move(x, y);
                } else if (this.didWheel) {
                    this.didWheel = false;
                } else {
                    this.didWheel = true;
                    Log.d("Rat", "bozo:" + y);
                    if (y > 0) {
                        this.handler.wheelDown(y);
                    } else {
                        this.handler.wheelUp(y);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                if ((event.getEventTime() - this.lastDown) < 100) {
                    this.handler.click(this.lastPressedButton);
                } else if (this.holdingButton) {
                    this.handler.release(this.lastPressedButton);
                }
                this.holdingButton = false;
                this.inMovement = false;
                this.timer.interrupt();
                break;
        }

        return true; 
    }

    private byte getPressedButton(MotionEvent event)
    {
        if (event.getRawX() >= this.leftButton.getWidth()) {
            return Handler.RIGHT_BUTTON;
        } else {
            return Handler.LEFT_BUTTON;
        }
    }

    public void shake()
    {
        this.handler.press(this.lastPressedButton);
        this.holdingButton = true;
        this.vibrator.vibrate(50);
    }

    public class Timer implements Runnable
    {
        private boolean interrupted = false;

        public void count()
        {
            this.interrupted = false;
            new Thread(this).start();
        }

        public void run()
        {
            for (int i = 0; i < 1000; i++) {
                try {
                    if (this.interrupted) {
                        break;
                    }
                    Thread.sleep(1);
                } catch (Exception e) {
                    Log.d("Rat", "timer:" + e);
                }
            }

            if (!this.interrupted) {
                shake();
            }
        }

        public void interrupt()
        {
            this.interrupted = true;
        }
    }
}
