# Site Activity Reporting Service

# Problem

Create a website activity and reporting program thatadds activity events by duration for the
most recent hour. This will require building out aweb server that implements the two APIs
defined below.

# APIs

## POST activity events

**Request**

```
POST /activity/{key}
{
"value": 4
}
```
**Response (200)**

```
{}
```
## GET activity events total

Returns the aggregate sum of all activity events reportedfor the key over the past 12 hours.

**Request**

```
GET /activity/{key}/total
```

**Response (200)**

```
{
"value": 500
}
```
# Hard Requirements

```
● Store the activity value as an integer (round to thenearest number).
● Use an in-memory data structure to hold the activityvalues.
● Prune activity data that is older than 12 hours.
● Make sure the API has a constant response time (O( 1 )).
● Make sure the API can handle multiple users concurrently.
● Write unit tests for all functions with 100% pathcoverage.
```
# Example

Imagine these are the events logged to your servicefor alearn_more_pageactivity:

```
// 781 minutes ago **
POST /activity/learn_more_page { "value": 16 }
```
```
// 510 minutes ago
POST /activity/learn_more_page { "value”: 5 }
```
```
// 50 seconds ago
POST /activity/learn_more_page { "value": 32 }
```
```
// 3 seconds ago
POST /activity/learn_more_page { "value": 4 }
```
These are the results expected from calling get aggregates:


```
GET /activity/learn_more_page/total // returns 41
```
** Note that the value posted >12 hours is not includedsince the only data relevant for these
APIs is that which was reported in the last 12 hours.
