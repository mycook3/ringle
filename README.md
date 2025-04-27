# ringle

# 개요
링글 Tech팀 Backend 과제

# 학생-튜터 수업 시스템의 기본 흐름
1. 학생은 튜터가 오픈한 시간대에 수업을 신청할 수 있으며, 30분 또는 60분의 수업 길이를 선택할 수 있음.<br>
이때 예약된 시간대는 중복 신청할 수 없도록 해야 하므로, 예약 현황을 추적하는 로직이 필요.
또한, 수업 신청/취소 등의 관리 기능도 필수적으로 구현해야 함.

2. 엔티티 설계
   수업 신청 시스템을 설계할 때 주요한 엔티티는 User, Course, Availability

   1) User: 학생(Student)과 튜터(Tutor)를 구분하기 위해 role 필드를 사용
   <br>   하나의 User가 Student나 Tutor로 각각 역할을 맡게 되며, 이에 따라 수업을 신청하거나 오픈하는 역할을 구분

   2) Availability:
튜터가 수업 가능 시간을 설정하는 엔티티<br>
수업 길이는 기본적으로 30분 단위로 설정되며, 튜터가 시간을 선택해 오픈
<br>학생은 이 시간대에 맞춰 수업을 신청할 수 있음

   3) Course: 학생이 수업을 신청한 내역을 기록하는 엔티티<br>
각 Course는 학생, 튜터, 시간대를 연결
<br>수업의 중복 예약을 방지하기 위해, 이미 예약된 시간대는 다른 학생이 신청할 수 없도록 구현

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

## 개선 할 부분
1. 에러 처리를 Global에서 Handler를 통해 처리
2. 로그인 구현 시 세션 또는 토큰으로 사용자 ID 받아오기