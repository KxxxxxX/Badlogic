package cn.scut.kx.serachbook;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL on 2017/1/19.
 */

public class BookInfo implements Parcelable{
    private String mTitle = "";
    private Bitmap mCover;
    private String mAuthor = "";
    private String mPublisher = "";
    private String mPublishDate = "";
    private String mISBN = "";
    private String mSummary = "";

    public static final Parcelable.Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.mTitle = source.readString();
            bookInfo.mCover = source.readParcelable(Bitmap.class.getClassLoader());
            bookInfo.mAuthor = source.readString();
            bookInfo.mPublisher = source.readString();
            bookInfo.mPublishDate = source.readString();
            bookInfo.mISBN = source.readString();
            bookInfo.mSummary = source.readString();
            return bookInfo;
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeParcelable(mCover, flags);
        dest.writeString(mAuthor);
        dest.writeString(mPublisher);
        dest.writeString(mPublishDate);
        dest.writeString(mISBN);
        dest.writeString(mSummary);
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Bitmap getmCover() {
        return mCover;
    }

    public void setmCover(Bitmap mCover) {
        this.mCover = mCover;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getmPublishDate() {
        return mPublishDate;
    }

    public void setmPublishDate(String mPublishDate) {
        this.mPublishDate = mPublishDate;
    }

    public String getmISBN() {
        return mISBN;
    }

    public void setmISBN(String mISBN) {
        this.mISBN = mISBN;
    }

    public String getmSummary() {
        return mSummary;
    }

    public void setmSummary(String mSummary) {
        this.mSummary = mSummary;
    }
}
