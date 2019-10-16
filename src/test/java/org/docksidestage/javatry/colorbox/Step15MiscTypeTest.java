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

import static org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.BOXES;
import static org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.getBoxStream;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.docksidestage.bizfw.colorbox.impl.StandardColorBox;
import org.docksidestage.bizfw.colorbox.space.BoxSpace;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom;
import org.docksidestage.bizfw.colorbox.yours.YourPrivateRoom.FavoriteProvider;
import org.docksidestage.unit.PlainTestCase;

/**
 * The test of various type with color-box. <br>
 * Show answer by log() for question of javadoc.
 * @author jflute
 * @author your_name_here
 */
public class Step15MiscTypeTest extends PlainTestCase {

    // ===================================================================================
    //                                                                           Exception
    //                                                                           =========
    /**
     * What class name is throw-able object in color-boxes? <br>
     * (カラーボックスに入っているthrowできるオブジェクトのクラス名は？)
     */
    public void test_throwable() {
        String name = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Throwable)
                .map(Object::getClass)
                .map(Class::getName)
                .findFirst()
                .get();
        log(name);
    }

    /**
     * What message is for exception that is nested by exception in color-boxes? <br>
     * (カラーボックスに入っている例外オブジェクトのネストした例外インスタンスのメッセージは？)
     */
    public void test_nestedException() {
        String msg = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof Throwable)
                .map(Throwable.class::cast)
                .map(Throwable::getCause)
                .map(Throwable::getMessage)
                .findFirst()
                .get();
        log(msg);
    }

    // ===================================================================================
    //                                                                           Interface
    //                                                                           =========
    /**
     * What value is returned by justHere() of FavoriteProvider in yellow color-box? <br>
     * (カラーボックスに入っているFavoriteProviderインターフェースのjustHere()メソッドの戻り値は？)
     */
    public void test_interfaceCall() {
        String here = getBoxStream()
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof FavoriteProvider)
                .map(FavoriteProvider.class::cast)
                .map(FavoriteProvider::justHere)
                .findFirst()
                .get();
        log(here);
    }

    // ===================================================================================
    //                                                                            Optional
    //                                                                            ========
    // https://stackoverflow.com/a/22726869
    static <T> Stream<T> streamopt(Optional<T> opt) { return opt.map(Stream::of).orElse(Stream.empty()); }

    /**
     * What keyword is in BoxedStage of BoxedResort in List in beige color-box? (show "none" if no value) <br>
     * (beigeのカラーボックスに入っているListの中のBoxedResortのBoxedStageのkeywordは？(値がなければ固定の"none"という値を))
     */
    public void test_optionalMapping() {
        String keyword = BOXES.stream()
                .filter(colorBox -> colorBox.getColor().getColorName().equals("beige"))
                .flatMap(colorBox -> colorBox.getSpaceList().stream())
                .map(BoxSpace::getContent)
                .filter(obj -> obj instanceof List)
                .map(obj -> (List<Object>) obj)
                .flatMap(List::stream)
                .filter(obj -> obj instanceof YourPrivateRoom.BoxedResort)
                .peek(this::log)
                .map(YourPrivateRoom.BoxedResort.class::cast)
                .map(boxedResort -> boxedResort.getPark())
                .flatMap(Step15MiscTypeTest::streamopt)
                .map(YourPrivateRoom.BoxedPark::getStage)
                .flatMap(Step15MiscTypeTest::streamopt)
                .map(YourPrivateRoom.BoxedStage::getKeyword)
                .filter(Objects::nonNull)
                .findFirst()
                .get();
        log(keyword);
    }

    // ===================================================================================
    //                                                                           Challenge
    //                                                                           =========
    /**
     * What line number is makeEighthColorBox() call in getColorBoxList()? <br>
     * (getColorBoxList()メソッドの中のmakeEighthColorBox()メソッドを呼び出している箇所の行数は？)
     */
    public void test_lineNumber() {
        Exception e = (Exception) ((StandardColorBox) BOXES.get(7)).getMiddleSpace().getContent();
        int line = e.getStackTrace()[1].getLineNumber();
        log(line);
    }
}
