package seedu.address.ui.timetabletab;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tuitionclass.ClassTiming;
import seedu.address.model.tuitionclass.TuitionClass;
import seedu.address.ui.UiPart;


/**
 * A UI for the Timetable Panel Tab.
 */
public class TimetablePanel extends UiPart<Region> {
    private static final String FXML = "timetableTab/TimetablePanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TimetablePanel.class);

    private final LocalTime defaultLatestHour = LocalTime.parse("18:00", DateTimeFormatter.ofPattern("HH:mm"));
    private final LocalTime defaultEarliestHour = LocalTime.parse("09:00", DateTimeFormatter.ofPattern("HH:mm"));


    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane timetable;

    /**
     * Creates a {@code TimetablePanel} with the given {@code ObservableList}.
     */
    public TimetablePanel(ObservableList<TuitionClass> tuitionClasses) {
        super(FXML);
        build(tuitionClasses);
        timetable.maxWidth(1539);
        if (tuitionClasses != null) {
            tuitionClasses.addListener(new ListChangeListener<TuitionClass>() {
                @Override
                public void onChanged(Change<? extends TuitionClass> change) {
                    while (change.next()) {
                        build(tuitionClasses);
                        logger.info("Change to uniqueClassList, rebuilding timetable.");
                    }
                }
            });
        }
    }

    /**
     * Builds the UI of the timetable panel.
     *
     * @param tuitionClasses List of tuitionClass to retrieve the tuition class timings from for the timetable.
     */
    public void build(ObservableList<TuitionClass> tuitionClasses) {
        clearAll();
        if (tuitionClasses == null || tuitionClasses.isEmpty()) {
            logger.info("No class in uniqueClassList.");
            Label label = new Label("You have no classes.");
            timetable.add(label, 0, 0);
        } else {
            logger.info("Building timetable from uniqueClassList.");
            buildHeader(tuitionClasses);
            buildDays();
            buildClasses(tuitionClasses);
        }
    }

    /**
     * Builds the header panel for the timetable panel ui.
     *
     * @param tuitionClasses List of tuitionClasses to retrieve the earliest tuition class start timing and
     *                       latest end timing.
     */
    public void buildHeader(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null;
        LocalTime earliestHour = getEarliestHour(tuitionClasses);
        LocalTime latestHour = getLatestHour(tuitionClasses);

        timetable.add(new TimetableHeader("Time Slots", 50).getRoot(), 0, 0, 50, 1);

        int columnIndex = 50;

        while (earliestHour.isBefore(latestHour) || earliestHour.isBefore(defaultLatestHour)) {
            if (earliestHour.equals(LocalTime.parse("23:30", DateTimeFormatter.ofPattern("HH:mm")))) {
                timetable.add(new TimetableHeader(earliestHour, earliestHour.plusMinutes(29)).getRoot(),
                        columnIndex, 0, 15, 1);
                break;
            } else {
                timetable.add(new TimetableHeader(earliestHour, earliestHour.plusMinutes(30)).getRoot(),
                        columnIndex, 0, 15, 1);
            }
            columnIndex += 30;

            earliestHour = earliestHour.plusMinutes(30);
        }

    }

    /**
     * Builds the tuition class slot UI for the timetable.
     *
     * @param tuitionClasses List of tuition classes to retrieve the tuition class timings.
     */
    public void buildClasses(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null;

        LocalTime earliestHour = getEarliestHour(tuitionClasses);

        //earliest time is after the date
        ArrayList<TuitionClass> sortedList = new ArrayList<TuitionClass>(tuitionClasses);
        sortedList.sort(Comparator.comparing(TuitionClass::getClassTiming));


        // build the timetable
        LocalTime previousTime = earliestHour;
        for (int i = 0; i < sortedList.size(); i++) {
            TuitionClass currentTuitionClass = sortedList.get(i);
            if (currentTuitionClass.isAfter(previousTime)) {
                addEmptySlot(previousTime, currentTuitionClass.getStartTime(),
                        currentTuitionClass.getDayToInt(),
                        getColumnIndex(earliestHour, previousTime));
            }

            addTuitionClassSlot(currentTuitionClass, earliestHour);

            if (i != sortedList.size() - 1
                    && currentTuitionClass.getDayToInt() < sortedList.get(i + 1).getDayToInt()) {
                previousTime = earliestHour;
            } else {
                previousTime = currentTuitionClass.getEndTime();
            }
        }
    }

    /**
     * Creates a tuition class slot to be displayed on the timetable.
     *
     * @param tuitionClass TuitionClass to add
     * @param earliestTime Earliest class start time of the timetable.
     */
    private void addTuitionClassSlot(TuitionClass tuitionClass, LocalTime earliestTime) {
        assert !tuitionClass.getStartTime().isBefore(earliestTime);

        TimetableTuitionClassSlot timetableTuitionClassSlot =
                new TimetableTuitionClassSlot(tuitionClass.getClassNameString(),
                        tuitionClass.getClassTiming());
        int duration = getTimeDifference(tuitionClass.getStartTime(), tuitionClass.getEndTime());
        int columnIndex = getColumnIndex(earliestTime, tuitionClass.getStartTime());
        timetable.add(timetableTuitionClassSlot.getRoot(), columnIndex, tuitionClass.getDayToInt(),
                duration, 1);
    }

    /**
     * Gets the column index which the tuition class timing starts from on the timetable.
     *
     * @param earliestTime Earliest class start time of the timetable.
     * @param timeToIndex Time which we want to find the column index of.
     * @return Column index of the timeToIndex.
     */
    private int getColumnIndex(LocalTime earliestTime, LocalTime timeToIndex) {
        return TimetableDay.getWidth() + getTimeDifference(earliestTime, timeToIndex);
    }

