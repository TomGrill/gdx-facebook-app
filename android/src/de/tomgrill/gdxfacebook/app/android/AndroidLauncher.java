package de.tomgrill.gdxfacebook.app.android;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import de.tomgrill.gdxfacebook.app.GdxFacebookSampleApp;

/*
    DEFAULT LIBGDX SETUP SETUP
 */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new GdxFacebookSampleApp(), config);
    }
}

/*
    FRAGMENT ACTIVITY SETUP
 */
//public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks {
//
//    private GameFragment gameFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        gameFragment = new GameFragment();
//        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//        trans.replace(android.R.id.content, gameFragment);
//        trans.commit();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        gameFragment.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void exit() {
//
//    }
//
//    public static class GameFragment extends AndroidFragmentApplication {
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            return initializeForView(new GdxFacebookSampleApp(), new AndroidApplicationConfiguration());
//        }
//    }
//}