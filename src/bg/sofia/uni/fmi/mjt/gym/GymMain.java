package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.Gender;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.Member;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;

public class GymMain {
     public static void main(String[] args) {
         Address address1 = new Address(123.5, 243.65);
         Gym gym = new Gym(15, address1);

         GymMember mem1 = new Member(address1, "Gosho", 23, "2345", Gender.MALE);
         GymMember mem2 = new Member(address1, "Sasho", 26, "4932", Gender.MALE);
         GymMember mem3 = new Member(address1, "Krasimir", 43, "9384", Gender.MALE);
         GymMember mem4 = new Member(address1, "Petur", 45, "9483", Gender.MALE);
         GymMember mem5 = new Member(address1, "Dimitur", 19, "5436", Gender.MALE);
         GymMember mem6 = new Member(address1, "Vasilka", 34, "0294", Gender.FEMALE);

         Exercise pullUp = new Exercise("pullUp", 3, 5);
         Exercise pullOver = new Exercise("pullOver", 3, 5);
         Exercise pushDown = new Exercise("pushDown", 3, 5);
         Exercise sidePull = new Exercise("sidePull", 3, 5);
         Exercise frontPull = new Exercise("frontPull", 3, 5);
         Exercise bench = new Exercise("bench", 3, 5);


         SequencedCollection<Exercise> workout1 = new LinkedList<>();
         workout1.add(pullUp);
         workout1.add(pullOver);
         workout1.add(sidePull);
         workout1.add(frontPull);
         Workout workoutFinal1 = new Workout(workout1);

         SequencedCollection<Exercise> workout2 = new LinkedList<>();
         workout2.add(pullUp);
         workout2.add(pullOver);
         workout2.add(sidePull);
         workout2.add(pushDown);
         Workout workoutFinal2 = new Workout(workout2);

         SequencedCollection<Exercise> workout3 = new LinkedList<>();
         workout3.add(bench);
         workout3.add(pullOver);
         workout3.add(sidePull);
         workout3.add(pushDown);
         Workout workoutFinal3 = new Workout(workout3);


         mem1.setWorkout(DayOfWeek.MONDAY, workoutFinal1);
         mem1.setWorkout(DayOfWeek.FRIDAY, workoutFinal2);
         mem1.setWorkout(DayOfWeek.WEDNESDAY, workoutFinal3);

         mem2.setWorkout(DayOfWeek.MONDAY, workoutFinal2);
         mem2.setWorkout(DayOfWeek.TUESDAY, workoutFinal2);
         mem2.setWorkout(DayOfWeek.WEDNESDAY, workoutFinal3);

         mem3.setWorkout(DayOfWeek.MONDAY, workoutFinal1);
         mem3.setWorkout(DayOfWeek.THURSDAY, workoutFinal1);
         mem3.setWorkout(DayOfWeek.WEDNESDAY, workoutFinal3);

         mem4.setWorkout(DayOfWeek.MONDAY, workoutFinal1);
         mem4.setWorkout(DayOfWeek.FRIDAY, workoutFinal2);
         mem4.setWorkout(DayOfWeek.SATURDAY, workoutFinal2);

         mem5.setWorkout(DayOfWeek.TUESDAY, workoutFinal3);
         mem5.setWorkout(DayOfWeek.FRIDAY, workoutFinal2);
         mem5.setWorkout(DayOfWeek.SUNDAY, workoutFinal2);

         mem6.setWorkout(DayOfWeek.SUNDAY, workoutFinal2);
         mem6.setWorkout(DayOfWeek.SATURDAY, workoutFinal1);
         mem6.setWorkout(DayOfWeek.WEDNESDAY, workoutFinal3);

         gym.addMember(mem1);
         gym.addMember(mem2);
         gym.addMember(mem3);
         gym.addMember(mem4);
         gym.addMember(mem5);
         gym.addMember(mem6);

         Map<DayOfWeek, List<String>> dailyList = gym.getDailyListOfMembersForExercise("pullUp");
         gym.printDailyListOfMembers(dailyList);
     }
}
