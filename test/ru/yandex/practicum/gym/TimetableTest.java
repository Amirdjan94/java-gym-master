package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    private Timetable timetable;

    private final Coach COACH_FOR_ADULT = new Coach("Васильев", "Николай", "Сергеевич");
    private final Coach COACH_FOR_CHILD = new Coach("Тихоходько", "Анастасия", "Владимировна");

    private final Group GROUP_ADULT = new Group("Акробатика для взрослых", Age.ADULT, 90);
    private final Group GROUP_CHILD = new Group("Акробатика для детей", Age.CHILD, 60);

    @BeforeEach
    public void beforeEach() {
        timetable = new Timetable();
    }

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {

        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        TrainingSession singleTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.MONDAY, timeOfDay);

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(),
                "За понедельник должно вернутся одно занятие");
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).get(timeOfDay).size(),
                "За понедельник должно вернутся одно занятие");
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY),
                "За вторник должны отсутсвовать занятия");
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {

        TrainingSession thursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_FOR_ADULT,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        TrainingSession mondayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);
        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(),
                "За понедельник должно вернутся одно занятие");
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size(),
                "За понедельник должно вернутся одно занятие");

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00

        Assertions.assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size(),
                "За четверг должны вернутся два занятия");
        Assertions.assertEquals(new TimeOfDay(13, 0), timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).navigableKeySet().getFirst(),
                "Первым должно быть занятие в 13:00");
        Assertions.assertEquals(new TimeOfDay(20, 0), timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).navigableKeySet().getLast(),
                "Вторым должно быть занятие в 20:00");

        // Проверить, что за вторник не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY),
                "За вторник должны отсутсвовать занятия");

    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {

        TrainingSession singleTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size(),
                "За понедельник в 13:00 должно вернутся одно занятие");
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)),
                "За понедельник в 14:00 не должно быть занятий");
    }

    @Test
    public void testGetTrainingSessionsForDayAndTimeMultipleSessions() {

        TrainingSession firstThursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_FOR_ADULT,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession secondThursdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(firstThursdayAdultTrainingSession);
        timetable.addNewTrainingSession(secondThursdayChildTrainingSession);

        // Проверить, что за четверг в 13:00 вернулись два занятия
        Assertions.assertEquals(2, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.THURSDAY, new TimeOfDay(13, 0)).size(),
                "За четверг в 13:00  должны вернутся два занятия");
    }

    @Test
    public void testGetTrainingSessionsForDayFromEmptyTimetable() {
       Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY),
                "При запросе списка тренировок за день из пустого списка, должно вернутся null");
    }

    @Test
    public void testGetTrainingSessionsForDayAndTimeFromEmptyTimetable() {
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.THURSDAY, new TimeOfDay(13, 0)),
                "При запросе списка тренировок за определенный день и время из пустого списка, должно вернутся null");
    }

    @Test
    public void testGetCoachCountListForMultipleCoachAndSession() {
        TrainingSession firstThursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_FOR_ADULT,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession secondThursdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession thirdWednesdayChildTrainingSession = new TrainingSession(GROUP_CHILD, COACH_FOR_CHILD,
                DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0));

        timetable.addNewTrainingSession(firstThursdayAdultTrainingSession);
        timetable.addNewTrainingSession(secondThursdayChildTrainingSession);
        timetable.addNewTrainingSession(thirdWednesdayChildTrainingSession);
        List<CounterOfTrainings> counterOfTrainingsList = timetable.getCountByCoaches();

        Assertions.assertEquals(2, counterOfTrainingsList.size(),
                "В списке должно вернуться два тренера");
        Assertions.assertEquals(COACH_FOR_CHILD, counterOfTrainingsList.get(0).getCoach(),
                "В списке первым должен быть тренер Тихоходько Анастасия Владимировна");
        Assertions.assertEquals(2, counterOfTrainingsList.get(0).getCount(),
                "У тренера Тихоходько Анастасия Владимировна должно быть две тренировки");
        Assertions.assertEquals(COACH_FOR_ADULT, counterOfTrainingsList.get(1).getCoach(),
                "В списке первым должен быть тренер Васильев Николай Сергеевич");
        Assertions.assertEquals(1, counterOfTrainingsList.get(1).getCount(),
                "У тренера Васильев Николай Сергеевич должно быть одна тренировка");

    }

    @Test
    public void testGetCoachCountListForEmptyTimetable() {
        List<CounterOfTrainings> counterOfTrainingsList = timetable.getCountByCoaches();
        Assertions.assertNull(counterOfTrainingsList,"Список должен быть пустым");
    }

    @Test
    public void testGetCoachCountListForSingleCoachAndSession() {
        TrainingSession firstThursdayAdultTrainingSession = new TrainingSession(GROUP_ADULT, COACH_FOR_ADULT,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(firstThursdayAdultTrainingSession);
        List<CounterOfTrainings> counterOfTrainingsList = timetable.getCountByCoaches();

        Assertions.assertEquals(1, counterOfTrainingsList.size(),
                "В списке должен вернуться один тренер");
        Assertions.assertEquals(COACH_FOR_ADULT, counterOfTrainingsList.get(0).getCoach(),
                "В списке должен быть тренер Васильев Николай Сергеевич");
        Assertions.assertEquals(1, counterOfTrainingsList.get(0).getCount(),
                "У тренера Васильев Николай Сергеевич должна быть одна тренировка");

    }

}
