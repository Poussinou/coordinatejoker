/*
 * Copyright (c) 2018 by siggel <siggel-apps@gmx.de>
 *
 *     This file is part of Coordinate Joker.
 *
 *     Coordinate Joker is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Coordinate Joker is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Coordinate Joker.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.siggel.coordinatejoker;

/**
 * a named waypoint
 */
class Point {

    // point values
    private String name; // name
    private double latitude; // latitude in degrees
    private double longitude; // longitude in degrees

    /**
     * constructor
     *
     * @param name      name of the point
     * @param latitude  latitude value in degrees
     * @param longitude longitude value in degrees
     */
    Point(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getters and setters need no explanation

    double getLatitude() {
        return latitude;
    }

    @SuppressWarnings("unused")
    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    double getLongitude() {
        return longitude;
    }

    @SuppressWarnings("unused")
    void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    void setName(String name) {
        this.name = name;
    }
}