/*
 * Copyright 2019-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.docksidestage.javatry.colorbox;

import static org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.*;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.space.DoorBoxSpace;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of Date with color-box. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step14DateTest extends PlainTestCase {

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    /**
     * What string is date in color-boxes formatted as slash-separated (e.g. 2019/04/24)? <br>
     * (カラーボックスに入っている日付をスラッシュ区切り (e.g. 2019/04/24) のフォーマットしたら？)
     */
    public void test_formatDate() {
        String str = extractLocalDateTimes()
                .map(localDateTime -> localDateTime.format(DateTimeFormatter.ofPattern("uuuu/MM/dd")))
                .findFirst()
                .orElse("*no date found");
        log(str);
    }

    /**
     * What string of toString() is converted to LocalDate from slash-separated date string (e.g. 2019/04/24) in Set in yellow color-box? <br>
     * (yellowのカラーボックスに入っているSetの中のスラッシュ区切り (e.g. 2019/04/24) の日付文字列をLocalDateに変換してtoString()したら？)
     */
    public void test_parseDate() {
        String str = BOXES.stream()
                .filter(colorBox -> colorBox.getColor().getColorName().equals("yellow"))
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Set)
                .map(obj -> (Set<Object>) obj)
                .flatMap(Set::stream)
                .filter(obj -> obj instanceof String)
                .map(String.class::cast)
                .map(s -> {
                    try {
                        return LocalDate.parse(s, DateTimeFormatter.ofPattern("uuuu/MM/dd"));
                    } catch (DateTimeParseException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                .map(LocalDate::toString)
                .orElse("*no date found");
        log(str);
    }

    /**
     * What is total of month numbers of date in color-boxes? <br>
     * (カラーボックスに入っている日付の月を全て足したら？)
     */
    public void test_sumMonth() {
        int total = extractLocalDateTimes()
                .mapToInt(localDateTime -> localDateTime.getMonthValue())
                .sum();
        log(total);
    }

    /**
     * What day of week is second-found date in color-boxes added to three days? <br>
     * (カラーボックスに入っている二番目に見つかる日付に3日進めると何曜日？)
     */
    public void test_plusDays_weekOfDay() {
        int day = extractLocalDateTimes()
                .skip(1)
                .map(LocalDateTime::getDayOfWeek)
                .map(DayOfWeek::getValue)
                .findFirst()
                .orElse(-1);
        log(day == -1 ? "*day of week not found" : day);
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * How many days (number of day) are between two dates in yellow color-boxes?   <br>
     * (yellowのカラーボックスに入っている二つの日付は何日離れている？)
     */
    public void test_diffDay() {
        List<LocalDateTime> days = extractLocalDateTimes()
                .collect(Collectors.toList());
        long diff = ChronoUnit.DAYS.between(days.get(0).toLocalDate(), days.get(1).toLocalDate());
        log(diff);
    }

    /**
     * What date is LocalDate in yellow color-box
     * that is month-added with LocalDateTime's seconds in the same color-box,
     * and is day-added with Long value in red color-box,
     * and is day-added with the first decimal place of BigDecimal that has three (3) as integer in List in color-boxes? <br>
     * (yellowのカラーボックスに入っているLocalDateに、同じカラーボックスに入っているLocalDateTimeの秒数を月数として足して、
     * redのカラーボックスに入っているLong型を日数として足して、カラーボックスに入っているリストの中のBigDecimalの整数値が3の小数点第一位の数を日数として引いた日付は？)
     */
    public void test_birthdate() {
        LocalDate date = BOXES.stream()
                .filter(colorBox -> colorBox.getColor().getColorName().equals("yellow"))
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof LocalDate)
                .map(LocalDate.class::cast)
                .findFirst()
                .get();
        int months = BOXES.stream()
                .filter(colorBox -> colorBox.getColor().getColorName().equals("yellow"))
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof LocalDateTime)
                .map(LocalDateTime.class::cast)
                .map(LocalDateTime::getSecond)
                .findFirst()
                .get();
        long days = BOXES.stream()
                .filter(colorBox -> colorBox.getColor().getColorName().equals("red"))
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Long)
                .map(Long.class::cast)
                .findFirst()
                .get();
        int days2 = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof List)
                .map(obj -> (List<Object>) obj)
                .flatMap(list -> list.stream().filter(v -> v instanceof BigDecimal).map(obj -> (BigDecimal) obj))
                .filter(bigDecimal -> bigDecimal.intValue() == 3)
                .map(bigDecimal -> {
                    String s = bigDecimal.stripTrailingZeros().toPlainString();
                    return s.charAt(s.indexOf('.') + 1);
                })
                .map(Character::getNumericValue)
                .findFirst()
                .get();
        log(date);
        log(months);
        log(days);
        log(days2);
        date = date.plusMonths(months).plusDays(days).plusDays(days2);
        log(date);
    }

    /**
     * What second is LocalTime in color-boxes? <br>
     * (カラーボックスに入っているLocalTimeの秒は？)
     */
    public void test_beReader() {
        int second = getBoxStream()
                .filter(boxSpace -> boxSpace instanceof DoorBoxSpace)
                .map(DoorBoxSpace.class::cast)
                .peek(DoorBoxSpace::openTheDoor)
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof LocalTime)
                .map(LocalTime.class::cast)
                .map(LocalTime::getSecond)
                .findFirst()
                .get();
        log(second);
    }
}
