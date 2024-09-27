package bg.sofia.uni.fmi.mjt.gym.workout;

import bg.sofia.uni.fmi.mjt.gym.member.Member;

import java.util.Objects;

public record Exercise(String name, int sets, int repetitions) {

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Exercise exercise = (Exercise) obj;
        return Objects.equals(this.name, exercise.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

}
