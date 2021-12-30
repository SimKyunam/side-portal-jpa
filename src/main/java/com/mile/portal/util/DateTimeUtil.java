package com.mile.portal.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class DateTimeUtil {

    /**
     * 두 날짜 사이의 일 수 구하기
     *
     * @param begin
     * @param end
     * @return
     */
    public static Long diffOfDate(String begin, String end) {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date startDt = format.parse(begin);
            Date endDt = format.parse(end);

            long diff = endDt.getTime() - startDt.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            return diffDays;

        } catch (Exception ex) {
            log.info("DateTimeHelper diffOfDate() error. ex: {}", ex.getMessage());
        }

        return null;
    }

    /**
     * 특정 날짜 기준으로 더하고 뺀 날짜 구하기
     *
     * @param inputDate
     * @param type      year/month/date/hour...
     * @param period    더하고 뺄 기간
     * @return
     */
    public static String plusMinusDate(String inputDate, String type, int period) {

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date basicDate = format.parse(inputDate);
            cal.setTime(basicDate);

            if (type != null) {
                switch (type) {
                    case "year":
                        cal.add(Calendar.YEAR, period);
                        break;
                    case "month":
                        cal.add(Calendar.MONTH, period);
                        break;
                    case "date":
                        cal.add(Calendar.DATE, period);
                        break;
                    case "hour":
                        cal.add(Calendar.HOUR_OF_DAY, period);
                        break;
                    case "minute":
                        cal.add(Calendar.MINUTE, period);
                        break;
                    case "second":
                        cal.add(Calendar.SECOND, period);
                        break;
                    default:
                        break;
                }
            }
            return format.format(cal.getTime());
        } catch (Exception ex) {
            log.info("DateTimeHelper plusMinusDate() error. ex: {}", ex.getMessage());
        }

        return null;
    }

    /**
     * 현재날짜 구하기
     *
     * @param format 결과로 출력할 포맷(yyyyMMdd 등)
     * @return
     */
    public static String getToday(String format) {

        try {
            Calendar today = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            if (format.contains("'Z'") || format.contains("'z'") || format.contains("'X'")) {
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return dateFormat.format(today.getTime());
        } catch (Exception ex) {
            log.info("DateTimeHelper getToday() error. ex: {}", ex.getMessage());
        }

        return null;
    }

    /**
     * 현재 만나이 구하기
     *
     * @param birthYear  년도
     * @param birthMonth 월
     * @param birthDay   일
     * @return
     */
    public static int getAge(int birthYear, int birthMonth, int birthDay) {
        Calendar current = Calendar.getInstance();
        int currentYear = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) {
            age--;
        }

        return age;
    }

    /**
     * 특정 날짜가 특정 기간 사이에 있는지 체크
     *
     * @param date      yyyy-MM-dd
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return true/false
     */
    public static boolean isWithinRange(String date, String startDate, String endDate) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            LocalDate startLocalDate = LocalDate.parse(startDate);
            LocalDate endLocalDate = LocalDate.parse(endDate);
            endLocalDate = endLocalDate.plusDays(1);

            return (!localDate.isBefore(startLocalDate)) && (localDate.isBefore(endLocalDate));
        } catch (Exception ex) {
            log.info("DateTimeHelper isWithinRange() error. ex: {}", ex.getMessage());
        }

        return false;
    }

    /**
     * 날짜 포맷 변경(in: case별로 다름 -> out: case별로 다름)
     *
     * @param inDate  변경하려는 일시
     * @param formatB 원본 포맷
     * @param formatA 변경 포맷
     * @return 포맷이 변경된 일시
     */
    public static String transFormat(String inDate, String formatB, String formatA) {
        try {
            SimpleDateFormat dateFormatB = new SimpleDateFormat(formatB);
            if (formatB.contains("'Z'") || formatB.contains("'z'") || formatB.contains("'X'")) {
                dateFormatB.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            Date date = dateFormatB.parse(inDate);
            SimpleDateFormat dateFormatA = new SimpleDateFormat(formatA);
            if (formatA.contains("'Z'") || formatA.contains("'z'") || formatA.contains("'X'")) {
                dateFormatA.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return dateFormatA.format(date);

        } catch (Exception ex) {
            log.info("DateTimeHelper transFormat() error. ex: {}", ex.getMessage());
        }

        return inDate;
    }

    /**
     * 두 날짜 사이에 포함되는 월 조회
     *
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return yyyy-MM
     */
    public static List<String> getMonthList(String startDate, String endDate) {
        List<String> resultList = new ArrayList<>();

        int startYear = Integer.parseInt(startDate.substring(0, 4));
        int startMonth = Integer.parseInt(startDate.substring(5, 7));
        int endYear = Integer.parseInt(endDate.substring(0, 4));
        int endMonth = Integer.parseInt(endDate.substring(5, 7));

        int period = (endYear - startYear) * 12 + (endMonth - startMonth);
        if (period == 0) {
            resultList.add(startDate.substring(0, 7));
        } else {
            resultList.add(startDate.substring(0, 7));
            for (int i = 0; i < period; i++) {
                if (startMonth == 12) {
                    startYear++;
                    startMonth = 1;
                } else {
                    startMonth++;
                }
                if (startMonth < 10) {
                    resultList.add(startYear + "-0" + startMonth);
                } else {
                    resultList.add(startYear + "-" + startMonth);
                }
            }
        }
        return resultList;
    }

    /**
     * 해당 월의 마지막 날짜 구하기
     *
     * @param fromMonth MM
     */
    public static int getLastDayByMonth(int fromMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, fromMonth - 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 해당 년, 월의 마지막 날짜 구하기
     *
     * @param year  yyyy
     * @param month MM
     */
    public static int getLastDayByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * String to Instant 또는 Instant to String
     * 결과값은 String 으로 변환(Instant.toString())
     *
     * @param resType      변환 결과 타입 (STRING, INSTANT)
     * @param stringFormat 날짜 포맷 형태
     * @param stringTime   String 형태의 날짜
     * @param instantTime  Instant 형태의 날짜
     * @return String, Instant.toString()
     */
    public static String transStringInstant(String resType, String stringFormat, String stringTime, Instant instantTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(stringFormat);

        try {
            if (resType.equals("STRING")) {
                if (instantTime != null) {
                    Date tmpDate = Date.from(instantTime);
                    return dateFormat.format(tmpDate);
                } else {
                    return null;
                }
            } else {
                Date tmpDate = dateFormat.parse(stringTime);
                Instant tmpInstant = tmpDate.toInstant();
                return tmpInstant.toString();
            }
        } catch (Exception ex) {
            log.info("DateTimeHelper transStringInstant() error. ex: {}", ex.getMessage());
            return null;
        }
    }

    public static String millisToDate(Long millis, String format) {
        format = format == null ? "YYYY-MM-DD HH:mm:ss.SSS" : format;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(millis);
    }
}
