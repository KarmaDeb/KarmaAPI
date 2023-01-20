package ml.karmaconfigs.api.common.version.comparator;

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

import ml.karmaconfigs.api.common.karma.source.KarmaSource;

/**
 * Karma version comparator
 */
public class VersionComparator {

    /**
     * The comparator options
     */
    private final ComparatorBuilder options;

    /**
     * Initialize the version comparator
     */
    public VersionComparator() {
        options = new ComparatorBuilder();
    }

    /**
     * Initialize the version comparator
     *
     * @param opts the comparator options
     */
    public VersionComparator(final ComparatorBuilder opts) {
        options = opts;
    }

    /**
     * Initialize the version comparator
     *
     * @param current the version to compare with
     * @param check the version to compare with
     */
    public VersionComparator(final String current, final String check) {
        options = new ComparatorBuilder()
                .currentVersion(current).checkVersion(check);
    }

    /**
     * Initialize the version comparator
     *
     * @param source the file version to use
     * @param check the version to compare with
     */
    public VersionComparator(final KarmaSource source, final String check) {
        options = new ComparatorBuilder()
                .currentVersion(source.version()).checkVersion(check);
    }

    /**
     * Get the version difference
     *
     * @return the version difference
     */
    public Difference getDifference() {
        String[] currentParts = options.getCurrentVersion().split("\\.");
        String[] checkParts = options.getCheckVersion().split("\\.");
        int length = Math.max(currentParts.length, checkParts.length);
        for (int i = 0; i < length; i++) {
            try {
                int currentPart = (i < currentParts.length) ? Integer.parseInt(currentParts[i]) : 0;
                int checkPart = (i < checkParts.length) ? Integer.parseInt(checkParts[i]) : 0;

                if (currentPart != checkPart) {
                    if (currentPart < checkPart) {
                        return Difference.OUTDATED;
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        return Difference.UPDATED;
    }

    /**
     * Get if the current version is up-to-date
     *
     * @return if the version is up-to-date
     */
    public boolean isUpToDate() {
        return getDifference().equals(Difference.UPDATED);
    }

    /**
     * Create a new version comparator options
     * builder
     *
     * @return a new version comparator options
     * builder
     */
    public static ComparatorBuilder createBuilder() {
        return new ComparatorBuilder();
    }
}
