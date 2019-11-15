import { Injectable } from '@nestjs/common';
import { Interval, NestSchedule } from 'nest-schedule';
import { AppService } from './app.service';

@Injectable()
export class ScheduleService extends NestSchedule {
  @Interval(5000, { waiting: true, enable: true })
  async intervalJob() {
    console.log('Run background job.');

    await Promise.all(AppService.zusteller.map(zusteller => zusteller.checkStatus()));
  }
}
