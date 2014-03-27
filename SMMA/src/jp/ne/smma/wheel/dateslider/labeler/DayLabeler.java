package jp.ne.smma.wheel.dateslider.labeler;

import java.util.Calendar;

import jp.ne.smma.wheel.dateslider.labeler.slider.TimeObject;

/**
 * A Labeler that displays days
 */
public class DayLabeler extends Labeler {
    private final String mFormatString;

    public DayLabeler(String formatString) {
        super(150, 60);
        mFormatString = formatString;
    }

    @Override
    public TimeObject add(long time, int val) {
        return timeObjectfromCalendar(Util.addDays(time, val));
    }

    @Override
    protected TimeObject timeObjectfromCalendar(Calendar c) {
        return Util.getDay(c, mFormatString);
    }
}