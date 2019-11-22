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
    // console.log('Run background job.');

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
            try {
              const route = (await this.hereService.routing(zusteller.latDestination, zusteller.lonDestination, zusteller.lat, zusteller.lon)).response;
              if (route.route && route.route.length !== 0) {
                zusteller.stopDelivery = zusteller.startDelivery + (route.route[0].summary.travelTime * 1000);
              } else {
                // TODO throw correct http error
                throw 'no address found';
              }

            } catch (error) {
              console.log(JSON.stringify(error));
            }
          }
      }
      switch (zusteller.status) {
        case ZustellerState.READY:
          if (zusteller.lampColor !== LampColor.GREEN) {
            zusteller.lampColor = LampColor.GREEN;
            await this.hueService.color(zusteller).catch(() => {
              // TODO handle error
            });
          }
          break;

        case ZustellerState.DELIVERED:
          if (zusteller.lampColor !== LampColor.OFF) {
            zusteller.lampColor = LampColor.OFF;
            await this.hueService.color(zusteller).catch(() => {
              // TODO handle error
            });
          }
          break;

        case ZustellerState.DELIVERING:
          const dMinutes = Math.ceil((zusteller.stopDelivery - zusteller.startDelivery) / (1000 * 60));
          const deliveryTemp = zusteller.temperature - dMinutes;
          if (deliveryTemp < 60) {
            if (zusteller.lampColor !== LampColor.BLUE_BLINKING && zusteller.lampColor !== LampColor.BLUE_BLINKING_OFF) {
              zusteller.lampColor = LampColor.BLUE_BLINKING;
              await this.hueService.color(zusteller).catch(() => {
                // TODO handle error
              });
            }
          } else {
            if (zusteller.lampColor !== LampColor.ORANGE) {
              zusteller.lampColor = LampColor.ORANGE;
              await this.hueService.color(zusteller).catch(() => {
                // TODO handle error
              });
            }
          }
      }
    }));
  }
}
