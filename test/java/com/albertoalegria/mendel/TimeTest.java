package com.albertoalegria.mendel;

import com.albertoalegria.mendel.enums.Day;
import com.albertoalegria.mendel.enums.Shift;
import com.albertoalegria.mendel.models.Time;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author Alberto Alegria
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeTest {
    private Time mondayTime;
    private Time tuesdayTime;
    private Time wednesdayTime;
    private Time thursdayTime;
    private Time fridayTime;

    @Before
    public void setUp() {
        //Test made with 14 hours per day, 5 days per week.
        mondayTime = new Time(14);
        tuesdayTime = new Time(15);
        wednesdayTime = new Time(29);
        thursdayTime = new Time(43);
        fridayTime = new Time(70);
    }

    @Test
    public void testTimeMatchWithAssignedDay() {
        assertEquals(Day.MONDAY, mondayTime.getDayOfWeek());
        assertEquals(Day.TUESDAY, tuesdayTime.getDayOfWeek());
        assertEquals(Day.WEDNESDAY, wednesdayTime.getDayOfWeek());
        assertEquals(Day.THURSDAY, thursdayTime.getDayOfWeek());
        assertEquals(Day.FRIDAY, fridayTime.getDayOfWeek());
    }

    @Test
    public void testTimeMatchWithAssignedHour() {
        assertEquals(14, mondayTime.getHour());
        assertEquals(15, tuesdayTime.getHour());
        assertEquals(29, wednesdayTime.getHour());
        assertEquals(43, thursdayTime.getHour());
        assertEquals(70, fridayTime.getHour());
    }

    @Test
    public void testTimeMatchWithAssignedHourOfDay() {
        assertEquals(14, mondayTime.getHourOfDay());
        assertEquals(1, tuesdayTime.getHourOfDay());
        assertEquals(1, wednesdayTime.getHourOfDay());
        assertEquals(1, thursdayTime.getHourOfDay());
        assertEquals(14, fridayTime.getHourOfDay());
    }

    @Test
    public void testTimeMatchWithAssignedHourId() {
        assertEquals(13, mondayTime.getId());
        assertEquals(14, tuesdayTime.getId());
        assertEquals(28, wednesdayTime.getId());
        assertEquals(42, thursdayTime.getId());
        assertEquals(69, fridayTime.getId());
    }

    @Test
    public void testTimeMatchWithAssignedFancyHourOfDay() {
        assertEquals(20, mondayTime.getFancyHourOfDay());
        assertEquals(7, tuesdayTime.getFancyHourOfDay());
        assertEquals(7, wednesdayTime.getFancyHourOfDay());
        assertEquals(7, thursdayTime.getFancyHourOfDay());
        assertEquals(20, fridayTime.getFancyHourOfDay());
    }

    @Test
    public void testTimeMatchWithAssignedShift() {
        assertEquals(Shift.AFTERNOON, mondayTime.getShift());
        assertEquals(Shift.MORNING, tuesdayTime.getShift());
        assertEquals(Shift.MORNING, wednesdayTime.getShift());
        assertEquals(Shift.MORNING, thursdayTime.getShift());
        assertEquals(Shift.AFTERNOON, fridayTime.getShift());
    }
}
