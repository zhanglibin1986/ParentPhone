package parent.com.parentphone;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.parentphone
 * @date 16/1/8下午5:19
 * @Description
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
