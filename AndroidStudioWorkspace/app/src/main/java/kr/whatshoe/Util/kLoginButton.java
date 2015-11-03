package kr.whatshoe.Util;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.util.helper.StoryProtocol;
import com.kakao.util.helper.TalkProtocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.whatshoe.whatShoe.R;

/**
 * Created by jaewoo on 2015-10-15.
 */
public class kLoginButton extends FrameLayout {
    public kLoginButton(Context context) {
        super(context);
    }

    public kLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public kLoginButton(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        inflate(this.getContext(), R.layout.kakao_login_layout, this);
        this.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List authTypes = kLoginButton.this.getAuthTypes();
                if (authTypes.size() == 1) {
                    Session.getCurrentSession().open((AuthType) authTypes.get(0), (Activity) getContext());
                } else {
//                    super.onClickLoginButton(authTypes);
                }

            }
        });
    }
    private List<AuthType> getAuthTypes() {
        ArrayList availableAuthTypes = new ArrayList();
        if(TalkProtocol.existCapriLoginActivityInTalk(this.getContext(), Session.getCurrentSession().isProjectLogin())) {
            availableAuthTypes.add(AuthType.KAKAO_TALK);
            availableAuthTypes.add(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN);
        }

        if(StoryProtocol.existCapriLoginActivityInStory(this.getContext(), Session.getCurrentSession().isProjectLogin())) {
            availableAuthTypes.add(AuthType.KAKAO_STORY);
        }

        availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        AuthType[] selectedAuthTypes = Session.getCurrentSession().getAuthTypes();
        availableAuthTypes.retainAll(Arrays.asList(selectedAuthTypes));
        if(availableAuthTypes.size() == 0) {
            availableAuthTypes.add(AuthType.KAKAO_ACCOUNT);
        }

        return availableAuthTypes;
    }
    private static class Item {
        public final int textId;
        public final int icon;
        public final AuthType authType;

        public Item(int textId, Integer icon, AuthType authType) {
            this.textId = textId;
            this.icon = icon.intValue();
            this.authType = authType;
        }
    }
}
