package hailongs.cn.utils;

import java.io.IOException;
import java.util.HashSet;

import hailongs.cn.bean.Post;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dhl on 2016/11/29.
 */

public interface Apis {

    @GET("data")
    Observable<Post> getPostList(
            @Query("type") String type,
            @Query("count") int count,
            @Query("page") int page
    );

    class Helper {
        public static Apis getSimpleApi() {
            return DefautReftAdapter.getDefautReftAdapter(Constants.baseUrl).build().create(Apis.class);
        }
    }

    class CookieHolder {
        HashSet<String> cookies = new HashSet<String>();
        private static CookieHolder holder;

        private CookieHolder() {
        }

        public static CookieHolder getInstance() {
            if (holder == null) {
                holder = new CookieHolder();
            }
            return holder;
        }

        public HashSet<String> getCookies() {
            return cookies;
        }

        public void setCookies(HashSet<String> cookies) {
            this.cookies = cookies;
        }
    }

    class DefautReftAdapter {
        private static String cookieString = null;

        public static String getCookieString() {
            return cookieString;
        }

        public static void setCookieString(String cookieString) {
            DefautReftAdapter.cookieString = cookieString;
        }

        public static class AddCookiesInterceptor implements Interceptor {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                HashSet<String> preferences = CookieHolder.getInstance().getCookies();
                for (String cookie : preferences) {
                    builder.addHeader("Cookie", cookie);
                }
                return chain.proceed(builder.build());
            }
        }

        public static class ReceivedCookiesInterceptor implements Interceptor {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());

                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    HashSet<String> cookies = new HashSet();

                    for (String header : originalResponse.headers("Set-Cookie")) {
                        cookies.add(header);
                    }


                    CookieHolder.getInstance().setCookies(cookies);
                }
                return originalResponse;
            }
        }

        public static Retrofit.Builder getDefautReftAdapter(String baseUrl) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor())
                    .build();
            return new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .client(client);
        }
    }


}
