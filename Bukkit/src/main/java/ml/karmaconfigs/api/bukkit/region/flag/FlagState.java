package ml.karmaconfigs.api.bukkit.region.flag;

/**
 * Flag states
 */
public enum FlagState {
    /*
    Allowed
     */
    ALLOW,
    /*
    Denied
     */
    DENY,
    /*
    Don't perform any special action (deny if enforceability required)
     */
    DEFAULT
}
