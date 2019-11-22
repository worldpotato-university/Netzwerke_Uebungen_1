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
          console.log(`${zusteller.name} messenger is ready.`);
          break;

        case ZustellerState.DELIVERED:
          console.log(`${zusteller.name} has delivered.`);
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
                throw 'Corrupt message from messenger. Don\'t change the arriving time';
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
              throw `hue-service is corrupt - can set lamp color to GREEN for ${zusteller.name}`
            });
          }
          break;

        case ZustellerState.DELIVERED:
          if (zusteller.lampColor !== LampColor.OFF) {
            zusteller.lampColor = LampColor.OFF;
            await this.hueService.color(zusteller).catch(() => {
              throw `hue-service is corrupt - can not switch of the lamp for ${zusteller.name}`
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
                throw `hue-service is corrupt - can not set lamp color to blinking-blue for ${zusteller.name}`
              });
            }
          } else {
            if (zusteller.lampColor !== LampColor.ORANGE) {
              zusteller.lampColor = LampColor.ORANGE;
              await this.hueService.color(zusteller).catch(() => {
                throw `hue-service is corrupt - can not set lamp color to orange for ${zusteller.name}`
              });
            }
          }
      }
    }));
  }
}
