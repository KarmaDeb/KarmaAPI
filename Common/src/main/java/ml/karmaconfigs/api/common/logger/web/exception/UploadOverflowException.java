package ml.karmaconfigs.api.common.logger.web.exception;

import ml.karmaconfigs.api.common.karma.source.KarmaSource;

public class UploadOverflowException extends Exception {

    public UploadOverflowException(final KarmaSource source, final int wait) {
        super("Cannot upload log for source " + source.name() + " because its in cooldown. Must wait: " + wait + " seconds");
    }
}
