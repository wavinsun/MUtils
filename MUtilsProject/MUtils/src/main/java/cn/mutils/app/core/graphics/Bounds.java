package cn.mutils.app.core.graphics;

/**
 * Bounds for graphics algorithm logic
 */
@SuppressWarnings("unused")
public class Bounds {

    /**
     * X coordinate point
     */
    public double x;

    /**
     * Y coordinate point
     */
    public double y;

    /**
     * Size of x coordinate for target
     */
    public double width;

    /**
     * Size of y coordinate for target
     */
    public double height;

    public Bounds() {

    }

    public Bounds(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double width() {
        return this.width;
    }

    public double height() {
        return this.height;
    }

    public int intX() {
        return (int) this.x;
    }

    public int intY() {
        return (int) this.y;
    }

    public int intWidth() {
        return (int) this.width;
    }

    public int intHeight() {
        return (int) this.height;
    }

    public float floatX() {
        return (float) this.x;
    }

    public float floatY() {
        return (float) this.y;
    }

    public float floatWidth() {
        return (float) this.width;
    }

    public float floatHeight() {
        return (float) this.height;
    }

    /**
     * Get center crop bounds for moving source graphics object into target
     * graphics object
     *
     * @param targetWidth  target graphics object width
     * @param targetHeight target graphics object height
     * @param srcWidth     source graphics object width
     * @param srcHeight    source graphics object height
     * @return Bounds
     */
    public static Bounds getCenterCropBounds(double targetWidth, double targetHeight, double srcWidth,
                                             double srcHeight) {
        Bounds b = getFillBounds(srcWidth, srcHeight, targetWidth, targetHeight);
        double ratio = targetWidth / b.width;
        b.width = srcWidth * ratio;
        b.height = srcHeight * ratio;
        b.x = -b.x * ratio;
        b.y = -b.y * ratio;
        return b;
    }

    /**
     * Get fit bounds for moving source graphics object into target graphics
     * object
     *
     * @param targetWidth  target graphics object width
     * @param targetHeight target graphics object height
     * @param srcWidth     source graphics object width
     * @param srcHeight    source graphics object height
     * @return Bounds
     */
    public static Bounds getFitBounds(double targetWidth, double targetHeight, double srcWidth, double srcHeight) {
        if (srcWidth < targetWidth && srcHeight < targetHeight) {
            Bounds b = new Bounds();
            b.width = srcWidth;
            b.height = srcHeight;
            b.x = (targetWidth - b.width) * 0.5;
            b.y = (targetHeight - b.height) * 0.5;
            return b;
        } else {
            return getFillBounds(targetWidth, targetHeight, srcWidth, srcHeight);
        }
    }

    /**
     * Get fill bounds for moving source graphics object into target graphics
     * object
     *
     * @param targetWidth  target graphics object width
     * @param targetHeight target graphics object height
     * @param srcWidth     source graphics object width
     * @param srcHeight    source graphics object height
     * @return Bounds
     */
    public static Bounds getFillBounds(double targetWidth, double targetHeight, double srcWidth, double srcHeight) {
        Bounds b = new Bounds();
        if (srcWidth / srcHeight > targetWidth / targetHeight) {
            b.width = targetWidth;
            b.height = targetWidth * srcHeight / srcWidth;
            b.x = 0;
            b.y = (targetHeight - b.height) * 0.5;
        } else {
            b.height = targetHeight;
            b.width = targetHeight * srcWidth / srcHeight;
            b.x = (targetWidth - b.width) * 0.5;
            b.y = 0;
        }
        return b;
    }

}
