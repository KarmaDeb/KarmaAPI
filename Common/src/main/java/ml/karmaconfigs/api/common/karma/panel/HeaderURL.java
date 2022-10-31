package ml.karmaconfigs.api.common.karma.panel;

import org.apache.http.Header;

import java.net.URL;

public final class HeaderURL {

    private final URL url;
    private final Header header;

    public HeaderURL(final URL u, final Header h) {
        url = u;
        header = h;
    }

    public URL getUrl() {
        return url;
    }

    public Header getHeader() {
        return header;
    }
}
