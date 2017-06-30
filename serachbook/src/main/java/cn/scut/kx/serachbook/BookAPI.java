package cn.scut.kx.serachbook;

/**
 * Created by DELL on 2017/1/19.
 */

public class BookAPI {
    final static String TAG_TITLE = "title";
    final static String TAG_COVER = "image";
    final static String TAG_AUTHOR = "author";
    final static String TAG_PUBLISHER = "publisher";
    final static String TAG_PUBLISH_DATE = "pubdate";
    final static String TAG_ISBN = "isbn13";
    final static String TAG_SUMMARY = "summary";
    final static String TAG_ERROR_CODE = "code";

    final static int RESPONSE_CODE_SUCCEED = 200;
    final static int RESPONSE_CODE_ERROR_BOOK_NOT_FOUND = 6000;
    final static int RESPONSE_CODE_ERROR_NET_EXCEPTION = 9999;

    final static String URL_ISBN_BASE = "https://api.douban.com/v2/book/isbn/";
}
