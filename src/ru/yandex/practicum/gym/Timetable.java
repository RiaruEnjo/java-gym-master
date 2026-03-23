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

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        return daySessions != null ? daySessions : new TreeMap<>();
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions == null) {
            return Collections.emptyList();
        }
        return daySessions.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();
        for (TreeMap<TimeOfDay, List<TrainingSession>> trainingsForDay : timetable.values()) {
            for (List<TrainingSession> sessions : trainingsForDay.values()) {
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

        counts.sort(null);
        return counts;
    }
}
