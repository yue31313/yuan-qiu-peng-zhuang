package com.cc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * 
 * @author Himi
 *
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
	//定义两个圆形的半径与坐标
	private int r1 = 20, r2 = 20;
	private int x1 = 50, y1 = 100, x2 = 150, y2 = 100;
	//定义一个碰撞标识位
	private boolean isCollision;

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//实例线程
		th = new Thread(this);
		//启动线程
		th.start();
	}

	/**
	 * 游戏绘图
	 */
	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				if (isCollision) {
					paint.setColor(Color.RED);
					paint.setTextSize(20);
					canvas.drawText("Collision!", 0, 30, paint);
				} else {
					paint.setColor(Color.WHITE);
				}
				canvas.drawCircle(x1, y1, r1, paint);
				canvas.drawCircle(x2, y2, r2, paint);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 触屏事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x1 = (int) event.getX();
		y1 = (int) event.getY();
		if (isCollisionWithCircle(x1, y1, x2, y2, r1, r2)) {
			isCollision = true;
		} else {
			isCollision = false;
		}
		return true;
	}
	/**
	 * 圆形碰撞
	 * @param x1	圆形1的圆心X坐标
	 * @param y1	圆形2的圆心X坐标
	 * @param x2	圆形1的圆心Y坐标
	 * @param y2	圆形2的圆心Y坐标
	 * @param r1	圆形1的半径
	 * @param r2	圆形2的半径
	 * @return
	 */
	private boolean isCollisionWithCircle(int x1, int y1, int x2, int y2, int r1, int r2) {
		//Math.sqrt:开平方
		//Math.pow(double x, double y): X的Y次方
		if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r1 + r2) {
			//如果两圆的圆心距小于或等于两圆半径则认为发生碰撞
			return true;
		}
		return false;
	}

	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 游戏逻辑
	 */
	private void logic() {
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SurfaceView视图状态发生改变，响应此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
