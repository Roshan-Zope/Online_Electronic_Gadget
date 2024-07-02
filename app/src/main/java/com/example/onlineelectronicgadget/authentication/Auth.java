package com.example.onlineelectronicgadget.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.example.onlineelectronicgadget.activities.LoginActivity;
import com.example.onlineelectronicgadget.activities.MainActivity;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Tablets;
import com.example.onlineelectronicgadget.models.User;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Auth {
    private DatabaseHelper db;
    private static FirebaseAuth mAuth;
    private Context context;
    public static User currentUser;

    public Auth(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHelper();
    }

    public void reAuthenticateUserAndChangeEmail(String currentEmail, String currPassword, String newEmail) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currPassword);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   Log.d("myTag", "user re-authenticated");
                   changeEmail(user, newEmail);
               }
            }).addOnFailureListener(e -> {
                Log.d("myTag", "user re-authenticated failed");
            });
        }

    }

    private void changeEmail(FirebaseUser user, String newEmail) {
        if (user != null) {
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("myTag", "email changed successfully");
                    db.updateUser(user.getUid(), newEmail, "email");
                    user.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(context, "verification email sent to your email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(context, "email verification failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }).addOnFailureListener(e -> {
                Log.d("myTag", "email update failed");
            });
        }
    }

    public void reAuthenticateAndChangedPassword(String email, String oldPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("myTag", "user re-authenticated");
                    changePassword(user, newPassword);
                }
            }).addOnFailureListener(e -> {
                Log.d("myTag", "user re-authenticated failed");
            });
        }
    }

    public void changePassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        db.updateUser(user.getUid(), newPassword, "password");
                        /*user.sendEmailVerification().addOnCompleteListener(task1 -> {
                            Toast.makeText(context, "verification email sent to your email", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(context, "email verification failed", Toast.LENGTH_SHORT).show();
                        });*/
                        Log.d("myTag", "email changed successfully");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("myTag", "error => while updating password");
                });
    }

    public void authenticate() {
        FirebaseUser currUser = mAuth.getCurrentUser();
        if (currUser != null) {
            db.getCurrUser(currUser.getUid(), user -> {
                currentUser = user;
            });
            Log.d("myTag", "user successfully login");
        }
        else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    public void registerUser(String username, String email, String password, String accType) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "Registration successful");
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                        user.updateProfile(request).addOnCompleteListener(v -> {
                            if (task.isSuccessful()) {
                                Log.d("myTag", "profile updated successfully");
                                user.sendEmailVerification().addOnCompleteListener((Activity) context, t -> {
                                    if (t.isSuccessful()) {
                                        Log.d("myTag", "Email Verification sent");
                                        Toast.makeText(context, "Verify your email", Toast.LENGTH_SHORT).show();

                                        db.saveUser(mAuth.getCurrentUser().getUid(), username, email, password, accType);
                                        changeActivity(context, LoginActivity.class, accType);
                                    }
                                    else {
                                        Log.d("myTag", "Email verification failed");
                                        Toast.makeText(context, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.d("myTag", "user profile is not updated");
                            }
                        });
                    } else {
                        Log.d("myTag", "Registration failed");
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void login(String email, String password, String accType) {
        Log.d("myTag", "in Auth class => login()");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, task -> {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                if (task.isSuccessful()) {
                                    Log.d("myTag", "Authentication successful");
                                    Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show();

                                    db.getUserAccountType(email, accType1 -> {
                                       if (accType != null) {
                                           FirebaseUser user = mAuth.getCurrentUser();
                                           if (user != null) {
                                               String id = user.getUid();
                                               SharedPreferences preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                               SharedPreferences.Editor editor = preferences.edit();
                                               editor.putString(id + "_accType", accType);
                                               editor.apply();
                                           }
                                           changeActivity(context, MainActivity.class, accType1);
                                       } else {
                                           Log.d("myTag", "failed to login");
                                       }
                                    });
                                } else {
                                    Log.d("myTag", "Authentication failed");
                                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("myTag", "Email is not verified");
                                Toast.makeText(context, "Please verify your email first", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                            Log.d("myTag", "Authentication failed");
                        }
                    });
    }

    private void changeActivity(Context context, Class<?> cls, String accType) {

        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void deleteAcc(String email, String password) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("myTag", "user deleted");
                    mAuth.signOut();
                    redirectToLogin();
                } else {
                    Log.d("myTag", "user account deletion failed");
                    if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                        // The user needs to re-authenticate
                        reAuthenticateAndDelete(email, password);
                    }
                }
            });
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void reAuthenticateAndDelete(String email, String password) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("myTag", "Re-authentication successful.");
                    deleteAcc(email, password);
                } else {
                    Log.e("myTag", "Re-authentication failed.", task.getException());
                }
            });
        }
    }
}
