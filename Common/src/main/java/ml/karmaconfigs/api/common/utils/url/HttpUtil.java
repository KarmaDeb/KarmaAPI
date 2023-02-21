package ml.karmaconfigs.api.common.utils.url;

/*
 * This file is part of KarmaAPI, licensed under the MIT License.
 *
 *  Copyright (c) karma (KarmaDev) <karmaconfigs@gmail.com>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ml.karmaconfigs.api.common.utils.JavaVM;
import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.string.StringUtils;
import ml.karmaconfigs.api.common.utils.url.request.HeaderAdapter;
import ml.karmaconfigs.api.common.utils.url.request.Post;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.routing.RoutingSupport;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Karma HTTP Utilities using org.apache.http implementation
 */
@SuppressWarnings("unused")
public final class HttpUtil {

    private final URI url;

    private final CloseableHttpClient httpClient;

    /**
     * Initialize the HTTP utilities
     *
     * @param target the target URL
     * @throws URISyntaxException if the URI of the URL is not valid
     */
    HttpUtil(final URL target) throws URISyntaxException {
        url = target.toURI();

        httpClient = HttpClientBuilder.create()
                .disableDefaultUserAgent()
                .disableAuthCaching()
                .disableCookieManagement()
                .disableConnectionState()
                .setUserAgent("KarmaAPI/" + KarmaAPI.getVersion() + " (" + JavaVM.getSystem().getName() + " " + JavaVM.osVersion() + "; " + JavaVM.osModel() + "; " + JavaVM.osArchitecture() + ") JavaWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                .build();
    }

    /**
     * Get the response code from the URL
     *
     * @param headers the request headers
     * @return the URL response code
     */
    public int getCode(final HeaderAdapter... headers) {
        try {
            HttpHead httpHead = new HttpHead(url);
            for (HeaderAdapter h : headers) httpHead.addHeader(new BasicHeader(h.getKey(), h.getValue()));
            HttpHost host = RoutingSupport.determineHost(httpHead);

            try (ClassicHttpResponse httpResponse = httpClient.executeOpen(host, httpHead, null)) {
                return httpResponse.getCode();
            }
        } catch (Throwable ignored) {
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {}
        }

        return HttpURLConnection.HTTP_MULT_CHOICE;
    }

    /**
     * Connect and instantly disconnect from a URL.
     * This method may be called only when actually
     * needed
     */
    public void push() {
        try {
            HttpGet httpget = new HttpGet(url);
            httpClient.execute(httpget, (rsp) -> rsp);
        } catch (Throwable ignored) {
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Connect and instantly disconnect from a URL.
     * This method may be called only when actually needed
     *
     * @param data    the post data
     * @param headers the request headers
     */
    public void push(final Post data, final HeaderAdapter... headers) {
        try {
            HttpPost postRequest = new HttpPost(url);
            for (HeaderAdapter header : headers) postRequest.addHeader(new BasicHeader(header.getKey(), header.getValue()));

            if (StringUtils.isNullOrEmpty(data.getJson())) {
                List<NameValuePair> params = new ArrayList<>();
                data.getData().forEach((key) -> params.add(new BasicNameValuePair(key, data.get(key))));

                postRequest.setEntity(new UrlEncodedFormEntity(params));
            } else {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                JsonObject json = gson.fromJson(data.getJson(), JsonObject.class);

                data.getData().forEach((k) -> {
                    json.addProperty(k, data.get(k));
                });

                String string = gson.toJson(json);
                postRequest.setEntity(new StringEntity(string));
            }

            httpClient.execute(postRequest, (rsp) -> rsp);
        } catch (Throwable ignored) {
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Connect and instantly disconnect from a URL.
     * This method may be called only when actually needed
     *
     * @param headers the request headers
     */
    public void push(final HeaderAdapter... headers) {
        try {
            HttpPost postRequest = new HttpPost(url);
            for (HeaderAdapter h : headers) postRequest.addHeader(new BasicHeader(h.getKey(), h.getValue()));

            httpClient.execute(postRequest, (rsp) -> rsp);
        } catch (Throwable ignored) {
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Get the response
     *
     * @param data    the post data
     * @param headers the request headers
     * @return the url response
     */
    @NotNull
    public String getResponse(final Post data, final HeaderAdapter... headers) {
        String response = "";

        try {
            HttpPost postRequest = new HttpPost(url);
            for (HeaderAdapter h : headers) postRequest.addHeader(h.getKey(), h.getValue());

            if (StringUtils.isNullOrEmpty(data.getJson())) {
                List<NameValuePair> params = new ArrayList<>();
                data.getData().forEach((key) -> params.add(new BasicNameValuePair(key, data.get(key))));

                postRequest.setEntity(new UrlEncodedFormEntity(params));
            } else {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                JsonObject json = gson.fromJson(data.getJson(), JsonObject.class);

                data.getData().forEach((k) -> {
                    json.addProperty(k, data.get(k));
                });

                String string = gson.toJson(json);

                postRequest.setEntity(new StringEntity(string));
            }

            HttpHost host = RoutingSupport.determineHost(postRequest);
            try (ClassicHttpResponse httpResponse = httpClient.executeOpen(host, postRequest, null)) {
                Header[] contentType = httpResponse.getHeaders("Content-type");

                boolean json = false;
                for (Header header : contentType) {
                    if (header.getValue().contains("application/json")) {
                        json = true;
                        break;
                    }
                }
                Scanner sc = new Scanner(httpResponse.getEntity().getContent());

                StringBuilder sb = new StringBuilder();
                while (sc.hasNext()) {
                    sb.append(sc.next()).append(" ");
                }

                response = sb.toString();
                if (json) {
                    try {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonElement object = gson.fromJson(response, JsonElement.class);

                        //Set json to pretty print
                        response = gson.toJson(object);
                    } catch (Throwable ignored) {
                    }
                }
            }
        } catch (HttpHostConnectException ex) {
            response = "403 - Connection refused";
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {
            }
        }

        return response;
    }

    /**
     * Get the response
     *
     * @param headers the request headers
     * @return the url response
     */
    @NotNull
    public String getResponse(final HeaderAdapter... headers) {
        String response = "";

        try {
            HttpGet httpget = new HttpGet(url);

            for (HeaderAdapter h : headers) httpget.addHeader(h.getKey(), h.getValue());

            HttpHost host = RoutingSupport.determineHost(httpget);
            try (ClassicHttpResponse httpResponse = httpClient.executeOpen(host, httpget, null)) {
                Header[] contentType = httpResponse.getHeaders("Content-type");

                boolean json = false;
                for (Header header : contentType) {
                    if (header.getValue().contains("application/json")) {
                        json = true;
                        break;
                    }
                }
                Scanner sc = new Scanner(httpResponse.getEntity().getContent());

                StringBuilder sb = new StringBuilder();
                while (sc.hasNext()) {
                    sb.append(sc.next()).append(" ");
                }

                response = sb.toString();

                if (json) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonElement object = gson.fromJson(response, JsonElement.class);

                    //Set json to pretty print
                    response = gson.toJson(object);
                }
            }
        } catch (HttpHostConnectException ex) {
            response = "403 - Connection refused";
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (HttpException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                httpClient.close();
            } catch (Throwable ignored) {
            }
        }

        return response;
    }
}
