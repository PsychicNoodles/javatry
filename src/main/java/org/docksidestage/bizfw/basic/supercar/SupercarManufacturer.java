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
package org.docksidestage.bizfw.basic.supercar;

import org.docksidestage.bizfw.basic.supercar.SupercarSteeringWheelManufacturer.SteeringWheel;
import org.docksidestage.bizfw.basic.supercar.SupercarSteeringWheelManufacturer.SupercarSteeringWheelCannotMakeByIdException;

/**
 * The manufacturer(製造業者) of supercar.
 * @author jflute
 */
public class SupercarManufacturer {

    private final SupercarEasyCatalog catalog = new SupercarEasyCatalog();

    public Supercar makeSupercar(String catalogKey) {
        Integer steeringWheelId = catalog.findSteeringWheelSpecId(catalogKey);

        SupercarSteeringWheelManufacturer manufacturer = createSupercarSteeringWheelManufacturer();
        SteeringWheel steeringWheel;
        try {
            steeringWheel = manufacturer.makeSteeringWheel(steeringWheelId);
        } catch (SupercarSteeringWheelCannotMakeByIdException e) {
            throw new SupercarManufacturerManufacturingException(catalogKey, e);
        }

        return new Supercar(steeringWheel);
    }

    protected SupercarSteeringWheelManufacturer createSupercarSteeringWheelManufacturer() {
        return new SupercarSteeringWheelManufacturer();
    }

    public static class Supercar {

        public Supercar(SteeringWheel steeringWheel) {
            // dummy
        }
    }

    public static class SupercarManufacturerManufacturingException extends RuntimeException {
        public SupercarManufacturerManufacturingException(String key, Throwable cause) {
            super("Could not manufacture supercar with key " + key, cause);
        }
    }
}
