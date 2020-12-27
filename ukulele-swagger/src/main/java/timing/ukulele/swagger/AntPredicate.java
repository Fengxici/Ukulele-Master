package timing.ukulele.swagger;

import com.google.common.base.Predicate;
import org.springframework.util.AntPathMatcher;

public class AntPredicate {
    public static Predicate<String> ant(final String antPattern) {
        return input -> {
            AntPathMatcher matcher = new AntPathMatcher();
            return matcher.match(antPattern, input);
        };
    }
}
