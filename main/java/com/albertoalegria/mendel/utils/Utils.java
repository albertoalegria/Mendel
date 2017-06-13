package com.albertoalegria.mendel.utils;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.models.Classroom;
import com.albertoalegria.mendel.models.Time;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Alberto Alegria
 */
public class Utils {
    public static class TimesUtils {
        public static List<Time> getTimesArray(int first, int last) {
            return getAllTimes().stream().filter(time -> time.getHourOfDay() >= first && time.getHourOfDay() <= last).collect(Collectors.toCollection(ArrayList::new));
        }

        public static List<Time> getAllTimes() {
            ArrayList<Time> times = new ArrayList<>();
            for (int i = 1; i <= 70; i++) {
                times.add(new Time(i));
            }
            return times;
        }

        public static List<Time> getTimesForDay(Day day) {
            return getAllTimes().stream().filter(time -> time.getDayOfWeek().equals(day)).collect(Collectors.toList());
        }

        public static List<Time> getTimesRange(int first, int last) {
            ArrayList<Time> times = new ArrayList<>();

            for (int i = first; i <= last; i++) {
                times.add(new Time(i));
            }

            return times;
        }

        /*public static ArrayList<String> getDayHours(int first, int last) {
            ArrayList<String> hours = new ArrayList<>();
            for (int i = first; i < last; i++) {
                hours.add(first + ":00" + "-" + (first + 1) + ":00");
            }
            return hours;
        }*/
    }

    public static class ArrayUtils {
        public static String timesToString(List<Time> times) {
            if (times.size() > 0) {

                StringBuilder builder = new StringBuilder();

                for (Time time : times) {
                    builder.append(time.getHour()).append(",");
                }
                builder.setLength(builder.length() - 1);

                return builder.toString();
            } else {
                return "";
            }
        }

        public static String classroomsToString(List<Classroom> classrooms) {
            if (classrooms.size() > 0) {

                StringBuilder builder = new StringBuilder();
                for (Classroom classroom : classrooms) {
                    builder.append(classroom.getId()).append(",");
                }
                builder.setLength(builder.length() - 1);

                return builder.toString();
            } else {
                return "";
            }
        }

        public static List<Time> stringToTimes(String timesStr) {
            if (!timesStr.isEmpty()) {
                String[] arrTimes = timesStr.split(",");
                List<Time> times = new ArrayList<>();
                for (String t : arrTimes) {
                    times.add(new Time(Integer.parseInt(t)));
                }
                return times;
            } else {
                return new ArrayList<>();
            }
        }

    }

    public static class RandUtils {
        public static int getRandomNumber(int min, int max) {
            return new Random().nextInt((max - min) + 1) + min;
        }

        public static double getRandomDouble() {
            return new Random().nextDouble();
        }
    }

    public static void export(String data, String path) throws IOException {
        Path target = Paths.get("/Users/alberto/Desktop/mendel/" + path);
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        Files.write(target, bytes, StandardOpenOption.CREATE);
    }

}
