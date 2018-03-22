/*
Copyright 2015 shizhefei（LuckyJayce）
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.fzui.libs.FzEmptyView;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fzui.libs.R;


/**
 * 自定义要切换的布局，通过IVaryViewHelper实现真正的切换<br>
 * 使用者可以根据自己的需求，使用自己定义的布局样式
 * 
 * @author LuckyJayce
 * 
 */
public class LoadViewHelper {

	private IVaryViewHelper helper;
	private RotateAnimation mAnim;

	public LoadViewHelper(View view) {
		this(new VaryViewHelper(view));
	}

	public LoadViewHelper(IVaryViewHelper helper) {
		super();
		this.helper = helper;
	}

	public void showError(String errorText, String buttonText,
						  OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_error);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(errorText);
		Button button = (Button) layout.findViewById(R.id.button1);
		button.setText(buttonText);
		button.setOnClickListener(onClickListener);
		helper.showLayout(layout);
	}

	public void showEmpty(String errorText, String buttonText,
						  OnClickListener onClickListener) {
		View layout = helper.inflate(R.layout.load_empty);
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		textView.setText(errorText);
		Button button = (Button) layout.findViewById(R.id.button1);

		if (TextUtils.isEmpty(buttonText)) {
			button.setVisibility(View.GONE);
		} else {
			button.setText(buttonText);
			button.setOnClickListener(onClickListener);
		}

		helper.showLayout(layout);
	}

	private void initAnim() {
		mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f,
				Animation.RESTART, 0.5f);
		mAnim.setDuration(2000);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setRepeatMode(Animation.RESTART);
		mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
	}

	public void showLoading(String loadText) {
		View layout = helper.inflate(R.layout.load_ing);
		initAnim();
		TextView textView = (TextView) layout.findViewById(R.id.textView1);
		ImageView iv_route = (ImageView) layout.findViewById(R.id.iv_route);
		textView.setText(loadText);
		helper.showLayout(layout);
		iv_route.startAnimation(mAnim);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void restore() {
		if (mAnim != null) {
			mAnim.cancel();
		}
		helper.restoreView();
	}
}
