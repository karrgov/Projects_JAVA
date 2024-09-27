package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.MemberCompareByName;
import bg.sofia.uni.fmi.mjt.gym.member.MemberCompareByPersonalId;
import bg.sofia.uni.fmi.mjt.gym.member.MemberCompareByProximityToGym;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {

    private int capacity;
    private Address address;
    private SortedSet<GymMember> members;

    Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        this.members = new TreeSet<GymMember>(new MemberCompareByPersonalId());
    }

    @Override
    public SortedSet<GymMember> getMembers() {
       return Collections.unmodifiableSortedSet(this.members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        return getUnmodifiableSortedSet(new MemberCompareByName());
    }

    private SortedSet<GymMember> getUnmodifiableSortedSet(Comparator<GymMember> comparator) {
        SortedSet<GymMember> sorted = new TreeSet<>(comparator);
        sorted.addAll(this.members);
        return Collections.unmodifiableSortedSet(sorted);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        return getUnmodifiableSortedSet(new MemberCompareByProximityToGym(this.address));
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if(member == null) {
            throw new IllegalArgumentException("The member can not be null!");
        }

        if(this.members.size() == this.capacity) {
            throw new GymCapacityExceededException("Can not add new member because gym's capacity is reached!");
        }

        this.members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if(members == null || members.isEmpty()) {
            throw new IllegalArgumentException("The list of members can not be null or empty!");
        }

        if(members.size() + this.members.size() > this.capacity) {
            throw new GymCapacityExceededException("Can not add new member because gym's capacity is reached!");
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if(member == null) {
            throw new IllegalArgumentException("The member can not be null!");
        }

        for(GymMember curr : this.members) {
            if(curr.equals(member))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if(day == null || exerciseName == null ||  exerciseName.isBlank()) {
            throw new IllegalArgumentException("Parameters can not be empty or null!");
        }

        for(GymMember curr : this.members) {
            for(Map.Entry<DayOfWeek, Workout> currPair : curr.getTrainingProgram().entrySet()) {
                if(currPair.getKey() == day && currPair.getValue().containsExercise(exerciseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if(exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException("Exercise can not be null or empty!");
        }

        Map<DayOfWeek, List<String>> result = new LinkedHashMap<>();

        for(DayOfWeek curr : DayOfWeek.values())
        {
            List<String> names = new ArrayList<>();

            if(isExerciseTrainedOnDay(exerciseName, curr)) {
                for(GymMember member : this.members) {
                    for(Map.Entry<DayOfWeek, Workout> currPair : member.getTrainingProgram().entrySet()) {
                        if(currPair.getKey() == curr && currPair.getValue().containsExercise(exerciseName)) {
                            names.add(member.getName());
                        }
                    }
                }
            }

            result.put(curr, names);
        }
        return result;
    }

//    @Override
//    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
//        if (exerciseName == null || exerciseName.isEmpty()) {
//            throw new IllegalArgumentException("ExerciseName should not be null or empty");
//        }
//
//        Map<DayOfWeek, List<String>> result = new HashMap<>();
//        for (GymMember member : this.members) {
//            Map<DayOfWeek, Workout> memberProgram = member.getTrainingProgram();
//            for (Map.Entry<DayOfWeek, Workout> entry : memberProgram.entrySet()) {
//                if (entry.getValue().containsExercise(exerciseName)) {
//                    List<String> membersForExercise = result.get(entry.getKey());
//                    if (membersForExercise == null) {
//                        membersForExercise = new ArrayList<>();
//                        result.put(entry.getKey(), membersForExercise);
//                    }
//
//                    membersForExercise.add(member.getName());
//                }
//            }
//        }
//
//        return Collections.unmodifiableMap(result);
//    }

    public void printDailyListOfMembers(Map<DayOfWeek, List<String>> dailyList) {
        if (dailyList == null || dailyList.isEmpty()) {
            throw new IllegalArgumentException("Can not be null or empty!");
        }

        for (Map.Entry<DayOfWeek, List<String>> entry : dailyList.entrySet()) {
            DayOfWeek day = entry.getKey();
            List<String> members = entry.getValue();

            System.out.print(day + ": ");

            if (members == null || members.isEmpty()) {
                System.out.println("No members.");
            } else {
                System.out.println(String.join(", ", members));
            }
        }
    }

}
