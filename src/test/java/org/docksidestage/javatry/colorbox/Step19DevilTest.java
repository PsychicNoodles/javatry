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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.docksidestage.bizfw.colorbox.AbstractColorBox;
import org.docksidestage.bizfw.colorbox.ColorBox;
import org.docksidestage.bizfw.colorbox.impl.CompactColorBox;
import org.docksidestage.bizfw.colorbox.impl.StandardColorBox;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of Devil with color-box, (try if you woke up Devil in StringTest) <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step19DevilTest extends PlainTestCase {

    // ===================================================================================
    //                                                                        Devil Parade
    //                                                                        ============
    /**
     * What is the content in low space of color-box
     * of which lengths of the color is same as first place number of BigDecimal value first found in List in box spaces,
     * that the second decimal place is same as tens place of depth of the color-box
     * of which color name ends with third character of color-box that contains null as content? <br>
     * (nullを含んでいるカラーボックスの色の名前の3文字目の文字で色の名前が終わっているカラーボックスの深さの十の位の数字が小数点第二桁目になっている
     * スペースの中のリストの中で最初に見つかるBigDecimalの一の位の数字と同じ色の長さのカラーボックスの一番下のスペースに入っているものは？)
     */
    public void test_too_long() {
        List<Character> thirds = BOXES.stream()
                .filter(colorBox -> colorBox.getSpaceList().stream()
                        .map(BoxSpace::getContent)
                        .map(YourPrivateRoom::getDevilContent)
                        .anyMatch(Objects::isNull))
                .peek(this::log)
                .map(colorBox -> colorBox.getColor().getColorName())
                .map(s -> s.charAt(2))
                .distinct()
                .collect(Collectors.toList());
        log("thirds " + thirds);

        List<Integer> tens = BOXES.stream()
                .filter(colorBox -> thirds.stream().anyMatch(c -> {
                    String colorName = colorBox.getColor().getColorName();
                    return c == colorName.charAt(colorName.length() - 1);
                }))
                .map(colorBox -> Integer.toString(colorBox.getSize().getDepth()))
                .map(s -> Character.getNumericValue(s.charAt(s.length() - 2)))
                .collect(Collectors.toList());
        log("tens " + tens);

        List<BigDecimal> seconds = BOXES.stream()
                .filter(colorBox -> colorBox.getSpaceList().stream().map(BoxSpace::getContent).anyMatch(o -> o instanceof List))
                .flatMap(colorBox -> colorBox.getSpaceList().stream()
                            .map(BoxSpace::getContent)
                            .filter(o -> o instanceof List)
                            .map(o -> (List<Object>) o)
                            .flatMap(List::stream)
                            .map(YourPrivateRoom::toDecimalType)
                            .filter(Objects::nonNull)
                            .filter(bigDecimal -> YourPrivateRoom.getNumberOfDecimalPlaces(bigDecimal) > 2))
                .peek(this::log)
                .collect(Collectors.toList());
        log("seconds " + seconds);

        List<Object> boxes = BOXES.stream()
                .filter(colorBox -> seconds.stream()
                        .map(bigDecimal -> bigDecimal.remainder(BigDecimal.ONE).stripTrailingZeros().toPlainString())
                        .map(s -> s.charAt(3))
                        .map(Character::getNumericValue)
                        .anyMatch(f -> f == colorBox.getColor().getColorName().length()))
                .collect(Collectors.toList());

        List<BoxSpace> end = Stream.concat(
                boxes.stream()
                        .filter(o -> o instanceof StandardColorBox)
                        .map(StandardColorBox.class::cast)
                        .map(StandardColorBox::getLowerSpace),
                boxes.stream()
                        .filter(o -> o instanceof CompactColorBox)
                        .map(CompactColorBox.class::cast)
                        .map(CompactColorBox::getLowerSpace))
                .collect(Collectors.toList());

        log(end);
    }

    // ===================================================================================
    //                                                                      Java Destroyer
    //                                                                      ==============
    /**
     * What string of toString() is BoxSize of red color-box after changing height to 160 (forcedly in this method)? <br>
     * ((このテストメソッドの中だけで無理やり)赤いカラーボックスの高さを160に変更して、BoxSizeをtoString()すると？)
     */
    public void test_looks_like_easy() {
    }

    // ===================================================================================
    //                                                                        Meta Journey
    //                                                                        ============
    /**
     * What value is returned from functional method of interface that has FunctionalInterface annotation in color-boxes? <br> 
     * (カラーボックスに入っているFunctionalInterfaceアノテーションが付与されているインターフェースのFunctionalメソッドの戻り値は？)
     */
    public void test_be_frameworker() {
    }
}
