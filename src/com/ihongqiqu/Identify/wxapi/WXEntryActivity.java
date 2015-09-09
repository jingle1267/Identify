package com.ihongqiqu.Identify.wxapi;


import android.view.View;
import android.widget.Toast;
import com.ihongqiqu.Identify.activity.BaseActivity;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends BaseActivity {

		@Override
		public void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);

				handleIntent(getIntent());
				// setContentView(new View(WXEntryActivity.this));
				// toolbar.setVisibility(View.GONE);
		}

		@Override
		protected void onNewIntent(Intent intent) {
				super.onNewIntent(intent);
				handleIntent(intent);
		}

		private void handleIntent(Intent intent) {
				SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
				if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
						// 用户同意
						Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
				} else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
						// 用户取消
						Toast.makeText(this, "取消成功", Toast.LENGTH_SHORT).show();
				} else {
						Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
				}

				finish();
		}
}
