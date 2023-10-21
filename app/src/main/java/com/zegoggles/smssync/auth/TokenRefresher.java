package com.zegoggles.smssync.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountsException;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import com.zegoggles.smssync.preferences.AuthPreferences;

import java.io.IOException;

import static android.text.TextUtils.isEmpty;
import static com.zegoggles.smssync.App.TAG;
import static com.zegoggles.smssync.activity.auth.AccountManagerAuthActivity.AUTH_TOKEN_TYPE;
import static com.zegoggles.smssync.activity.auth.AccountManagerAuthActivity.GOOGLE_TYPE;

public class TokenRefresher {
    private @Nullable final AccountManager accountManager;
    private final OAuth2Client oauth2Client;
    private final AuthPreferences authPreferences;

    public TokenRefresher(Context context, OAuth2Client oauth2Client, AuthPreferences authPreferences) {
        this(AccountManager.get(context), oauth2Client, authPreferences);
    }

    TokenRefresher(@Nullable AccountManager accountManager, OAuth2Client oauth2Client,
                   AuthPreferences authPreferences) {
        this.authPreferences = authPreferences;
        this.oauth2Client = oauth2Client;
        this.accountManager = accountManager;
    }

    public void refreshOAuth2Token() throws TokenRefreshException{
        final String token = authPreferences.getOauth2Token();
        final String refreshToken = authPreferences.getOauth2RefreshToken();
        final String name = authPreferences.getOauth2Username();

        if (isEmpty(token)) {
            throw new TokenRefreshException("no current token set");
        }

        if (!isEmpty(refreshToken)) {
            // user authenticated using webflow
            refreshUsingOAuth2Client(name, refreshToken);
        } else {
            refreshUsingAccountManager(token, name);
        }
    }

    private void refreshUsingAccountManager(String token, String name) throws TokenRefreshException {
        if (accountManager == null) throw new TokenRefreshException("account manager is null");
        invalidateToken(token);
        //noinspection TryWithIdenticalCatches
        try {
            Bundle bundle = getAuthToken(new Account(name, GOOGLE_TYPE));

            if (bundle != null) {
                String newToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                if (!isEmpty(newToken)) {
                    authPreferences.setOauth2Token(name, newToken, null);
                } else {
                    throw new TokenRefreshException("no new token obtained");
                }
            } else {
                throw new TokenRefreshException("no bundle received from accountmanager");
            }
        } catch (AccountsException e) {
            Log.w(TAG, e);
            throw new TokenRefreshException(e);
        } catch (IOException e) {
            Log.w(TAG, e);
            throw new TokenRefreshException(e);
        }
    }

    private Bundle getAuthToken(Account account) throws AccountsException, IOException {
        return getAuthTokenApi14(account);
    }

    private Bundle getAuthTokenApi14(Account account) throws AccountsException, IOException {
        //noinspection DataFlowIssue
        return accountManager.getAuthToken(account,
                AUTH_TOKEN_TYPE,
                null,
                true,
                null,
                null
        ).getResult();
    }

    public boolean invalidateToken(String token) {
        if (accountManager != null) {
            // USE_CREDENTIALS permission should be enough according to docs
            // but some systems require MANAGE_ACCOUNTS

            // java.lang.SecurityException: caller uid 10051 lacks android.permission.MANAGE_ACCOUNTS
            try {
                accountManager.invalidateAuthToken(GOOGLE_TYPE, token);
                return true;
            } catch (SecurityException e) {
                Log.w(TAG, e);
            }
        }
        return false;
    }

    private void refreshUsingOAuth2Client(String name, String refreshToken) throws TokenRefreshException {
        try {
            final OAuth2Token token = oauth2Client.refreshToken(refreshToken);
            authPreferences.setOauth2Token(name, token.accessToken, isEmpty(token.refreshToken) ? refreshToken : token.refreshToken);
        } catch (IOException e) {
            throw new TokenRefreshException(e);
        }
    }
}
