/*
 Doge.js (Get your own at https://github.com/xxyy/doge.js/)
 Copyright (C) 2014 Philipp Nowak / xxyy (xxyy.github.io)

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

var doges = ["wow", "such fox", "many bot", "very sophisticate"];
var colors = ["#0040FF", "#2EFEF7", "#DF0101", "#088A08", "#CC2EFA", "#FFBF00"];

/**
 * Builds a doge saying with color, text and position randomised.
 */
function buildDoge() {
    //Need class for identification
    return "<div class='doge' style='color: "
        // Set font color to random entry of colors array
        // Math.random returns a number between 0 and 1
        + colors[Math.floor(Math.random() * colors.length)]
        + "; left: "
        // Set random distance from left screen edge in %
        + Math.floor(Math.random() * 95) //don't want overflow
        + "%; top: "
        // Set random distance from left screen edge in %
        + Math.floor(Math.random() * 95) //don't want overflow
        + "%;'>"
        // Set the text to a random doge saying from the doges array
        + doges[Math.floor(Math.random() * doges.length)]
        + "</div>";
}

/**
 * Displays a random doge saying, with fade-in and fade-out animations.
 * @see buildDoge()
 */
function showDoge() {
    //Appends a new doge saying to the doge container (will be the body element in most cases)
    $(".wow").append(buildDoge);
    // Apply a fadeIn function to all doge sayings on the page with a duration of 2 seconds.
    // fade in won't occur without calling hide() before:
    $(".doge").hide().fadeIn(1500, function() {
        //Once complete, fade all doge sayings out with a duration of 2 seconds.
        $(".doge").fadeOut(2000, function() {
            //Once that has completed too, remove all doge sayings from the page
            $(".doge").remove();
            //Recurse this function so a new saying is displayed
            showDoge(); // wow such recurse
        });
    });
}

//Start doge saying display loop on load
$(window).on("load", function() {
    showDoge();
});