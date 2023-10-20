package com.zegoggles.smssync;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static com.google.common.truth.Truth.assertThat;

import android.annotation.SuppressLint;

@RunWith(RobolectricTestRunner.class)
public class AppTest {
    @SuppressLint("IgnoreWithoutReason")
    @Ignore @Test public void shouldGetVersionName() throws Exception {
        //noinspection deprecation
        assertThat(App.getVersionName(RuntimeEnvironment.application)).matches("\\d+\\.\\d+\\.\\d+(-\\w+)?");
    }

    @Test public void shouldGetVersionCode() throws Exception {
        //noinspection deprecation
        assertThat(App.getVersionCode(RuntimeEnvironment.application)).isEqualTo(0);
    }

    @Test public void shouldTestOnSDCARD() throws Exception {
        //noinspection deprecation
        assertThat(App.isInstalledOnSDCard(RuntimeEnvironment.application)).isFalse();
    }
}
