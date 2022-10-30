package seeta;

public class FaceLandmarker {
    static {
        System.loadLibrary("SeetaFaceLandmarker600_java");
    }

    public long impl = 0;

    private native void construct(SeetaModelSetting setting);
    public FaceLandmarker(SeetaModelSetting setting) {
        this.construct(setting);
    }

    public native void dispose();
    protected void finalize()throws Throwable{
        super.finalize();
        this.dispose();
    }

    public native int number();

    public native void mark(SeetaImageData image, SeetaRect rect, SeetaPointF[] pointFS);
    public native void mark(SeetaImageData image, SeetaRect rect, SeetaPointF[] pointFS, int[] masks);
}
