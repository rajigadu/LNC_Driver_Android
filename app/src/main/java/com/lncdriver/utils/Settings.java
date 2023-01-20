package com.lncdriver.utils;

import android.app.Application;

/**
 * Created by narip on 2/4/2017.
 */
public class Settings extends Application {
    public static final String INSTANCE_NAME = "com.haystack";
    public static final String API_KEY = "ANAADhaystack";
    //public static String URLMAINDATA = "http://me911.com/new/haystack/android/";
    //public static String URLMAINDATA = "http://anaadit.net/late_night_chauffeurs/android/driver/";
    //public static String URLIMAGEBASE = "http://anaadit.net/late_night_chauffeurs/uploads/";

    //main base url

//    public static final String URL_MAIN_DATA_IOS = "https://latenightchauffeurs.com/lnc-administrator/ios/driver/";

//    public static final String URL_MAIN = "https://latenightchauffeurs.com/lnc-administrator/";

//    public static final String URL_MAIN = "https://lnc.latenightchauffeurs.com/lnc-administrator/";
    public static final String URL_MAIN = "https://lnc.latenightchauffeurs.com/lnc-administrator/android-test/";
    //public static final String URL_MAIN = "https://lnc.latenightchauffeurs.com/lnc-administrator/ios-test/driver/";

    public static final String URL_MAIN_DATA = URL_MAIN + "driver/";
    public static final String URLIMAGEBASE = URL_MAIN+"uploads/";

    public static final String GOOGLE_DESTINATIOON = "https://maps.googleapis.com/maps/api/";


    //beta base url
    //public static String URLMAINDATA = "http://latenightchauffeurs.com/lnc-administrator/beta/android/driver/";
    // public static String URLIMAGEBASE = " http://latenightchauffeurs.com/lnc-administrator/beta/uploads/";

    public static final String URL_LOGIN = "login.php";
    public static final String URL_FORGETPASSWORD = "forgotpassword.php";
    public static final String URL_REGISTRATION = "signup.php";
    public static final String URL_CHANGEPASSWORD = "change-password.php";
    public static final String URL_PRIVACY_POLICY = URL_MAIN+"android/privay-policy.php";
    public static final String URL_TERMS_CONDITIONS = URL_MAIN+"android/terms.php";
    public static final String URL_SERVICETYPE = "driver-type-new.php";
    public static final String URL_DRIVERDASHBOARD = "driver-dashboard.php";
    public static final String URL_DRIVER_ONLINE_REQUEST = "update-online-status.php";
    public static final String URL_DRIVER_ONLINE_STATUS_REQUEST = "online-status.php";
    public static final String URL_DRIVERLOCATIONUPDATE = "update-driver-location.php";
    public static final String URL_DEVICE_TOKEN_UPDATION = "update-device-token.php";
    public static final String URL_RIDEHISTORY = "ride-histroy.php";
    public static final String URL_DRIVERACCEPT = "driver-response.php";
    public static final String URL_PARTNERACCEPT = "partner-response.php";
    public static final String URL_CURRENTRIDE = "current-ride.php";
    public static final String URL_CURRENTRIDES = "current-rides.php";
    public static final String URL_NEXT_RIDE = "next-ride-time.php";

    public static final String URL_PAYMENTHISTORY = "payment-history-complete-cancel.php";

    public static final String URL_ADDBANKACCOUNT = "add-bank-detail.php";
    public static final String URL_CONTACTUS = "contact-us.php";
    public static final String URL_EDIT_VEHICLE_INFO = "edit-vehicle-info.php";
    public static final String URL_GETVEHICLEINFO = "vehicle-info.php";
    public static final String URL_EDIT_PERSONAL_INFO = "edit-profile-info.php";
    public static final String URL_PARTNERACTIVATE = "update-partner-status-new.php";

