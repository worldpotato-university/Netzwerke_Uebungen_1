import { NestSchedule, Interval } from 'nest-schedule';
import { AppService } from './app.service';
import { Zusteller } from './zusteller';
import * as http from 'http';

export enum LampColor {
  OFF,
  GREEN,
  ORANGE,
  BLUE_BLINKING,
  BLUE_BLINKING_OFF,
}

export class HueService extends NestSchedule {

  public async color(zusteller: Zusteller) {
    let data;
    if (zusteller.lampColor === LampColor.BLUE_BLINKING_OFF || zusteller.lampColor === LampColor.OFF) {
      data = {
        on: false,
      };
    } else {
      data = {
        on: true,
        hue: zusteller.lampColor === LampColor.BLUE_BLINKING ? 46920 :
          (zusteller.lampColor === LampColor.ORANGE ? 65535 :
              (zusteller.lampColor === LampColor.GREEN ? 25500 : 0)
          ),
      };
    }

    const req = http.request({
      hostname: 'localhost',
      port: 8000,
      path: `/api/newdeveloper/lights/${zusteller.lamp}/state`,
      method: 'PUT',
    }, res => {
      // TODO check status response
    });

    req.on('error', (e) => {
      console.error(`problem with request: ${e.message}`);
      // TODO handle response
    });

    req.write(JSON.stringify(data));
    req.end();
  }

  @Interval(900, { waiting: true, enable: true })
  private async checkBliking() {
    await Promise.all(AppService.zusteller.map(async zusteller => {
      if (zusteller.lampColor === LampColor.BLUE_BLINKING) {
        zusteller.lampColor = LampColor.BLUE_BLINKING_OFF;
        await this.color(zusteller).catch(() => {
          // TODO handle error
        });
      } else if (zusteller.lampColor === LampColor.BLUE_BLINKING_OFF) {
        zusteller.lampColor = LampColor.BLUE_BLINKING;
        await this.color(zusteller).catch(() => {
          // TODO handle error
        });
      }
    }));
  }
}
