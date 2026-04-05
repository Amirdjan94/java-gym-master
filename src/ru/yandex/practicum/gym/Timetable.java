package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable =
            new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        //сохраняем занятие в расписании
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(dayOfWeek)) {
            this.addTrainingSessionForNewDay(trainingSession);
        } else if (timetable.get(dayOfWeek).containsKey(timeOfDay)) {
            this.addTrainingSessionForCurrentDayAndTime(trainingSession);
        } else {
            this.addTrainingSessionForCurrentDay(trainingSession);
        }
    }

    private void addTrainingSessionForCurrentDayAndTime(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        timetable.get(dayOfWeek).get(timeOfDay).add(trainingSession);
    }

    private void addTrainingSessionForCurrentDay(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        List<TrainingSession> trainingSessionList = new ArrayList<>();

        trainingSessionList.add(trainingSession);
        timetable.get(dayOfWeek).put(timeOfDay,trainingSessionList);
    }

    private void addTrainingSessionForNewDay(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> trainingsOfDayOfWeek =
                new TreeMap<>();
        List<TrainingSession> trainingSessionList = new ArrayList<>();

        trainingSessionList.add(trainingSession);
        trainingsOfDayOfWeek.put(timeOfDay, trainingSessionList);
        timetable.put(dayOfWeek, trainingsOfDayOfWeek);
    }


    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        //как реализовать, тоже непонятно, но сложность должна быть О(1)
        if (timetable.get(dayOfWeek) == null) {
            return null;
        }
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public List <CounterOfTrainings> getCountByCoaches() {
        List <CounterOfTrainings> counterOfTrainingsList = new ArrayList<>();

        if (timetable.isEmpty()){
            return null;
        } else {
            for (TreeMap<TimeOfDay, List<TrainingSession>> value : timetable.values()) {
                for (List<TrainingSession> trainingSessions : value.values()) {
                    for (TrainingSession trainingSession : trainingSessions) {
                        incrementCountSessionForCoach(trainingSession, counterOfTrainingsList);
                    }
                }
            }
        }

        Collections.sort(counterOfTrainingsList);
        return counterOfTrainingsList;
    }

    public void incrementCountSessionForCoach(TrainingSession trainingSession, List <CounterOfTrainings> counterOfTrainingsList) {
        if (counterOfTrainingsList.contains(new CounterOfTrainings(trainingSession.getCoach()))) {
            int index = counterOfTrainingsList.indexOf(new CounterOfTrainings(trainingSession.getCoach()));
            counterOfTrainingsList.get(index).incrementCount();
        } else {
            counterOfTrainingsList.add(new CounterOfTrainings(trainingSession.getCoach()));
        }
    }

}
