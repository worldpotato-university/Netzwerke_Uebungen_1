import { Injectable } from '@nestjs/common';
import { Interval, NestSchedule } from 'nest-schedule';

@Injectable()
export class ScheduleService extends NestSchedule {
  @Interval(5000, { waiting: true, enable: true })
  async intervalJob() {
    console.log('Interval Job');

  }
}
