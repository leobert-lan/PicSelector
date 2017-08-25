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
