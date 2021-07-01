package com.okada.travelogue.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.okada.travelogue.Fragment.ChatPageFragment;
import com.okada.travelogue.Fragment.FavoritePageFragment;
import com.okada.travelogue.Fragment.HistoryPageFragment;
import com.okada.travelogue.Fragment.HomePageFragment;
import com.okada.travelogue.Fragment.ProfilePageFragment;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private MeowBottomNavigation meowBottomNavigation;
    private BoomMenuButton boomButton;
    private String help, settings, signIn, signOut, message, yes, no;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        reference = FirebaseDatabase.getInstance().getReference();
        findingViews();
        checkLanguage();
        meowWorks();
        boomMenuWorks();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new HomePageFragment()).commit();
    }

    private void checkLanguage() {
        Resources resources = LanguageResourceClass.getResource(this);
        help = resources.getString(R.string.popup_help);
        settings = resources.getString(R.string.settings);
        signIn = resources.getString(R.string.sign_in);
        signOut = resources.getString(R.string.sign_out);
        message = resources.getString(R.string.quit);
        yes = resources.getString(R.string.yes);
        no = resources.getString(R.string.no);
    }


    private void boomWorks() {
        boomButton.setButtonEnum(ButtonEnum.TextInsideCircle);
        boomButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        boomButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_3);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void boomMenuWorks() {
        boomWorks();
        boomButton.addBuilder(new TextInsideCircleButton.Builder().normalText(help)
                .normalImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_help_24, null))
                .normalImageRes(R.drawable.ic_baseline_help_24)
                .imagePadding(new Rect(30, 15, 30, 30))
                .normalColor(Color.parseColor("#ffa270")).textSize(13).normalTextColor(Color.BLACK)
                .typeface(ResourcesCompat.getFont(this, R.font.baloo))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        onClickHelp();
                    }
                }));
        boomButton.addBuilder(new TextInsideCircleButton.Builder().normalText(settings)
                .normalImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_settings_24, null))
                .normalImageRes(R.drawable.ic_baseline_settings_24)
                .imagePadding(new Rect(30, 15, 30, 30))
                .normalColor(Color.parseColor("#bdbdbd")).textSize(13).normalTextColor(Color.BLACK)
                .typeface(ResourcesCompat.getFont(this, R.font.baloo))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        onClickSettings();
                    }
                }));
        if (isLoggedIn()) {
            boomButton.addBuilder(new TextInsideCircleButton.Builder().normalText(signOut)
                    .normalImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_log_out_24, null))
                    .normalImageRes(R.drawable.ic_baseline_log_out_24)
                    .imagePadding(new Rect(30, 15, 30, 30))
                    .normalColor(Color.parseColor("#5ddef4")).textSize(13).normalTextColor(Color.BLACK)
                    .typeface(ResourcesCompat.getFont(this, R.font.baloo)).rotateText(false)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            onClickSignOut();
                        }
                    }));
        } else {
            boomButton.addBuilder(new TextInsideCircleButton.Builder().normalText(signIn)
                    .normalImageRes(R.drawable.ic_baseline_login_24)
                    .normalImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_login_24, null))
                    .imagePadding(new Rect(30, 15, 30, 30))
                    .normalColor(Color.parseColor("#5ddef4")).textSize(13).normalTextColor(Color.BLACK)
                    .typeface(ResourcesCompat.getFont(this, R.font.baloo))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            onClickSignIn();
                        }
                    }));
        }

    }

    private void onClickHelp() {
        Intent intent = new Intent(this, HelpStartActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
    }

    private void onClickSettings() {
        Intent intent = new Intent(this, SettingsStartActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeRight(this);
    }

    private void onClickSignIn() {
        Intent toSignInStartActivity = new Intent(HomeActivity.this, SignInStartActivity.class);
        startActivity(toSignInStartActivity);
        Animatoo.animateInAndOut(this);
    }

    private void onClickSignOut() {
        firebaseAuth.signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        if (googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        Intent toHome = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(toHome);
        Animatoo.animateDiagonal(this);
        finish();
    }

    public void findingViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        boomButton = findViewById(R.id.boom_menu_button);

    }

    public boolean isLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;

    }

    public void meowWorks() {
        meowBottomNavigation = findViewById(R.id.meow);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_favorite_border_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_history_toggle_off_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_chat_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_baseline_account_circle_24));
        meowBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
            }
        });
        meowBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomePageFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FavoritePageFragment()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HistoryPageFragment()).commit();
                        break;
                    case 4:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ChatPageFragment()).commit();
                        break;
                    case 5:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ProfilePageFragment()).commit();
                        break;
                }
            }
        });
        meowBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
        if (firebaseAuth.getCurrentUser() != null)
        reference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("favourites").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0)
                    meowBottomNavigation.clearCount(2);
                else
                    meowBottomNavigation.setCount(2, String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (firebaseAuth.getCurrentUser() != null)
            reference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("tickets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() == 0)
                    meowBottomNavigation.clearCount(3);
                else
                    meowBottomNavigation.setCount(3, String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        meowBottomNavigation.show(1, true);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
        alert.setCancelable(false);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setTitle("Travelogue");
        alert.setMessage(message);
        alert.setPositiveButton(yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.setNegativeButton(no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

}