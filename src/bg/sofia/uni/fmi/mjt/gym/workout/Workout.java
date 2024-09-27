package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.SequencedCollection;

public record Workout(SequencedCollection<Exercise> exercises) {

    public boolean containsExercise(String exerciseName) {
        for(Exercise curr : this.exercises) {
            if(curr.name().equals(exerciseName))
            {
                return true;
            }
        }
        return false;
    }

}
