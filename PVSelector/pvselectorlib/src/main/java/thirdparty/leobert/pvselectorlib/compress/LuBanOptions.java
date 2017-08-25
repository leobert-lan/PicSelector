/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package thirdparty.leobert.pvselectorlib.compress;

import java.io.Serializable;

public class LuBanOptions implements Serializable {
    /**
     * the threshold of max byte count of the picture after fetchCompressInterface
     */
    private int maxSize;
    private int maxHeight;
    private int maxWidth;

//    private LuBanOptions() {
//    }


    public LuBanOptions() {
    }

    public LuBanOptions(int maxSize, int maxHeight, int maxWidth) {
        this.maxSize = maxSize;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

//    public static class Builder {
//        private LuBanOptions options;
//
//        public Builder() {
//            options = new LuBanOptions();
//        }
//
//        public Builder setMaxSize(int maxSize) {
//            options.setMaxSize(maxSize);
//            return this;
//        }
//
//        public Builder setMaxHeight(int maxHeight) {
//            options.setMaxHeight(maxHeight);
//            return this;
//        }
//
//        public Builder setMaxWidth(int maxWidth) {
//            options.setMaxWidth(maxWidth);
//            return this;
//        }
//
//        public LuBanOptions create() {
//            return options;
//        }
//    }
}
