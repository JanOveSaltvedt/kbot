/*	
	Copyright 2012 Jan Ove Saltvedt
	
	This file is part of KBot.

    KBot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KBot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KBot.  If not, see <http://www.gnu.org/licenses/>.
	
*/

package com.kbotpro.scriptsystem.various;
/**
 * Timer class that can be used to measure time passing or as a countdown timer.
 *
 * @author Exarctus & 933pm
 */
public class KTimer {

	private long startTime = 0;
	private long endTime = 0;

	/**
	 * Use for countdowns.
	 * @param timeToRun Countdown time. No effect once timer is finished. Use isDone() to check.
	 */
	public KTimer(long timeToRun) {
		if (timeToRun < 0) {
			timeToRun *= -1;
		}
		startTime = System.currentTimeMillis();
		endTime = startTime + timeToRun;
	}

	/**
	 * Use to measure the passing of time.
	 */
	public KTimer() {
		startTime = System.currentTimeMillis();
		endTime = startTime;
	}

	/**
	 * Add time to a countdown.
	 * @param timeToRun Number of milliseconds to add.
	 */
	public void addTime(long timeToRun) {
		//edit by Apples
		endTime = endTime + timeToRun;
	}

	/**
	 * Creates a new end time.
	 * @param timeToRun Number of milliseconds to end from the current time.
	 */
	//Replaces old addTime method
	public void newEndTime(long timeToRun) {
		endTime = System.currentTimeMillis() + timeToRun;
	}

	/**
	 * Completely reset timer.
	 */
	public void reset() {
		startTime = System.currentTimeMillis();
		endTime = startTime;
	}

	/**
	 * Check if countdown is finished.
	 * @return True if finished, false if still going.
	 */
	public boolean isDone() {
		if (endTime <= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * Get time remaining in countdown.
	 * @return Time remaining until countdown is finished.
	 */
	public long getTimeRemaining() {
		return endTime - System.currentTimeMillis();
	}

	/**
	 * Get time passed since timer initialization.
	 * @return Time passed.
	 */
	public long getTimeElapsed() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Formats time in the "HH:MM:SS" format.
	 * @param timeMilliseconds Time to format.
	 * @return Formatted time.
	 */
	public String getFormattedTime(long timeMilliseconds) {
		StringBuilder b = new StringBuilder();
		long runtime = timeMilliseconds;
		long TotalSecs = runtime / 1000;
		long TotalMins = TotalSecs / 60;
		long TotalHours = TotalMins / 60;
		int seconds = (int) TotalSecs % 60;
		int minutes = (int) TotalMins % 60;
		int hours = (int) TotalHours % 60;
		if (hours < 10) {
			b.append("0");
		}
		b.append(hours);
		b.append(" : ");
		if (minutes < 10) {
			b.append("0");
		}
		b.append(minutes);
		b.append(" : ");
		if (seconds < 10) {
			b.append("0");
		}
		b.append(seconds);
		return b.toString();
	}

}