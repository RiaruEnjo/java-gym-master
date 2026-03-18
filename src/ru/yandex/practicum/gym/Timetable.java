package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        timetable.computeIfAbsent(day, k -> new TreeMap<>())
                .computeIfAbsent(time, k -> new ArrayList<>())
                .add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions == null) {
            return new ArrayList<>();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : daySessions.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions == null) {
            return new ArrayList<>();
        }
        List<TrainingSession> sessions = daySessions.get(timeOfDay);
        return sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();
        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> dayEntry : timetable.entrySet()) {
            for (List<TrainingSession> sessions : dayEntry.getValue().values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCounts.put(coach, coachCounts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CoachTrainingCount> counts = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            counts.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }

        counts.sort((a, b) -> Integer.compare(b.count, a.count));
        return counts;
    }

    public static class CoachTrainingCount {
        private final Coach coach;
        private final int count;

        public CoachTrainingCount(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }
    }
}
