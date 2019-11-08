import { HttpException, Injectable } from '@nestjs/common';
import { Zusteller, ZustellerState } from './zusteller';

@Injectable()
export class AppService {
  public static zusteller: Zusteller[] = [];

  constructor() {
    AppService.zusteller.push(new Zusteller('Hans'));
    AppService.zusteller.push(new Zusteller('Georg'));
    AppService.zusteller.push(new Zusteller('Anna'));
  }

  createJob(id: number, lat: number, lon: number, temp: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.READY) {
      // TODO ist der Error Code richtig?
      throw new HttpException('Der Zusteller ist nicht bereit eine Bestellung entgegen zu nehmen', 400);
    }

    zust.status = ZustellerState.DELIVERING;
    zust.temperature = temp;
    zust.startDelivery = new Date().getUTCMilliseconds();
    // TODO Route berechnen
    // TODO stopDelivery setzen
  }

  updateJob(id: number, lat: number, lon: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.DELIVERING) {
      // TODO ist der Error Code richtig?
      throw new HttpException('Der Zusteller ist gerade nicht unterwegs.', 400);
    }

    // TODO Route berechnen
    // TODO stopDelivery setzen
  }

  deleteJob(id: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.DELIVERED) {
      // TODO ist der Error Code richtig?
      throw new HttpException('Der Zusteller ist noch unterwegs.', 400);
    }

    zust.status = ZustellerState.READY;
    zust.startDelivery = 0;
    zust.stopDelivery = 0;
    zust.temperature = 0;
  }
}
