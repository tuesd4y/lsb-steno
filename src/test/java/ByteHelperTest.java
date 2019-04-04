import encoder.ByteHelper;
import matchers.CustomMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ByteHelperTest {
    @Test
    public void testGetBitAtIndex() {
        final byte[] toTest = new byte[]{
                (byte) 0b1111_1111,
                (byte) 0b1010_1010,
                (byte) 0b0000_0000
        };

        for (int i = 0; i < 8; i++) {
            assertThat("Problem at position " + i, ByteHelper.getBitAtIndex(toTest, i), is(true));
        }
        for (int i = 8; i < 16; i++) {
            assertThat("Problem at position " + i, ByteHelper.getBitAtIndex(toTest, i), is(i % 2 == 1));
        }
        for (int i = 16; i < 24; i++) {
            assertThat("Problem at position " + i, ByteHelper.getBitAtIndex(toTest, i), is(false));
        }
    }

    @Test
    public void switchLsb() {
        final int endsWith0 = 0b1111_1100;
        final int endsWith1 = 0b1111_1001;

        assertThat("should negate last digit", ByteHelper.switchLsb(true, endsWith0), is(CustomMatchers.endingWith1));
        assertThat("should not negate last digit", ByteHelper.switchLsb(false, endsWith0), is(endsWith0));
        assertThat("should negate last digit", ByteHelper.switchLsb(true, endsWith1), is(CustomMatchers.endingWith0));
        assertThat("should not negate last digit", ByteHelper.switchLsb(false, endsWith1), is(endsWith1));
    }
}
