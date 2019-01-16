package com.app.barber.util;

import com.app.barber.util.iface.OnItemClickListener;

/**
 * Created by harish on 16/10/18.
 */

public class GlobalValues {
    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static String APPLICATION_PLAYSTORE_URL_CUSTOMER = "https://play.google.com/store/apps/details?id=\" + \"com.app.trimcheck.customer";
    public static String APPLICATION_PLAYSTORE_URL_BARBER = "https://play.google.com/store/apps/details?id=\" + \"com.app.trimcheck.barber";

    public interface RequestCodes {
        int TIME_SELECTION = 921;
        int ADD_MOBILE = 922;
        int ADDRESS_SEARCH = 923;
        int PERMISSIONS_REQUEST_CAMERA = 924;
        int REQUEST_TAKE_IMAGE = 925;
        int GALLERY_REQUEST = 926;
        int LOCATION_PICKER = 213;
        int PERMISSIONS_REQUEST_ = 927;
        int REQUEST_PERMISSION_FOR_LOCATION = 928;
        int MESSAGE = 929;
        int CALL = 930;
        int REQUEST_PHONE_CALL = 931;
        int CANCEL = 932;
    }

    public interface BARBER_TYPES {
        int BARBER = 1;
        int CALLOUT_BARBER = 2;
    }

    public interface PaymentTypes {
        String BOTH = "3";
        String CASH = "2";
        String CARD = "1";
    }

    public interface PaymentModes {
        String CASH = "Cash";
        String CARD = "Card";
    }

    public interface RatingTypes {
        int PUNCTUALITY = 4;
        int EXPERTIES = 1;
        int VALUE = 3;
        int HYGINENE = 2;
    }

    public interface NotificationType {
        int CalloutCompleted = 5;
        int BookingCompleted = 3;
        int BookingCanceled = 4;
    }

    public interface DateFormats {
        final String APPOINTMENT_FORMAT = "EEEE,MMMM YY";
        String DEFAULT_FORMAT_DATE = "MM/dd/yyyy";
        String DEFAULT_FORMAT_DATE_TIME ="MM/dd/yyyy hh:mm:ss" ;
    }

    public static class Font {
        public static final String COMFORTAA_BOLD = "fonts/Inter-UI-Medium.ttf";
        public static final String COMFORTAA_LIGHT = "fonts/Poppins-Light.ttf";
        public static final String COMFORTAA_REGULAR = "fonts/Inter-UI-Regular.ttf";
    }

    public static class CONSTANTS {
        public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
        public static final String CLIENT_ID = "2";
        public static final String CLIENT_SECRET_KEY = "HzMMk2fbmxUx8nCEsrTawxHCHXGfdIHmMubq6QyI";
        public static final int RADIO_CHECKED = 1;
    }

    public interface KEYS {
        String isLogedIn = "isUserLogedin";
        String USER_ID = "userId";
        String USER_NAME = "userName";
        String IS_EDIT = "isEdit";
        String SELECTED_DAY = "selectedDay";
        String PLACE_DETAIL = "";
        String SERVICE_DETAIL = "serviceDetail";
        String TITLE = "title";
        String PRIVACY = "Privacy";
        String TERMS = "Terms";
        String BARBER_DETAIL = "barberDetail";
        String BOOKING_DATA = "bookingData";
        String PRE_BOOKING_RESPONSE_DATA = "PreBookingResponse";
        String APPOINTMENT_STATUS_DETAIL = "AppointmetDetail";
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String BOOKING_TYPE = "bookingType";
        String NOTIFICATION_DATA = "notificationData";
        String SPECIALISATION = "specialisation";
        String EXTRA_DIALOG_ID = "dialogId";
        String CURRENT_ADDRESS = "isCurrentAddress";
        String DETAIL = "detail";

        String OTHER_IMAGE ="userImage" ;
    }

    public interface TIME_DURATIONS {
        int SMALL = 100;
        int MEDIUS = 300;
        int LARGE = 500;
        int EXTRA_LARGE = 3000;
    }

    public interface ClickOperations {
        int MORE_OPRION_CLICK = 1101;
        int ADD_TIME_CLICK = 1102;
        int SERVICE_DETAIL = 1103;
        int SERVICE_DELETE = 1104;
        int SERVICE_BOOK = 1105;
        int APAPTER_BOTTOM_DIALOG_CLICK = 1105;
        int ADD_IMAGE = 1106;

        int SHOW_DETAIL = 1107;
        int FAV_UNFAV = 1108;
        int NOTIFICATION_CLICK = 1109;
        int DETAILS = 1110;
    }

    public interface Extras {
        String ADD_MOBILE = "addMobile";
        String REGISTER_REQUEST_DATA = "requestModal";
        String COUNTRY_CODE = "countryCode";
        String VERIFIED = "verified";
        String FROM = "from";
    }

    public interface UserTypes {
        int BARBER = 1;
        int CUSTOMER = 2;
    }
}
