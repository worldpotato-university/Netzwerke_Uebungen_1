import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ScheduleModule } from 'nest-schedule';
import { ScheduleService } from './schedule.service';

@Module({
  imports: [ScheduleModule.register()],
  controllers: [AppController],
  providers: [AppService, ScheduleService],
})
export class AppModule {}
