package com.hezare.mmm.WebSide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.hezare.mmm.App;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import okhttp3.Response;

/**
 * Created by amirhododi on 7/29/2017.
 */


public class SendRequest {
   public static OnLoginCompleteListner onCardLoginCompleteClickListner;
    public  static OnLoginErrorListner onCardLoginErrorClickListner;
    public static OnListeDaneshAmoozaneMadreseCompleteListner onCardListeDaneshAmoozaneMadreseCompleteClickListner;
    public  static OnListeDaneshAmoozaneMadreseErrorListner onCardListeDaneshAmoozaneMadreseErrorClickListner;

    public static OnListeMaghateBeHamraheKelasHaCompleteListner onCardListeMaghateBeHamraheKelasHaCompleteClickListner;
    public  static OnListeMaghateBeHamraheKelasHaErrorListner onCardListeMaghateBeHamraheKelasHaErrorClickListner;

    public static OnListDorosCompleteListner onCardListDorosCompleteClickListner;
    public  static OnListDorosErrorListner onCardListDorosErrorClickListner;

    public static OnListClassStudentsCompleteListner onCardListClassStudentsCompleteClickListner;
    public  static OnListClassStudentsErrorListner onCardListClassStudentsErrorClickListner;

    public static OnReadElanatCompleteListner onCardReadElanatCompleteClickListner;
    public  static OnReadElanatErrorListner onCardReadElanatErrorClickListner;

    public static OnSendElanatCompleteListner onCardSendElanatCompleteClickListner;
    public  static OnSendElanatErrorListner onCardSendElanatErrorClickListner;

    public static OnAbsentCompleteListner onCardAbsentCompleteClickListner;
    public  static OnAbsentErrorListner onCardAbsentErrorClickListner;
    public static OnElanatCompleteListner onCardElanatCompleteClickListner;
    public  static OnElanatErrorListner onCardElanatErrorClickListner;
    public static OnTashvigCompleteListner onCardTashvigCompleteClickListner;
    public  static OnTashvigErrorListner onCardTashvigErrorClickListner;
    public static OnGeybatCompleteListner onCardGeybatCompleteClickListner;
    public  static OnGeybatErrorListner onCardGeybatErrorClickListner;
    public static OnClassListCompleteListner onCardClassListCompleteClickListner;
    public  static OnClassListErrorListner onCardClassListErrorClickListner;
    public static OnClassListDetailsCompleteListner onCardClassListDetailsCompleteClickListner;
    public  static OnClassListDetailsErrorListner onCardClassListDetailsErrorClickListner;
    public static OnTakalifCompleteListner onCardTakalifCompleteClickListner;
    public  static OnTakalifErrorListner onCardTakalifErrorClickListner;
    public static OnForgetCompleteListner onCardForgetCompleteClickListner;
    public  static OnForgetErrorListner onCardForgetErrorClickListner;
    public static OnChangePassCompleteListner onCardChangePassCompleteClickListner;
    public  static OnChangePassErrorListner onCardChangePassErrorClickListner;
    public static OnLogOutCompleteListner onCardLogOutCompleteClickListner;
    public  static OnLogOutErrorListner onCardLogOutErrorClickListner;
    public static OnChatListCompleteListner onCardChatListCompleteClickListner;
    public  static OnChatListErrorListner onCardChatListErrorClickListner;
    public static OnSendChatCompleteListner onCardSendChatCompleteClickListner;
    public  static OnSendChatErrorListner onCardSendChatErrorClickListner;
    public static OnReadChatCompleteListner onCardReadChatCompleteClickListner;
    public  static OnReadChatErrorListner onCardReadChatErrorClickListner;
    public static OnTashvighoTanbiheKelasiCompleteListner onCardTashvighoTanbiheKelasiCompleteClickListner;
    public  static OnTashvighoTanbiheKelasiErrorListner onCardTashvighoTanbiheKelasiErrorClickListner;


