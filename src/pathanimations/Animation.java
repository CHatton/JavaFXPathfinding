package pathanimations;

import java.util.LinkedList;
import java.util.Queue;

public class Animation {
    private Queue<Frame> animationFrames;

    public Animation() {
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