    /**
     * Creates an empty slot to be displayed on the timetable.
     *
     * @param startTime Start Time of the empty slot.
     * @param endTime End Time of the empty slot.
     * @param row Row to add the empty slot to.
     * @param column Column to add the empty slot to.
     */
    private void addEmptySlot(LocalTime startTime, LocalTime endTime, int row, int column) {
        assert startTime.isBefore(endTime);

        TimetableEmptySlot emptySlot = new TimetableEmptySlot(startTime, endTime);
        int duration = getTimeDifference(startTime, endTime);
        timetable.add(emptySlot.getRoot(), column, row, duration, 1);
    }

    /**
     * Gets the time difference from two timings.
     *
     * @param startTime Start time of the two timings.
     * @param endTime End time of the two timings.
     * @return Time difference between the start time and the end time.
     */
    public int getTimeDifference(LocalTime startTime, LocalTime endTime) {
        assert startTime.isBefore(endTime) || startTime.equals(endTime);

        return (int) startTime.until(endTime, ChronoUnit.MINUTES);
    }

    /**
     * Removes duplicate class timings from a list of class timings.
     *
     * @param classTimings List of class timings which duplicates are to be removed from.
     * @return ArrayList of classTimings which have duplicates removed.
     */
    public ArrayList<ClassTiming> removeDuplicateClassTimings(List<ClassTiming> classTimings) {
        assert classTimings != null;

        ArrayList<ClassTiming> uniqueClassTimings = new ArrayList<>();

        for (int i = 0; i < classTimings.size(); i++) {
            ClassTiming currentClassTiming = classTimings.get(i);
            if (uniqueClassTimings.contains(currentClassTiming)) {
                continue;
            }
            uniqueClassTimings.add(currentClassTiming);
        }

        return uniqueClassTimings;
    }

    /**
     * Gets the earliest hour of the earliest class timing from the list of students.
     *
     * @param tuitionClasses List of tuition classes to get the earliest hour from.
     * @return Earliest hour of the earliest class timing.
     */
    public LocalTime getEarliestHour(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null && tuitionClasses.size() > 0;

        LocalTime earliestTime = getEarliestTime(tuitionClasses);

        if (earliestTime.isAfter(defaultEarliestHour)) {
            return defaultEarliestHour;
        }

        String earliestTimeStr = earliestTime.toString();
        String earliestHourWithoutMinutes = earliestTimeStr.split(":")[0];
        String earliestHourStr = earliestHourWithoutMinutes + ":00";
        return LocalTime.parse(earliestHourStr, DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Gets the earliest class timing of the list of students.
     *
     * @param tuitionClasses List of students to get the earliest class timing from.
     * @return Earliest class timing from StudentNameList.
     */
    public LocalTime getEarliestTime(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null && tuitionClasses.size() > 0;

        return tuitionClasses.stream()
                .map(TuitionClass::getStartTiming)
                .reduce((classTime1, classTime2) -> classTime1.isBefore(classTime2) ? classTime1 : classTime2)
                .get();
    }

    /**
     * Gets the latest hour which all classes has ended.
     *
     * @param tuitionClasses List of students to get the latest hour from.
     * @return Latest hour which all the classes has ended.
     */
    public LocalTime getLatestHour(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null && tuitionClasses.size() > 0;

        LocalTime latestTime = getLatestTime(tuitionClasses);

        if (latestTime.isBefore(defaultLatestHour)) {
            return defaultLatestHour;
        }

        String latestTimeStr = latestTime.toString();
        String latestHourMinutes = latestTimeStr.split(":")[1];
        if (latestHourMinutes.equals("00")) {
            return latestTime;
        } else {
            String latestHourWithoutMinutes = latestTimeStr.split(":")[0];
            String latestHourStr = latestHourWithoutMinutes + ":00";
            LocalTime latestHour = LocalTime.parse(latestHourStr, DateTimeFormatter.ofPattern("HH:mm"));
            return latestHour.equals(LocalTime.parse("23:00", DateTimeFormatter.ofPattern("HH:mm")))
                    ? latestHour.plusMinutes(59)
                    : latestHour.plusHours(1);
        }
    }

    /**
     * Gets the latest class end time from the list of students.
     *
     * @param tuitionClasses List of students to get the latest class end time from.
     * @return Latest class end time.
     */
    public LocalTime getLatestTime(ObservableList<TuitionClass> tuitionClasses) {
        assert tuitionClasses != null && tuitionClasses.size() > 0;

        return tuitionClasses.stream()
                .map(TuitionClass::getEndTiming)
                .reduce((classTime1, classTime2) -> classTime1.isAfter(classTime2) ? classTime1 : classTime2)
                .get();
    }

    /**
     * Builds the days side panel of the timetable panel ui.
     */
    public void buildDays() {
        timetable.add(new TimetableDay("Mon").getRoot(), 0, 1, 50, 1);
        timetable.add(new TimetableDay("Tue").getRoot(), 0, 2, 50, 1);
        timetable.add(new TimetableDay("Wed").getRoot(), 0, 3, 50, 1);
        timetable.add(new TimetableDay("Thu").getRoot(), 0, 4, 50, 1);
        timetable.add(new TimetableDay("Fri").getRoot(), 0, 5, 50, 1);
        timetable.add(new TimetableDay("Sat").getRoot(), 0, 6, 50, 1);
        timetable.add(new TimetableDay("Sun").getRoot(), 0, 7, 50, 1);
    }

    /**
     * Clears the previously constructed timetable.
     */
    private void clearAll() {
        timetable.getChildren().clear();
        timetable.getRowConstraints().clear();
        timetable.getColumnConstraints().clear();
    }
}
