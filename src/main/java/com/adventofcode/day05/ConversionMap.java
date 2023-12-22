package com.adventofcode.day05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConversionMap {

    private final String source;
    private final String destination;
    private final List<MapRange> ranges;

    private boolean isSorted;


    public ConversionMap(String line) {
        var parts = line.split(" ")[0].split("-to-");
        source = parts[0];
        destination = parts[1];
        ranges = new ArrayList<>();
        isSorted = false;
    }

    public void parseRangeLine(String line) {
        isSorted = false;
        ranges.add(MapRange.parseLine(line));
    }

    public List<Interval> mapRangeToDestination(Interval interval) {
        sortRanges();

        Long start = interval.start();
        var end = interval.end();

        var iStart = findRangeIdx(start);
        var iEnd = findRangeIdx(end);

        List<Interval> intervals = new ArrayList<>();
        if (iStart == -1) {
            intervals.add(new Interval(start, Math.min(end, getFirst().startSource())));
            start = getFirst().startSource();

            if (iEnd == -1) {
                return intervals;
            }
        } else if (iStart < ranges.size() && iStart == iEnd && start > ranges.get(iStart).endSource()) {
            return List.of(interval);
        } else {
            start = null;
        }

        for (int i = Math.max(iStart, 0); i <= Math.min(iEnd, ranges.size() - 1); i++) {
            var mapRange = ranges.get(i);
            if (start == null) {
                start = interval.start();
            } else {
                intervals.add(new Interval(start, mapRange.startSource()));
                start = mapRange.startSource();
            }
            var deltaStart = start - mapRange.startSource();
            var deltaEnd = end - mapRange.startSource();
            intervals.add(new Interval(
                mapRange.startDestination() + deltaStart,
                Math.min(mapRange.range(), deltaEnd) + mapRange.startDestination()
            ));
            start = mapRange.endSource();
        }

        if (iEnd == ranges.size()) {
            start = iStart == ranges.size() ? interval.start() : getLast().endSource();
            intervals.add(new Interval(start, end));
        }

        return intervals;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public MapRange getFirst() {
        return ranges.get(0);
    }

    public MapRange getLast() {
        return ranges.get(ranges.size() - 1);
    }

    private void sortRanges() {
        if (isSorted) return;
        ranges.sort(Comparator.comparingLong(MapRange::startSource));
        isSorted = true;
    }

    private int findRangeIdx(long value) {
        if (value < getFirst().startSource()) {
            return -1;
        } else if (value >= getLast().endSource()) {
            return ranges.size();
        }

        var iLeft = 0;
        var iRight = ranges.size();
        while (iRight - iLeft > 1) {
            var iMid = (iLeft + iRight) / 2;
            if (ranges.get(iMid).startSource() > value) {
                iRight = iMid;
            } else {
                iLeft = iMid;
            }
        }

        return iLeft;
    }
}
