import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.core.splashscreen.SplashScreen;

public final class SplashScreenBridge {
    private SplashScreenBridge() {}

    public static void install(@NonNull Activity activity) {
        SplashScreen.installSplashScreen(activity);
    }
}
