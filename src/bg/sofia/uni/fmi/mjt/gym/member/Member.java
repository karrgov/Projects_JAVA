package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.GymAPI;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;

import java.util.*;

public class Member implements GymMember {

    private Address address;
    private String name;
    private int age;
    private String personalIdNumber;
    private Gender gender;
    private Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        this.trainingProgram = new HashMap<DayOfWeek, Workout>();
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public String getPersonalIdNumber() {
        return this.personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Collections.unmodifiableMap(this.trainingProgram);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if(day ==  null || workout == null) {
            throw new IllegalArgumentException("Day and/or workout can not be null!");
        }

        this.trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        Collection<DayOfWeek> result = new ArrayList<>();

        for(Map.Entry<DayOfWeek, Workout> curr : this.trainingProgram.entrySet()) {
            if(curr.getValue().exercises().getLast().name().equals(exerciseName)) {
                result.add(curr.getKey());
            }
        }
        return result;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if(this.trainingProgram.get(day) == null) {
            throw new DayOffException("The day is a rest day!");
        }

        if(day == null || exercise == null) {
            throw new IllegalArgumentException("Day or exercise can not be null!");
        }

        this.trainingProgram.get(day).exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if(this.trainingProgram.get(day) == null) {
            throw new DayOffException("The day is a rest day!");
        }

        if(day == null || exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Day or exercise can not be null!");
        }

        this.trainingProgram.get(day).exercises().addAll(exercises);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Member member = (Member) obj;
        return Objects.equals(this.personalIdNumber, member.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personalIdNumber);
    }

    @Override
    public String toString() {
        return "Member{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", personalIdNumber='" + personalIdNumber + '\'' +
                ", gender=" + gender +
                '}';
    }
}
