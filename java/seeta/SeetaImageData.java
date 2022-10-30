package seeta;

public class SeetaImageData {
    public int width;
    public int height;
    public int channels;
    public byte[] data;

    public SeetaImageData(int width, int height, int channels) {
        this.width = width;
        this.height = height;
        this.channels = channels;
        this.data = new byte[width * height * channels];
    }

    public SeetaImageData(int width, int height) {
        this(width, height, 1);
    }
}
