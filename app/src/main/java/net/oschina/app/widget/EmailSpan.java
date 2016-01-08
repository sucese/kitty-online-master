package net.oschina.app.widget;

import android.app.Activity;
import android.support.v4.app.ShareCompat;
import android.text.style.ClickableSpan;
import android.view.View;

public class EmailSpan extends ClickableSpan {

	private String email;

	public EmailSpan(String email) {
		this.email = email;
	}

	@Override
	public void onClick(View widget) {
		try {
			ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder
					.from((Activity)widget.getContext());
			builder.setType("message/rfc822");
			builder.addEmailTo(email);
			builder.setSubject("");
			builder.setChooserTitle("");
			builder.startChooser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
