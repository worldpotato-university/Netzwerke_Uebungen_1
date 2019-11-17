import { Injectable } from '@nestjs/common';
import { Interval, NestSchedule } from 'nest-schedule';
import { AppService } from './app.service';
import { HereService } from './here.service';
import { ZustellerState } from './zusteller';
import { HueService, LampColor } from './hue.service';

@Injectable()
export class ScheduleService extends NestSchedule {
  public constructor(
    private readonly hereService: HereService,
    private readonly hueService: HueService,
  ) {
    super();
  }

  @Interval(5000, { waiting: true, enable: true })
  async intervalJob() {
    console.log('Run background job.');

    await Promise.all(AppService.zusteller.map(async zusteller => {
      switch (zusteller.status) {
        case ZustellerState.READY:
          // TODO: nothing to here?
          break;

        case ZustellerState.DELIVERED:
          // TODO: nothing to here?
          break;

        case ZustellerState.DELIVERING:
          if (zusteller.stopDelivery !== 0 && Date.now() > zusteller.stopDelivery) {
            zusteller.status = ZustellerState.DELIVERED;
            zusteller.stopDelivery = 0;
          } else {
            const route = (await this.hereService.routing(zusteller.latDestination, zusteller.lonDestination, zusteller.lat, zusteller.lon)).response;
            if (route.route && route.route.length !== 0) {
              zusteller.stopDelivery = zusteller.startDelivery + (route.route[0].summary.travelTime * 1000);
            } else {
              // TODO throw correct http error
              throw 'no address found';
            }
          }
      }
      switch (zusteller.status) {
        case ZustellerState.READY:
          await this.hueService.color(zusteller.lamp, LampColor.GREEN);
          break;

        case ZustellerState.DELIVERED:
          await this.hueService.color(zusteller.lamp, LampColor.OFF);
          break;

        case ZustellerState.DELIVERING:
          const dMinutes = Math.ceil((zusteller.stopDelivery - zusteller.startDelivery) / (1000 * 60));
          const deliveryTemp = zusteller.temperature - dMinutes;
          if (deliveryTemp < 60) {
            await this.hueService.color(zusteller.lamp, LampColor.BLUE_BLINKING);
          } else {
            await this.hueService.color(zusteller.lamp, LampColor.ORANGE);
          }
      }
    }));
  }
}
