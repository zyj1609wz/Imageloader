package com.zyj.app.imageload.load.data;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by ${zyj} on 2016/9/27.
 */

public class HttpUrlFetcher implements DataFetcher<InputStream> {
    private static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
    private final HttpUrlConnectionFactory connectionFactory;
    private HttpURLConnection urlConnection;
    private InputStream stream;
    private volatile boolean isCancelled;
    private static final int MAXIMUM_REDIRECTS = 5;   //最大重定向次数\
    private final ZUrl zUrl ;

    public HttpUrlFetcher( ZUrl zUrl){
        this( zUrl , DEFAULT_CONNECTION_FACTORY ) ;
    }

    public HttpUrlFetcher( ZUrl zUrl , HttpUrlConnectionFactory httpUrlConnectionFactory) {
        this.zUrl = zUrl ;
        this.connectionFactory = httpUrlConnectionFactory ;
    }

    @Override
    public InputStream loadData() throws Exception {
       return loadDataWithRedirects( zUrl.toURL() ,  0 , null , zUrl.getHeaders() ) ;
    }

    private InputStream loadDataWithRedirects(URL url, int redirects, URL lastUrl, Map<String, String> headers)
            throws IOException {
        if (redirects >= MAXIMUM_REDIRECTS) {    //如果重定向的次数达到5次，就抛出异常
            throw new IOException("Too many (> " + MAXIMUM_REDIRECTS + ") redirects!");
        } else {
            // Comparing the URLs using .equals performs additional network I/O and is generally broken.
            // See http://michaelscharf.blogspot.com/2006/11/javaneturlequals-and-hashcode-make.html.
            try {
                if (lastUrl != null && url.toURI().equals(lastUrl.toURI())) {
                    throw new IOException("In re-direct loop");
                }
            } catch (URISyntaxException e) {
                // Do nothing, this is best effort.
            }
        }
        urlConnection = connectionFactory.build(url);

        //添加请求参数
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            urlConnection.addRequestProperty(headerEntry.getKey(), headerEntry.getValue());
        }
        urlConnection.setConnectTimeout(2500);
        urlConnection.setReadTimeout(2500);
        urlConnection.setUseCaches(false);
        urlConnection.setDoInput(true);

        //setDoInput(boolean)参数值为true决定着当前链接可以进行数据读取，反之则不允许读取操作；
        // setDoOutput(boolean)参数值为true时决定着当前链接可以进行数据提交工作，反之则不允许。

        // Connect explicitly to avoid errors in decoders if connection fails.
        urlConnection.connect();
        if (isCancelled) {
            return null;
        }
        final int statusCode = urlConnection.getResponseCode();
        if (statusCode / 100 == 2) { //请求成功，这里为什么不用200，因为服务器响应成功为2XX，比如200,201,202等
            return getStreamForSuccessfulRequest(urlConnection);
        } else if (statusCode / 100 == 3) {
            //获取需要重定向的目标url
            String redirectUrlString = urlConnection.getHeaderField("Location");
            if (TextUtils.isEmpty(redirectUrlString)) {
                throw new IOException("Received empty or null redirect url");
            }
            URL redirectUrl = new URL(url, redirectUrlString);
            return loadDataWithRedirects(redirectUrl, redirects + 1, url, headers);
        } else {
            if (statusCode == -1) {
                throw new IOException("Unable to retrieve response code from HttpUrlConnection.");
            }
            throw new IOException("Request failed " + statusCode + ": " + urlConnection.getResponseMessage());
        }
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection urlConnection)
            throws IOException {
        if (TextUtils.isEmpty(urlConnection.getContentEncoding())) {  //服务器返回类型
            int contentLength = urlConnection.getContentLength();
            stream = ContentLengthInputStream.obtain(urlConnection.getInputStream(), contentLength);
        } else {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Got non empty content encoding: " + urlConnection.getContentEncoding());
            }
            stream = urlConnection.getInputStream();
        }
        return stream;
    }



    @Override
    public void cleanup() {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

    @Override
    public String getId() {
        return zUrl.getCacheKey();
    }

    @Override
    public void cancel() {
        // TODO: we should consider disconnecting the url connection here, but we can't do so directly because cancel is
        // often called on the main thread.
        isCancelled = true;
    }

    interface HttpUrlConnectionFactory {
        HttpURLConnection build(URL url) throws IOException;
    }

    private static class DefaultHttpUrlConnectionFactory implements HttpUrlConnectionFactory {
        @Override
        public HttpURLConnection build(URL url) throws IOException {
            return (HttpURLConnection) url.openConnection();
        }
    }


    /**http状态返回代码 2xx （成功）表示成功处理了请求的状态代码。
            200 （成功） 服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。
            201 （已创建） 请求成功并且服务器创建了新的资源。
            202 （已接受） 服务器已接受请求，但尚未处理。
            203 （非授权信息） 服务器已成功处理了请求，但返回的信息可能来自另一来源。
            204 （无内容） 服务器成功处理了请求，但没有返回任何内容。
            205 （重置内容） 服务器成功处理了请求，但没有返回任何内容。
            206 （部分内容） 服务器成功处理了部分 GET 请求。

    作者：La Sha
    链接：http://www.zhihu.com/question/20908415/answer/24823723
    来源：知乎
    著作权归作者所有，转载请联系作者获得授权。
     **/
}