    public static final String URL_USERCHAT = "chat.php";
    public static final String URL_DRIVERCHAT = "driver-chat.php";
    public static final String URL_COMPLETERIDE = "ride-complete.php";
    public static final String URL_MANAGE_PARTNERS = "manage-partner-list.php";
    public static final String URL_ADDPARTNER = "add-partner-new.php";
    public static final String URL_GETPARTNERORDRIVER = "partner-detail.php";
    public static final String URL_PARTNERLOOKING = "looking-partner.php";
    public static final String URL_PAYMENTDEDUCTION = "do-payment.php";
    public static final String URL_DRIVERROLE = "driver-role.php";
    public static final String URL_ACTIVEPARTNER = "active-partner-new.php";
    public static final String URL_AUTHENTICATEPARTNER = "driver-type-new.php";
    public static final String URL_INTIMATIONTOPARTNER = "notify-partner.php";
    public static final String URL_STOPLOCATIONSLIST = "num-stops-addres-list.php";
    public static final String URL_EDIT_BANK_ACCOUNT = "get-account-detail.php";
    public static final String URL_DRIVERINCOMINGFUTURERIDE = "nearest_available_future_ride.php";
    public static final String URL_PARTNERINCOMINGFUTURERIDE = "nearest_available_partner_future_ride.php";
    public static final String URL_DRIVERFUTURERIDES = "driver_accepted_future_ride_list.php";
    public static final String URL_PARTNERFUTURERIDES = "partner_accepted_future_ride_list.php";
    public static final String URL_DRIVERACCEPTFUTURERIDE = "future-ride-accept_by_driver_new.php";
    public static final String URL_PARTNERACCEPTFUTURERIDE = "future-ride-accept_by_partner.php";
    public static final String URL_DRIVERFUTURERIDEDETAILS = "future-ride-detail-driver.php";
    public static final String URL_ESTIMATE_PRICE = "estimation-price.php";
    public static final String URL_PARTNERFUTURERIDEDETAILS = "future-ride-detail-partner.php";
    public static final String URL_FUTURERIDECOMPLETE = "ride-complete-future.php";
    public static final String URL_FUTUREPAYMENTDEDUCTION = "do-payment-future.php";
    public static final String URL_FRIDEINTIMATIONTOPARTNER = "notify-partner-future.php";
    public static final String URL_FUTURE_RIDE_HISTORY = "future-ride-history-driver.php";
    public static final String URL_PARTNERFUTURERIDEHISTORY = "future-ride-history-partner.php";
    public static final String URL_FUTURERIDESTART = "future-ride-start.php";
    public static final String URL_CURRENTRIDESTART = "current-ride-start.php";
    public static final String URL_CANCELRIDE = "cancel-ride.php";
    public static final String URL_CANCELFUTURERIDE = "future-ride-cancel-by-driver.php";
    public static final String URL_WAITING_CHARGE_START = "waiting-charges-start.php";
    public static final String URL_PENDINGCURRENTRIDES = "offline-available-rides.php";
    public static final String URL_FUTURE_RIDE_CANCEL_BY_PARTNER = "future-ride-cancel-by-partner.php";
    public static final String URL_CURRENT_RIDE_CANCEL_BY_PARTNER = "ride-cancel-by-partner.php";
    public static final String URL_FEED_BACK = "user-rating.php";
    public static final String URL_PARTNER_LIST = "partner-list-new.php";
    public static final String URL_DUMMY_PARTNER_FUTURE_ACCEPT = "dummy-partner-future-accept-new.php";
    public static final String URL_GET_BY_WEEK_PAYMENT_HISTORY = "week-count.php";
    public static final String URL_WEEK_REPORT = "weekly-report.php";
    public static final String URL_UNPLANNED_STOPS = "unplanned-stops.php";
    public static final String URL_ADD_WAITING_TIME = "add-waiting-time.php";

    public static String NETWORK_STATUS = "Online";
    public static String NETWORK_TYPE = "";
    public static String USERNAME = "username";
}

