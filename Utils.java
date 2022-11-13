package com.example.android.billsplittingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

/**
 * Created by praty on 23-06-2018.
 */

public class Utils {
    public static String MyPREFERENCES = "my_preferences";


    public static String URL = "http://phpstack-127383-542740.cloudwaysapps.com/api/v1/";

    public static String USER_IMAGE_URL = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/users/";
    public static String FAMILY_IMAGE_URL = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/familymembers/";
    public static String DEAD_PERSON_GALLERY_URL = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/gallary/";
    public static String COFFINBOX_IMAGE_URL = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/coffinbox/";
    public static String FUNURAL_IMAGE_URL = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/funeralvans/";
    public static String FINAL_WORDS = "http://phpstack-127383-509750.cloudwaysapps.com/public/uploads/finalwords/";

    public static String REGISTER = URL +"userregister";
    public static String LOGIN = URL + "userlogin";
    public static String RESET_PASSWORD = URL + "resetpassword";
    public static String CHECK_EMAIL = URL + "forgotpassword";
    public static String DEAD_PERSONS_LIST = URL  + "deadpersonslist";
    public static String DEAD_PERSON_DETAILS=URL+"deadpersondetails";
    public static String DEAD_PERSON_CONDOLENCES_LIST = URL+"listcondolences";
    public static String SAVE_CONDOLENCE=URL+"savecondolence";
    public static String CREATE_GROUP=URL+"creategroup";
    public static String GET_MY_TESTIMONIALS_LIST = URL +"listtestimonies";
    public static String UPDATE_TESTIMONIAL = URL + "updatetestimony";
    public static String DELETE_MY_TESTIMONY = URL + "deletetestimony";
    public static String COFFIN_BOXES_LIST = URL + "listcoffinboxes";
    public static String GET_MY_FINAL_WORDS = URL + "listfinalwords";
    public static String SAVE_FINAL_WORDS = URL+"savefinalwords";
    public static String UPDATE_FINAL_WORDS = URL + "updatefinalwords";
    public static String DELETE_MY_FINALWORD = URL + "deleteFinalWords";
    public static String GET_MY_PROFILE = URL + "getprofiledetails";
    public static String GET_EDIT_PROFILE = URL + "edituserprofile";
    public static String ABOUT_US = URL + "aboutus";
    public static String PRIVACY_POLICY = URL + "privacypolicy";
    public static String GET_RATING = URL + "getrating";
    public static String ADD_RATING = URL+"addrating";
    public static String GET_CHURCH_MEMBERS = URL + "getchurchmembers";
    public static String GET_ORGANIZATION_MEMBERS = URL + "getorganizationmembers";
    public static String GET_COMMUNITY_MEMBERS = URL + "getcommunitymembers";
    public static String GET_FAMILY_MEMBERS_LIST = URL + "getfamilymembers";
    public static String ADD_FAMILY_MEMBER = URL + "addfamilymember";
    public static String DELETE_FAMILY_MEMBER = URL + "deletefamilymember";
    public static String GET_COUNTRIES = URL + "getcountries";
    public static String GET_STATES = URL + "getstates";
    public static String GET_CITIES = URL + "getcities";
    public static String UPDATE_PROFILE = URL + "updateuserprofile";
    public static String CHANGE_PASSWORD = URL + "changepassword";
    public static String GET_FUNERAL_VANS = URL + "getfuneralvans";
    public static String TERMS_AND_CONDITIONS = URL + "termsconditions";
    public static String COFFIN_BOX_DETAILS = URL + "coffinboxdetails";
    public static String DECLARE_AS_DEAD = URL + "declaredeadperson";
    public static String GET_ORDERED_COFFIN_BOXES_LIST = URL + "getorderedcoffinboxes";
    public static String VIEW_ORDERED_COFFIN_BOX = URL + "vieworderedcoffinbox";
    public static String SOCIAL_LOGINS = URL + "sociallogins";
    public static String GET_DEAD_PERSON_GALLERY = URL + "getgallaryimages";
    public static String GET_DEAD_PERSON_TOMB_ADDRESS = URL + "getgeotag";
    public static String CHECK_GEO_TAG = URL + "checkgeotag";
    public static String ADD_GEO_TAG = URL + "addgeotag";
    public static String GET_MY_GALLERY = URL + "getmygallaryimages";
    public static String DELETE_MY_IMAGE = URL + "deletemygallaryimages";
    public static String UPLOAD_IMAGE_MY_GALLERY = URL +"uploadmygalleryimage";
    public static String UPLOAD_IMAGE_DEAD_PERSON_GALLERY = URL + "uploaddeadgalleryimage";
    public static String UPLOAD_MY_USER_PIC = URL + "uploaduserprofileimage";


    private static ProgressDialog m_Dialog ;
    public static ProgressDialog showProgressDialog(Context context, String message){
        m_Dialog   = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }

    public static void dissmisProgress() {
        if (m_Dialog != null){
            m_Dialog.dismiss();
        }

    }

    public static void showProgress() {
        if (m_Dialog != null)
            m_Dialog.show();
    }

}
