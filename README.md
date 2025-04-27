# ringle

# 개요
링글 Tech팀 Backend 과제

## 개발 기간
2025.04.25 - 2025.04.27

## 기능
1. 튜터가 이용할 API
   1) 수업 가능한 시간대 생성 & 삭제
2. 학생이 이용할 API
   1) 기간 & 수업 길이로 현재 수업 가능한 시간대를 조회
   2) 시간대 & 수업 길이로 수업 가능한 튜터 조회
   3) 시간대, 수업길이, 튜터로 새로운 수업 신청
   4) 신청한 수업 조회

## 기술
<img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=flat-square&logo=Spring&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=yellow"/>
<img src="https://img.shields.io/badge/MySql-003545?style=flat-square&logo=Mysql&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>
<br>
Java - 17 <br>
Spring Boot - 3.4.5

## 테스트 방법
0. DB
<br> 1) 유저 정보
```
INSERT INTO `users` VALUES (1,'tutor1@ringle.com','TUTOR','tutor1'),(2,'student1@ringle.com','STUDENT','student1'),(3,'tutor2@ringle.com','TUTOR','tutor2');
```
<br>

1. 튜터가 이용할 API
   1) 수업 가능한 시간대 생성
```
POST : http://localhost:8080/api/availability
{
  "tutorId": 1,
  "date": "2025-04-27",
  "startTime": "12:00"
}
```

   2) 수업 가능한 시간대 삭제
```
DELECT : http://localhost:8080/api/availability
{
  "tutorId": 1,
  "date": "2025-04-26",
  "startTime": "09:00"
}
```
<br>

2.학생이 이용할 API
   1) 기간 & 수업 길이로 현재 수업 가능한 시간대를 조회
```
GET : http://localhost:8080/api/courses/available-times?date=2025-04-27&durationMinutes=30
{
  "date": "yyyy-mm-dd",
  "durationMinutes": 30 or 60
}
```
   2) 시간대 & 수업 길이로 수업 가능한 튜터 조회
```
GET : http://localhost:8080/api/courses/available-tutors?date=2025-04-27&startTime=09:00&durationMinutes=30
{
   "date": "yyyy-mm-dd",
   "startTime" : "HH:mm",
   "durationMinutes": 30 or 60
}
```
   3) 시간대, 수업길이, 튜터로 새로운 수업 신청
```
POST : http://localhost:8080/api/courses
{
    "studentId" : 2,
    "tutorId" : 1,
    "date" : "2025-04-27",
    "startTime" : "09:00",
    "durationMinutes" : 60
}
```
   4) 신청한 수업 조회
```
GET : http://localhost:8080/api/courses/mine?studentId=2
{
  "studentId": "2"
}
```
