package com.app.barber.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by harish on 2/1/19.
 */

public class BarberRatingsResponseModel {

    /**
     * Message : Success
     * Status : 201
     * Response : {"AvgPuchRating":"3.00","AvgValueRating":"1.75","AvgHygieneRating":null,"AvgExpertiseRating":"2.00","AvgRating":"2.50","ReviewCount":"2","ReviewList":[{"m_Item1":"First review","m_Item2":"Test User","m_Item3":"http://barber.xicom.info/Uploads/ProfileImages/9f682873-7711-4476-949f-ee5c57943c59.jpg"},{"m_Item1":"First review","m_Item2":"Test User","m_Item3":"http://barber.xicom.info/Uploads/ProfileImages/9f682873-7711-4476-949f-ee5c57943c59.jpg"}]}
     */

    @SerializedName("Message")
    private String Message;
    @SerializedName("Status")
    private int Status;
    @SerializedName("Response")
    private ResponseBean Response;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class ResponseBean {
        /**
         * AvgPuchRating : 3.00
         * AvgValueRating : 1.75
         * AvgHygieneRating : null
         * AvgExpertiseRating : 2.00
         * AvgRating : 2.50
         * ReviewCount : 2
         * ReviewList : [{"m_Item1":"First review","m_Item2":"Test User","m_Item3":"http://barber.xicom.info/Uploads/ProfileImages/9f682873-7711-4476-949f-ee5c57943c59.jpg"},{"m_Item1":"First review","m_Item2":"Test User","m_Item3":"http://barber.xicom.info/Uploads/ProfileImages/9f682873-7711-4476-949f-ee5c57943c59.jpg"}]
         */

        @SerializedName("AvgPuchRating")
        private String AvgPuchRating;
        @SerializedName("AvgValueRating")
        private String AvgValueRating;
        @SerializedName("AvgHygieneRating")
        private String AvgHygieneRating;
        @SerializedName("AvgExpertiseRating")
        private String AvgExpertiseRating;
        @SerializedName("AvgRating")
        private String AvgRating;
        @SerializedName("ReviewCount")
        private String ReviewCount;
        @SerializedName("ReviewList")
        private List<ReviewListBean> ReviewList;

        public String getAvgPuchRating() {
            return AvgPuchRating;
        }

        public void setAvgPuchRating(String AvgPuchRating) {
            this.AvgPuchRating = AvgPuchRating;
        }

        public String getAvgValueRating() {
            return AvgValueRating;
        }

        public void setAvgValueRating(String AvgValueRating) {
            this.AvgValueRating = AvgValueRating;
        }

        public String getAvgHygieneRating() {
            return AvgHygieneRating;
        }

        public void setAvgHygieneRating(String AvgHygieneRating) {
            this.AvgHygieneRating = AvgHygieneRating;
        }

        public String getAvgExpertiseRating() {
            return AvgExpertiseRating;
        }

        public void setAvgExpertiseRating(String AvgExpertiseRating) {
            this.AvgExpertiseRating = AvgExpertiseRating;
        }

        public String getAvgRating() {
            return AvgRating;
        }

        public void setAvgRating(String AvgRating) {
            this.AvgRating = AvgRating;
        }

        public String getReviewCount() {
            return ReviewCount;
        }

        public void setReviewCount(String ReviewCount) {
            this.ReviewCount = ReviewCount;
        }

        public List<ReviewListBean> getReviewList() {
            return ReviewList;
        }

        public void setReviewList(List<ReviewListBean> ReviewList) {
            this.ReviewList = ReviewList;
        }

        public static class ReviewListBean {
            /**
             * m_Item1 : First review
             * m_Item2 : Test User
             * m_Item3 : http://barber.xicom.info/Uploads/ProfileImages/9f682873-7711-4476-949f-ee5c57943c59.jpg
             */

            @SerializedName("m_Item1")
            private String mItem1;
            @SerializedName("m_Item2")
            private String mItem2;
            @SerializedName("m_Item3")
            private String mItem3;

            public String getMItem1() {
                return mItem1;
            }

            public void setMItem1(String mItem1) {
                this.mItem1 = mItem1;
            }

            public String getMItem2() {
                return mItem2;
            }

            public void setMItem2(String mItem2) {
                this.mItem2 = mItem2;
            }

            public String getMItem3() {
                return mItem3;
            }

            public void setMItem3(String mItem3) {
                this.mItem3 = mItem3;
            }
        }
    }
}
