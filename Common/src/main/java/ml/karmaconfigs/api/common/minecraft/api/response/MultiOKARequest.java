package ml.karmaconfigs.api.common.minecraft.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Multiple oka request
 */
@Builder
public class MultiOKARequest {

    @Getter
    long stored;
    @Getter
    long page;
    @Getter
    long pages;
    @Getter
    int fetched;

    @NonNull
    OKARequest[] accounts;

    /**
     * Get a request by name
     *
     * @param name the name
     * @return the request
     */
    public Optional<OKARequest> find(final String name) {
        return Arrays.stream(accounts).filter((account) -> name.equalsIgnoreCase(account.nick)).findFirst();
    }

    /**
     * Get a request by uuid
     *
     * @param id the uuid
     * @return the request
     */
    public Optional<OKARequest> find(final UUID id) {
        return Arrays.stream(accounts).filter((account) ->
                id.equals(account.online) || id.equals(account.offline)).findFirst();
    }

    /**
     * Get all the fetched accounts
     *
     * @return the accounts
     */
    public Collection<OKARequest> getAll() {
        return Arrays.asList(accounts);
    }

    /**
     * Create an empty OKA request
     *
     * @return the request
     */
    public static MultiOKARequest empty() {
        return MultiOKARequest.builder().stored(0).page(0).pages(0).fetched(0).accounts(new OKARequest[0]).build();
    }
}
