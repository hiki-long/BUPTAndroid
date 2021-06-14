package com.example.myapplication.ui.course

import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.model.ScheduleEnable

/**
 * 自定义实体类需要实现ScheduleEnable接口并实现getSchedule()
 *
 * @see ScheduleEnable.getSchedule
 */
class MySubject(
    /**
     * 课程名
     */
    var name: String,
    /**
     * 教室
     */
    var room: String?,
    /**
     * 教师
     */
    var teacher: String,
    /**
     * 第几周至第几周上
     */
    var weekList: List<Int>?,
    /**
     * 开始上课的节次
     */
    var start: Int,
    /**
     * 上课节数
     */
    var step: Int,
    /**
     * 周几上
     */
    var day: Int,
    colorRandom: Int,
    time: String?
) : ScheduleEnable {
    var id = 0

    //无用数据
    lateinit var time: String

    /**
     * 一个随机数，用于对应课程的颜色
     */
    var colorRandom = 0
    var url: String? = null

    override fun getSchedule(): Schedule {
        val schedule = Schedule()
        schedule.day = day
        schedule.name = name
        schedule.room = room
        schedule.start = start
        schedule.step = step
        schedule.teacher = teacher
        schedule.weekList = weekList
        schedule.colorRandom = 2
        return schedule
    }

    init {
        this.colorRandom = colorRandom
        if (time != null) {
            this.time = time
        }
    }
}