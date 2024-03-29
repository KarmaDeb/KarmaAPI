package ml.karmaconfigs.api.common.utils.enums;

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

/**
 * Karma known levels
 */
public enum Level {
    /**
     * Nothing wrong here
     */
    OK,
    /**
     * Information you should know
     */
    INFO,
    /**
     * Everything is ok, but could be better
     */
    WARNING,
    /**
     * Something just went wrong
     */
    GRAVE,
    /**
     * Debug level
     */
    DEBUG;

    /**
     * Get level byte value
     *
     * @return the level byte value
     */
    public byte getByte() {
        switch (this) {
            case OK:
                return 0x01;
            case INFO:
                return 0x02;
            case WARNING:
                return 0x03;
            case GRAVE:
                return 0x04;
            case DEBUG:
                return 0x05;
            default:
                return 0x00;
        }
    }

    /**
     * Get Markdown format for the level
     *
     * @return the level Markdown format
     */
    public String getMarkdown() {
        switch (this) {
            case OK:
                return "<span style=\"color: lime\">OK</span>";
            case INFO:
                return "<span style=\"color: gray\">INFO</span>";
            case WARNING:
                return "<span style=\"color: gold\">WARNING</span>";
            case GRAVE:
                return "<span style=\"color: indianred\">GRAVE</span>";
            case DEBUG:
            default:
                return "<span style=\"color: cyan\">DEBUG</span>";
        }
    }
}
