package pathanimations;


import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class FXAnimationFactory {
    private FXAnimationFactory() {
    }

    public static ScaleTransition makeRectangleAnimation() {
        ScaleTransition st = new ScaleTransition();
        st.setDuration(Duration.seconds(0.1));
        st.setByX(0.15);
        st.setByY(0.15);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        return st;
    }
}
