package pathanimations;

import java.util.LinkedList;
import java.util.Queue;

public class GraphAnimation {
    private Queue<Frame> animationFrames;

    public GraphAnimation() {
        this.animationFrames = new LinkedList<>();
    }

    public void addFrame(Frame frame) {
        animationFrames.offer(frame);
    }

    public boolean hasNext() {
        return !animationFrames.isEmpty();
    }

    public Frame next() {
        return animationFrames.poll();
    }
}
