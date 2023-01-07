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

import ml.karmaconfigs.api.common.karma.KarmaAPI;
import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Karma URL utilities
 */
public final class URLUtils {

    static {
        KarmaAPI.install();
    }

    /**
     * Get if the URL exists
     *
     * @param url the url
     * @return if the url exists
     */
    public static boolean exists(final String url) {
        return (getResponseCode(url) == 200);
    }

    /**
     * Get the URL response code
     *
     * @param url the url
     * @return the URL response code
     */
    public static int getResponseCode(final String url) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("HEAD");
            int code = con.getResponseCode();
            con.disconnect();
            return code;
        } catch (Throwable e) {
            return 503;
        }
    }

    /**
     * Get the URL or null if it couldn't retrieve
     * specified URL
     *
     * @param target the url
     * @return the url or null
     */
    @Nullable
    public static URL getOrNull(final String target) {
        try {
            URL url = new URL(target);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int code = connection.getResponseCode();

            connection.disconnect();

            if (code == HttpURLConnection.HTTP_OK) {
                return url;
            }
        } catch (Throwable ignored) {
        }

        return null;
    }

    /**
     * Get the url or default
     *
     * @param hosts the host to get from
     * @return the url or defaults
     */
    @Nullable
    public static URL getOrBackup(final String... hosts) {
        URL url = null;
        for (String host : hosts) {
            int response = getResponseCode(host);
            if (response == 200) {
                try {
                    url = new URL(host);
                    break;
                } catch (Throwable ex) {
                    //Shouldn't print any error, as we checked if the response is a 200 OK response
                    ex.printStackTrace();
                }
            }
        }

        return url;
    }

    public static URL append(final URL url, final String target) {
        String urlString = url.toString();
        String toAdd = target;

        if (urlString.endsWith("/")) {
            if (target.startsWith("/")) {
                toAdd = target.replaceFirst("/", "");
            }
        } else {
            if (!target.startsWith("/")) {
                toAdd = "/" + target;
            }
        }

        String newUrl = urlString + toAdd;
        try {
            return new URL(newUrl);
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * Get the URL host
     *
     * @param url the url to get host from
     * @return the URL host
     */
    @Nullable
    public static String getDomainName(final String url) {
        try {
            String tmp = url;
            if (!url.startsWith("https://") && !url.startsWith("http://"))
                tmp = "https://" + url;

            URI uri = new URI(tmp);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Throwable ex) {
            return null;
        }
    }

    /**
     * Get the URL host
     *
     * @param url the url to get host from
     * @return the URL host
     */
    @Nullable
    public static String getDomainName(final URL url) {
        return getDomainName(url.toString());
    }

    /**
     * Generate a new HTTP utilities for the specified URL
     *
     * @param url the URL
     * @return the URL extra HTTP utilities
     */
    @Nullable
    public static HttpUtil extraUtils(final URL url) {
        try {
            //KarmaAPI.install();
            return new HttpUtil(url);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
