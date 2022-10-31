package ml.karmaconfigs.api.common.boss;

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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This exception is thrown when a boss bar is tried to fetch
 * with an invalid ID
 */
public final class BossNotFoundException extends Exception {

    /**
     * The available boss bar ids
     */
    private final Set<Integer> barIds = new LinkedHashSet<>();

    /**
     * Initialize the exception
     *
     * @param bossId the boss id
     * @param ids the valid boss bar ids
     */
    public BossNotFoundException(final int bossId, final Set<Integer> ids) {
        super("BossMessage with id " + bossId + " not found");

        barIds.addAll(ids);
    }

    /**
     * Get the valid boss bar ids
     *
     * @return the valid boss bar ids
     */
    public Integer[] validIds() {
        return barIds.toArray(new Integer[0]);
    }
}
