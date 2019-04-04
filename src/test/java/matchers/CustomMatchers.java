package matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public final class CustomMatchers {


    public final static Matcher<Integer> endingWith0 = new BaseMatcher<Integer>() {
        @Override
        public boolean matches(Object item) {
            int i = (Integer) item;
            return (i & 0b1) == 0b0;
        }

        @Override
        public void describeTo(Description description) {
            //description.
        }
    };

    public final static Matcher<Integer> endingWith1 = new BaseMatcher<Integer>() {
        @Override
        public boolean matches(Object item) {
            int i = (Integer) item;
            return (i & 0b1) == 0b1;
        }

        @Override
        public void describeTo(Description description) {
            //description.
        }
    };
}
