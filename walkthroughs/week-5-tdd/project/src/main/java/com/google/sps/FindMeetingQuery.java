// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.ArrayList;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //mandatory attendees of meeting request
    Collection<String> attendees = request.getAttendees();
    //times when at least one of the meeting request attendees is busy
    ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();
    for (Event event : events){
      //get required attendees for each event
      Set<String> eventAttendees = event.getAttendees();
      //if at least one of the meeting request attendees has to attend event, then that block of time becomes unavailable
      for (String attendee : eventAttendees){
        if (attendees.contains(attendee)){
          unavailableTimes.add(event.getWhen());
          break;
        }
      }
    }

    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);
    //get duration of meeting request
    long meetingDuration = request.getDuration();
    
    Collection<TimeRange> availableTimes = new ArrayList<TimeRange>();
    int start = TimeRange.START_OF_DAY;
    //find available times and add to collection
    for (TimeRange eventTime : unavailableTimes){
      if (!eventTime.contains(start) && eventTime.start() - start >= meetingDuration){
        availableTimes.add(TimeRange.fromStartEnd(start, eventTime.start(), false));
      }

      start = eventTime.end();
    }

    if (TimeRange.END_OF_DAY - start >= meetingDuration){
      availableTimes.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
    }
    return availableTimes;
  }
}
