package sh.xra;

import android.util.Log;
import sh.xra.networking.UDP;
import java.nio.ByteBuffer;

public final class Handler
{
    public static final byte LEFT_BUTTON = 1;
    public static final byte RIGHT_BUTTON = 3;

    public void clickLeftButton()
    {
        byte[] data = { 0x10, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void pressLeftButton()
    {
        byte[] data = { 0x11, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void releaseLeftButton()
    {
        byte[] data = { 0x12, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void clickRightButton()
    {
        byte[] data = { 0x20, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void pressRightButton()
    {
        byte[] data = { 0x21, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void releaseRightButton()
    {
        byte[] data = { 0x22, (byte)0, (byte)0, (byte)0, (byte)0 };
        UDP.send(data);
    }

    public void click(byte button)
    {
        switch (button) {
            case Handler.LEFT_BUTTON:
                this.clickLeftButton();
                break;
            case Handler.RIGHT_BUTTON:
                this.clickRightButton();
                break;
        }
    }

    public void press(byte button)
    {
        switch (button) {
            case Handler.LEFT_BUTTON:
                this.pressLeftButton();
                break;
            case Handler.RIGHT_BUTTON:
                this.pressRightButton();
                break;
        }
    }

    public void release(byte button)
    {
        switch (button) {
            case Handler.LEFT_BUTTON:
                this.releaseLeftButton();
                break;
            case Handler.RIGHT_BUTTON:
                this.releaseRightButton();
                break;
        }
    }

    public void move(int x, int y)
    {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        int multiplier  = Options.getMultiplier();

        buffer.put((byte)0x40);
        buffer.putShort((short) (x * multiplier));
        buffer.putShort((short) (y * multiplier));
        byte[] data = buffer.array();
        UDP.send(data);
    }

    public void wheelUp(int y)
    {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        if (Options.getNaturalScrolling()) {
            buffer.put((byte)0x31);
        } else {
            buffer.put((byte)0x30);
        }

        buffer.putShort((short)y);
        byte[] data = buffer.array();
        UDP.send(data);
    }

    public void wheelDown(int y)
    {
        ByteBuffer buffer = ByteBuffer.allocate(5);

        if (Options.getNaturalScrolling()) {
            buffer.put((byte)0x30);
        } else {
            buffer.put((byte)0x31);
        }

        buffer.putShort((short)y);
        byte[] data = buffer.array();
        UDP.send(data);
    }
}