    public static void SendPostLogin(String UserName,String Password,String AndroidId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidAccount/Login?UserName="+UserName+"&Password="+Password+"&AndroidId="+AndroidId+"&Type=Moaven");
                a.setPriority(Priority.HIGH);
                a.doNotCacheResponse();
                a.build()


                        .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                App.setCookie(response);
                                try {
                                    onCardLoginCompleteClickListner.OnLoginCompleteed(response.body().source().readUtf8());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardLoginErrorClickListner.OnLoginErrored(anError.getErrorDetail());
                    }
                });


    }



    public static void GetPostListeDaneshAmoozaneMadrese(String MoavenId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ListeDaneshAmoozaneMadrese?MoavenId="+MoavenId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardListeDaneshAmoozaneMadreseCompleteClickListner.OnListeDaneshAmoozaneMadreseCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardListeDaneshAmoozaneMadreseErrorClickListner.OnListeDaneshAmoozaneMadreseErrored(anError.getErrorDetail());
                    }
                });


    }

    public static void GetPostListeDaneshAmoozaneClass(String KelasId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ListDaneshAmoozan?KelasId="+KelasId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardListClassStudentsCompleteClickListner.OnListClassStudentsCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardListClassStudentsErrorClickListner.OnListClassStudentsErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void GetPostListeMaghateBeHamraheKelasHa(String MoavenId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ListeMaghateBeHamraheKelasHa?MoavenId="+MoavenId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                onCardListeMaghateBeHamraheKelasHaCompleteClickListner.OnListeMaghateBeHamraheKelasHaCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                    onCardListeMaghateBeHamraheKelasHaErrorClickListner.OnListeMaghateBeHamraheKelasHaErrored(anError.getErrorDetail());
                    }
                });


    }



    public static void GetPostTashvig(String DaneshAmoozId,String Mah) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/TashvighVaTanbih?DaneshAmoozId="+DaneshAmoozId+"&mah="+Mah);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardTashvigCompleteClickListner.OnTashvigCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardTashvigErrorClickListner.OnTashvigErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void GetPostGeybat(String DaneshAmoozId,String Mah) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/GozaresheHuzurghiab?DaneshAmoozId="+DaneshAmoozId+"&mah="+Mah);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardGeybatCompleteClickListner.OnGeybatCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardGeybatErrorClickListner.OnGeybatErrored(anError.getErrorDetail());
                    }
                });


    }

    public static void GetPostListDoros(String KelasId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ListeDorooseKelasha?KelasId="+KelasId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardListDorosCompleteClickListner.OnListDorosCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardListDorosErrorClickListner.OnListDorosErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void GetPostChatList(String DaneshAmoozId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ListeMokhatabineOvlia?DaneshAmuzId="+DaneshAmoozId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardChatListCompleteClickListner.OnChatListCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardChatListErrorClickListner.OnChatListErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void GetPostClassListDetails(String DaneshAmoozId,String F_BarnameHaftegiId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/DaryafteNomarateDars?DaneshAmoozId="+DaneshAmoozId+"&F_BarnameHaftegiId="+F_BarnameHaftegiId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardClassListDetailsCompleteClickListner.OnClassListDetailsCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardClassListDetailsErrorClickListner.OnClassListDetailsErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void GetPostTaklif(String DaneshAmoozId,String Mah) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/TakalifeDarsi?DaneshAmoozId="+"2"+"&mah="+Mah);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);

        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {

                                    //   Log.e("Login", "response : " + response.body().source().readUtf8());
                                    onCardTakalifCompleteClickListner.OnTakalifCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardTakalifErrorClickListner.OnTakalifErrored(anError.getErrorDetail());
                    }
                });


    }

    public static void SendPostForget(String Tell) {


        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidAccount/ForgottenPassword?Tell="+Tell+"&Type=Ovlia");
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardForgetCompleteClickListner.OnForgetCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardForgetErrorClickListner.OnForgetErrored(anError.getErrorDetail());
                    }
                });


    }

  /*  public static void SendPostHomeWork(String DaneshAmoozId,String BarnameHaftegiId,String Date,int Hafte,String OnvaneFile,String TozihateFile,String File) {

        Log.e("Sabt","http://soldaschool.ir/Home/TaklifeKelasi?DaneshAmoozId="+DaneshAmoozId+"&BarnameHaftegiId="+BarnameHaftegiId+"&Date="+Date+"&Hafte="+Hafte+"&OnvaneFile="+OnvaneFile+"&TozihateFile="+TozihateFile+"&File="+File);
        ANRequest.MultiPartBuilder a=new ANRequest.MultiPartBuilder("http://soldaschool.ir/Home/TaklifeKelasi");
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookieFile(a);
        a.addMultipartParameter("DaneshAmoozId",DaneshAmoozId);
        a.addMultipartParameter("BarnameHaftegiId", BarnameHaftegiId);
        a.addMultipartParameter("Date", Date);
        a.addMultipartParameter("Hafte", Hafte+"");
        a.addMultipartParameter("OnvaneFile", OnvaneFile);
        a.addMultipartParameter("TozihateFile", TozihateFile);
        a.addMultipartFile("File",new File(File));
        a.build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.e("Image", " timeTakenInMillis : " + timeTakenInMillis);
                        Log.e("Image", " bytesSent : " + bytesSent);
                        Log.e("Image", " bytesReceived : " + bytesReceived);
                        Log.e("Image", " isFromCache : " + isFromCache);
                    }
                })

                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardHomeWorkCompleteClickListner.OnHomeWorkCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardHomeWorkErrorClickListner.OnHomeWorkErrored(anError.getErrorDetail());
                    }
                });


    }
*/

    public static void SendPostChangePass(String OldPassword,String NewPassword) {


        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidAccount/ChangePassword?OldPassword="+OldPassword+"&NewPassword="+NewPassword);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardChangePassCompleteClickListner.OnChangePassCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardChangePassErrorClickListner.OnChangePassErrored(anError.getErrorDetail());
                    }
                });


    }

    public static void SendPostLogOut() {


        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidAccount/LogOut");
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardLogOutCompleteClickListner.OnLogOutCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardLogOutErrorClickListner.OnLogOutErrored(anError.getErrorDetail());
                    }
                });


    }




    public static void SendPostSendChat(String Text,String From_Id,String To_Id) {


        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/ErsalePayamBe?Text="+Text+"&From_Id="+From_Id+"&To_Id="+To_Id);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardSendChatCompleteClickListner.OnSendChatCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardSendChatErrorClickListner.OnSendChatErrored(anError.getErrorDetail());
                    }
                });


    }




    public static void SendPostReadChat(String From_Id,String To_Id,int page) {
        Log.e("List","http://soldaschool.ir/api/AndroidService/DaryafteChat?From_Id="+From_Id+"&To_Id="+To_Id+"&page="+page);

        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/DaryafteChat?From_Id="+From_Id+"&To_Id="+To_Id+"&page="+page);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardReadChatCompleteClickListner.OnReadChatCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardReadChatErrorClickListner.OnReadChatErrored(anError.getErrorDetail());
                    }
                });


    }




    public static void SendPostAbsent(String DaneshAmoozId,String BarnameHaftegiId,String Takhir,String Tozih,int Hafte,String Tarikh) {

        Log.e("Sabt","http://soldaschool.ir/api/AndroidService/HuzurGhiabeKelasi?DaneshAmoozId="+DaneshAmoozId+"&BarnameHaftegiId="+BarnameHaftegiId+"&Takhir="+Takhir+"&Tozih="+Tozih+"&Hafte="+Hafte+"&Tarikh="+Tarikh);

        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/MoavenHuzurGhiabeKelasi?DaneshAmoozId="+DaneshAmoozId+"&BarnameHaftegiId="+BarnameHaftegiId+"&Takhir="+Takhir+"&Tozih="+Tozih+"&Hafte="+Hafte+"&Tarikh="+Tarikh);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardAbsentCompleteClickListner.OnAbsentCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardAbsentErrorClickListner.OnAbsentErrored(anError.getErrorDetail());
                    }
                });


    }


    public static void SendPostReadElanat(String KelasId) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/MoavenRoyateFarakhan?KelasId="+KelasId);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardReadElanatCompleteClickListner.OnReadElanatCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardReadElanatErrorClickListner.OnReadElanatErrored(anError.getErrorDetail());
                    }
                });


    }

    public static void SendPostSendElanat(String KarmandId,String KelasId,String Movzoo,String Matn,String TarikheFarakhan) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/SabteFarakhan?KarmandId="+KarmandId+"&KelasId="+KelasId+"&Movzoo="+Movzoo+"&Matn="+Matn+"&TarikheFarakhan="+TarikheFarakhan);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardSendElanatCompleteClickListner.OnSendElanatCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardSendElanatErrorClickListner.OnSendElanatErrored(anError.getErrorDetail());
                    }
                });


    }







    public static void SendPostTashvighoTanbiheKelasi(String DaneshAmoozId,String Emtiaz,String Tozihat,String Hafte,String Tarikh) {
        final ANRequest.PostRequestBuilder a=new ANRequest.PostRequestBuilder("http://soldaschool.ir/api/AndroidService/TashvighoTanbiheKelasi?DaneshAmoozId="+DaneshAmoozId+"&BarnameHaftegiId=-1&Emtiaz="+Emtiaz+"&Tozihat="+Tozihat+"&Hafte="+Hafte+"&Tarikh="+Tarikh);
        a.setPriority(Priority.HIGH);
        a.doNotCacheResponse();
        App.getCookie(a);
        a.build()


                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if (response != null) {
                            if (response.isSuccessful()) {
                                Log.e("Login", "response is successful");
                                try {
                                    onCardTashvighoTanbiheKelasiCompleteClickListner.OnTashvighoTanbiheKelasiCompleteed(response.body().source().readUtf8());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Login", "response is not successful : "+response.code());
                            }
                        } else {
                            Log.e("Login", "response is null");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Login", anError.getErrorDetail());
                        onCardTashvighoTanbiheKelasiErrorClickListner.OnTashvighoTanbiheKelasiErrored(anError.getErrorDetail());
                    }
                });


    }
    public interface OnLoginCompleteListner {
        void OnLoginCompleteed(String response);
    }

    public void setOnLoginCompleteListner(OnLoginCompleteListner onCardLoginCompleteClickListner) {
        this.onCardLoginCompleteClickListner = onCardLoginCompleteClickListner;
    }
 public interface OnLoginErrorListner {
        void OnLoginErrored(String response);
    }

    public void setOnLoginErrorListner(OnLoginErrorListner onCardLoginErrorCompleteClickListner) {
        this.onCardLoginErrorClickListner = onCardLoginErrorCompleteClickListner;
    }

    public interface OnListeDaneshAmoozaneMadreseCompleteListner {
        void OnListeDaneshAmoozaneMadreseCompleteed(String response);
    }

    public void setOnListeDaneshAmoozaneMadreseCompleteListner(OnListeDaneshAmoozaneMadreseCompleteListner onCardListeDaneshAmoozaneMadreseCompleteClickListner) {
        this.onCardListeDaneshAmoozaneMadreseCompleteClickListner = onCardListeDaneshAmoozaneMadreseCompleteClickListner;
    }

    public interface OnListeDaneshAmoozaneMadreseErrorListner {
        void OnListeDaneshAmoozaneMadreseErrored(String response);
    }

    public void setOnListeDaneshAmoozaneMadreseErrorListner(OnListeDaneshAmoozaneMadreseErrorListner onCardListeDaneshAmoozaneMadreseErrorCompleteClickListner) {
        this.onCardListeDaneshAmoozaneMadreseErrorClickListner = onCardListeDaneshAmoozaneMadreseErrorCompleteClickListner;
    }



    public interface OnListeMaghateBeHamraheKelasHaCompleteListner {
        void OnListeMaghateBeHamraheKelasHaCompleteed(String response);
    }

    public void setOnListeMaghateBeHamraheKelasHaCompleteListner(OnListeMaghateBeHamraheKelasHaCompleteListner onCardListeMaghateBeHamraheKelasHaCompleteClickListner) {
        this.onCardListeMaghateBeHamraheKelasHaCompleteClickListner = onCardListeMaghateBeHamraheKelasHaCompleteClickListner;
    }

    public interface OnListeMaghateBeHamraheKelasHaErrorListner {
        void OnListeMaghateBeHamraheKelasHaErrored(String response);
    }

    public void setOnListeMaghateBeHamraheKelasHaErrorListner(OnListeMaghateBeHamraheKelasHaErrorListner onCardListeMaghateBeHamraheKelasHaErrorCompleteClickListner) {
        this.onCardListeMaghateBeHamraheKelasHaErrorClickListner = onCardListeMaghateBeHamraheKelasHaErrorCompleteClickListner;
    }



    public interface OnListDorosCompleteListner {
        void OnListDorosCompleteed(String response);
    }

    public void setOnListDorosCompleteListner(OnListDorosCompleteListner onCardListDorosCompleteClickListner) {
        this.onCardListDorosCompleteClickListner = onCardListDorosCompleteClickListner;
    }

    public interface OnListDorosErrorListner {
        void OnListDorosErrored(String response);
    }

    public void setOnListDorosErrorListner(OnListDorosErrorListner onCardListDorosErrorCompleteClickListner) {
        this.onCardListDorosErrorClickListner = onCardListDorosErrorCompleteClickListner;
    }

    public interface OnListClassStudentsCompleteListner {
        void OnListClassStudentsCompleteed(String response);
    }

    public void setOnListClassStudentsCompleteListner(OnListClassStudentsCompleteListner onCardListClassStudentsCompleteClickListner) {
        this.onCardListClassStudentsCompleteClickListner = onCardListClassStudentsCompleteClickListner;
    }

    public interface OnListClassStudentsErrorListner {
        void OnListClassStudentsErrored(String response);
    }

    public void setOnListClassStudentsErrorListner(OnListClassStudentsErrorListner onCardListClassStudentsErrorCompleteClickListner) {
        this.onCardListClassStudentsErrorClickListner = onCardListClassStudentsErrorCompleteClickListner;
    }


    public interface OnAbsentCompleteListner {
        void OnAbsentCompleteed(String response);
    }

    public void setOnAbsentCompleteListner(OnAbsentCompleteListner onCardAbsentCompleteClickListner) {
        this.onCardAbsentCompleteClickListner = onCardAbsentCompleteClickListner;
    }

    public interface OnAbsentErrorListner {
        void OnAbsentErrored(String response);
    }

    public void setOnAbsentErrorListner(OnAbsentErrorListner onCardAbsentErrorCompleteClickListner) {
        this.onCardAbsentErrorClickListner = onCardAbsentErrorCompleteClickListner;
    }

    public interface OnElanatCompleteListner {
        void OnElanatCompleteed(String response);
    }

    public void setOnElanatCompleteListner(OnElanatCompleteListner onCardElanatCompleteClickListner) {
        this.onCardElanatCompleteClickListner = onCardElanatCompleteClickListner;
    }

    public interface OnElanatErrorListner {
        void OnElanatErrored(String response);
    }

    public void setOnElanatErrorListner(OnElanatErrorListner onCardElanatErrorCompleteClickListner) {
        this.onCardElanatErrorClickListner = onCardElanatErrorCompleteClickListner;
    }

    public interface OnTashvigCompleteListner {
        void OnTashvigCompleteed(String response);
    }

    public void setOnTashvigCompleteListner(OnTashvigCompleteListner onCardTashvigCompleteClickListner) {
        this.onCardTashvigCompleteClickListner = onCardTashvigCompleteClickListner;
    }

    public interface OnTashvigErrorListner {
        void OnTashvigErrored(String response);
    }

    public void setOnTashvigErrorListner(OnTashvigErrorListner onCardTashvigErrorCompleteClickListner) {
        this.onCardTashvigErrorClickListner = onCardTashvigErrorCompleteClickListner;
    }
    public interface OnGeybatCompleteListner {
        void OnGeybatCompleteed(String response);
    }

    public void setOnGeybatCompleteListner(OnGeybatCompleteListner onCardGeybatCompleteClickListner) {
        this.onCardGeybatCompleteClickListner = onCardGeybatCompleteClickListner;
    }

    public interface OnGeybatErrorListner {
        void OnGeybatErrored(String response);
    }

    public void setOnGeybatErrorListner(OnGeybatErrorListner onCardGeybatErrorCompleteClickListner) {
        this.onCardGeybatErrorClickListner = onCardGeybatErrorCompleteClickListner;
    }
    public interface OnClassListCompleteListner {
        void OnClassListCompleteed(String response);
    }

    public void setOnClassListCompleteListner(OnClassListCompleteListner onCardClassListCompleteClickListner) {
        this.onCardClassListCompleteClickListner = onCardClassListCompleteClickListner;
    }

    public interface OnClassListErrorListner {
        void OnClassListErrored(String response);
    }

    public void setOnClassListErrorListner(OnClassListErrorListner onCardClassListErrorCompleteClickListner) {
        this.onCardClassListErrorClickListner = onCardClassListErrorCompleteClickListner;
    }
    public interface OnClassListDetailsCompleteListner {
        void OnClassListDetailsCompleteed(String response);
    }

    public void setOnClassListDetailsCompleteListner(OnClassListDetailsCompleteListner onCardClassListDetailsCompleteClickListner) {
        this.onCardClassListDetailsCompleteClickListner = onCardClassListDetailsCompleteClickListner;
    }

    public interface OnClassListDetailsErrorListner {
        void OnClassListDetailsErrored(String response);
    }

    public void setOnClassListDetailsErrorListner(OnClassListDetailsErrorListner onCardClassListDetailsErrorCompleteClickListner) {
        this.onCardClassListDetailsErrorClickListner = onCardClassListDetailsErrorCompleteClickListner;
    }
    public interface OnTakalifCompleteListner {
        void OnTakalifCompleteed(String response);
    }

    public void setOnTakalifCompleteListner(OnTakalifCompleteListner onCardTakalifCompleteClickListner) {
        this.onCardTakalifCompleteClickListner = onCardTakalifCompleteClickListner;
    }

    public interface OnTakalifErrorListner {
        void OnTakalifErrored(String response);
    }

    public void setOnTakalifErrorListner(OnTakalifErrorListner onCardTakalifErrorCompleteClickListner) {
        this.onCardTakalifErrorClickListner = onCardTakalifErrorCompleteClickListner;
    }
    public interface OnForgetCompleteListner {
        void OnForgetCompleteed(String response);
    }

    public void setOnForgetCompleteListner(OnForgetCompleteListner onCardForgetCompleteClickListner) {
        this.onCardForgetCompleteClickListner = onCardForgetCompleteClickListner;
    }

    public interface OnForgetErrorListner {
        void OnForgetErrored(String response);
    }

    public void setOnForgetErrorListner(OnForgetErrorListner onCardForgetErrorCompleteClickListner) {
        this.onCardForgetErrorClickListner = onCardForgetErrorCompleteClickListner;
    }
    public interface OnChangePassCompleteListner {
        void OnChangePassCompleteed(String response);
    }

    public void setOnChangePassCompleteListner(OnChangePassCompleteListner onCardChangePassCompleteClickListner) {
        this.onCardChangePassCompleteClickListner = onCardChangePassCompleteClickListner;
    }

    public interface OnChangePassErrorListner {
        void OnChangePassErrored(String response);
    }

    public void setOnChangePassErrorListner(OnChangePassErrorListner onCardChangePassErrorCompleteClickListner) {
        this.onCardChangePassErrorClickListner = onCardChangePassErrorCompleteClickListner;
    }
    public interface OnLogOutCompleteListner {
        void OnLogOutCompleteed(String response);
    }

    public void setOnLogOutCompleteListner(OnLogOutCompleteListner onCardLogOutCompleteClickListner) {
        this.onCardLogOutCompleteClickListner = onCardLogOutCompleteClickListner;
    }

    public interface OnLogOutErrorListner {
        void OnLogOutErrored(String response);
    }

    public void setOnLogOutErrorListner(OnLogOutErrorListner onCardLogOutErrorCompleteClickListner) {
        this.onCardLogOutErrorClickListner = onCardLogOutErrorCompleteClickListner;
    }
    public interface OnChatListCompleteListner {
        void OnChatListCompleteed(String response);
    }

    public void setOnChatListCompleteListner(OnChatListCompleteListner onCardChatListCompleteClickListner) {
        this.onCardChatListCompleteClickListner = onCardChatListCompleteClickListner;
    }

    public interface OnChatListErrorListner {
        void OnChatListErrored(String response);
    }

    public void setOnChatListErrorListner(OnChatListErrorListner onCardChatListErrorCompleteClickListner) {
        this.onCardChatListErrorClickListner = onCardChatListErrorCompleteClickListner;
    }
    public interface OnSendChatCompleteListner {
        void OnSendChatCompleteed(String response);
    }

    public void setOnSendChatCompleteListner(OnSendChatCompleteListner onCardSendChatCompleteClickListner) {
        this.onCardSendChatCompleteClickListner = onCardSendChatCompleteClickListner;
    }

    public interface OnSendChatErrorListner {
        void OnSendChatErrored(String response);
    }

    public void setOnSendChatErrorListner(OnSendChatErrorListner onCardSendChatErrorCompleteClickListner) {
        this.onCardSendChatErrorClickListner = onCardSendChatErrorCompleteClickListner;
    }

    public interface OnReadChatCompleteListner {
        void OnReadChatCompleteed(String response);
    }

    public void setOnReadChatCompleteListner(OnReadChatCompleteListner onCardReadChatCompleteClickListner) {
        this.onCardReadChatCompleteClickListner = onCardReadChatCompleteClickListner;
    }

    public interface OnReadChatErrorListner {
        void OnReadChatErrored(String response);
    }

    public void setOnReadChatErrorListner(OnReadChatErrorListner onCardReadChatErrorCompleteClickListner) {
        this.onCardReadChatErrorClickListner = onCardReadChatErrorCompleteClickListner;
    }
    public interface OnReadElanatCompleteListner {
        void OnReadElanatCompleteed(String response);
    }

    public void setOnReadElanatCompleteListner(OnReadElanatCompleteListner onCardReadElanatCompleteClickListner) {
        this.onCardReadElanatCompleteClickListner = onCardReadElanatCompleteClickListner;
    }

    public interface OnReadElanatErrorListner {
        void OnReadElanatErrored(String response);
    }

    public void setOnReadElanatErrorListner(OnReadElanatErrorListner onCardReadElanatErrorCompleteClickListner) {
        this.onCardReadElanatErrorClickListner = onCardReadElanatErrorCompleteClickListner;
    }

    public interface OnSendElanatCompleteListner {
        void OnSendElanatCompleteed(String response);
    }

    public void setOnSendElanatCompleteListner(OnSendElanatCompleteListner onCardSendElanatCompleteClickListner) {
        this.onCardSendElanatCompleteClickListner = onCardSendElanatCompleteClickListner;
    }

    public interface OnSendElanatErrorListner {
        void OnSendElanatErrored(String response);
    }

    public void setOnSendElanatErrorListner(OnSendElanatErrorListner onCardSendElanatErrorCompleteClickListner) {
        this.onCardSendElanatErrorClickListner = onCardSendElanatErrorCompleteClickListner;
    }
    public interface OnTashvighoTanbiheKelasiCompleteListner {
        void OnTashvighoTanbiheKelasiCompleteed(String response);
    }

    public void setOnTashvighoTanbiheKelasiCompleteListner(OnTashvighoTanbiheKelasiCompleteListner onCardTashvighoTanbiheKelasiCompleteClickListner) {
        this.onCardTashvighoTanbiheKelasiCompleteClickListner = onCardTashvighoTanbiheKelasiCompleteClickListner;
    }

    public interface OnTashvighoTanbiheKelasiErrorListner {
        void OnTashvighoTanbiheKelasiErrored(String response);
    }

    public void setOnTashvighoTanbiheKelasiErrorListner(OnTashvighoTanbiheKelasiErrorListner onCardTashvighoTanbiheKelasiErrorCompleteClickListner) {
        this.onCardTashvighoTanbiheKelasiErrorClickListner = onCardTashvighoTanbiheKelasiErrorCompleteClickListner;
    }
}
