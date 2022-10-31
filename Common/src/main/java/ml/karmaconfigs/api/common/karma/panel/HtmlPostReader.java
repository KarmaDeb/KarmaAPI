package ml.karmaconfigs.api.common.karma.panel;

import ml.karmaconfigs.api.common.karma.KarmaAPI;
import ml.karmaconfigs.api.common.utils.enums.Level;

import javax.swing.text.html.HTMLEditorKit;

public class HtmlPostReader extends HTMLEditorKit.ParserCallback {

    /**
     * @param errorMsg
     * @param pos
     */
    @Override
    public void handleError(final String errorMsg, final int pos) {
        KarmaAPI.source(false).console().send("{0} at line {1}", Level.GRAVE, errorMsg, pos);
        KarmaAPI.source(false).logger().scheduleLog(Level.GRAVE, "{0} at line {1}", errorMsg, pos);
    }
}
