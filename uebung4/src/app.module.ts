import { HttpModule, Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ScheduleModule } from 'nest-schedule';
import { ScheduleService } from './schedule.service';
import { HereService } from './here.service';
import { HueService } from './hue.service';

@Module({
  imports: [
    ScheduleModule.register(),
    HttpModule,
  ],
  controllers: [AppController],
  providers: [
    AppService,
    ScheduleService,
    HereService,
    HueService,
  ],
})
export class AppModule {}
