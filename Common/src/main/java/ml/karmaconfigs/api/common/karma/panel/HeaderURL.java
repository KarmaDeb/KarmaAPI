package ml.karmaconfigs.api.common.karma.panel;

import ml.karmaconfigs.api.common.utils.url.request.HeaderAdapter;

import java.net.URL;

public final class HeaderURL {

    private final URL url;
    private final HeaderAdapter header;

    public HeaderURL(final URL u, final HeaderAdapter h) {
        url = u;
        header = h;
    }

    public URL getUrl() {
        return url;
    }

    public HeaderAdapter getHeader() {
        return header;
    }
}
