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

// connection to hue
// @author Strobel

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
      hostname: '10.28.209.13',
      port: 9002,
      path: `/api/2217334838210e7f244460f83b42026f/lights/${zusteller.lamp}/state`,
      method: 'PUT',
    }, res => {
    });

    req.on('error', (e) => {
      console.error(`problem with request: ${e.message}`);
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
          console.error("Problem with blinking")
        });
      } else if (zusteller.lampColor === LampColor.BLUE_BLINKING_OFF) {
        zusteller.lampColor = LampColor.BLUE_BLINKING;
        await this.color(zusteller).catch(() => {
          console.error("Problem with blinking")
        });
      }
    }));
  }
}
