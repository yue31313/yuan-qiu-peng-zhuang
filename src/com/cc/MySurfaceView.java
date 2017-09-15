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
	//��������Բ�εİ뾶������
	private int r1 = 20, r2 = 20;
	private int x1 = 50, y1 = 100, x2 = 150, y2 = 100;
	//����һ����ײ��ʶλ
	private boolean isCollision;

	/**
	 * SurfaceView��ʼ������
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
	 * SurfaceView��ͼ��������Ӧ�˺���
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//ʵ���߳�
		th = new Thread(this);
		//�����߳�
		th.start();
	}

	/**
	 * ��Ϸ��ͼ
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
	 * �����¼�����
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
	 * Բ����ײ
	 * @param x1	Բ��1��Բ��X����
	 * @param y1	Բ��2��Բ��X����
	 * @param x2	Բ��1��Բ��Y����
	 * @param y2	Բ��2��Բ��Y����
	 * @param r1	Բ��1�İ뾶
	 * @param r2	Բ��2�İ뾶
	 * @return
	 */
	private boolean isCollisionWithCircle(int x1, int y1, int x2, int y2, int r1, int r2) {
		//Math.sqrt:��ƽ��
		//Math.pow(double x, double y): X��Y�η�
		if (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) <= r1 + r2) {
			//�����Բ��Բ�ľ�С�ڻ������Բ�뾶����Ϊ������ײ
			return true;
		}
		return false;
	}

	/**
	 * �����¼�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��Ϸ�߼�
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
	 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
