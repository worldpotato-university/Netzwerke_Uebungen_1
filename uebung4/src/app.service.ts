import { HttpException, Injectable } from '@nestjs/common';
import { Zusteller, ZustellerState } from './zusteller';
import { HereService } from './here.service';

@Injectable()
export class AppService {
  public static zusteller: Zusteller[] = [];

  constructor(private readonly hereService: HereService) {
    AppService.zusteller.push(new Zusteller('Hans', 0));
    AppService.zusteller.push(new Zusteller('Georg', 1));
    AppService.zusteller.push(new Zusteller('Anna', 2));
  }

  async createJob(id: number, street: string, temp: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.READY) {
      throw new HttpException('Der Zusteller ist nicht bereit eine Bestellung entgegen zu nehmen', 405); // 405 Method Not Allowed
    }

    zust.status = ZustellerState.DELIVERING;
    zust.temperature = temp;
    zust.startDelivery = Date.now();
    zust.lat = 48.155049;
    zust.lon = 11.555903;
    const response = (await this.hereService.geocode(street)).Response;
    if (response.View &&
      response.View.length !== 0 &&
      response.View[0].Result &&
      response.View[0].Result.length !== 0 &&
      response.View[0].Result[0].Location.NavigationPosition &&
      response.View[0].Result[0].Location.NavigationPosition.length !== 0) {
      zust.latDestination = response.View[0].Result[0].Location.NavigationPosition[0].Latitude;
      zust.lonDestination = response.View[0].Result[0].Location.NavigationPosition[0].Longitude;
    } else {
      // TODO throw correct http error
      throw 'no address found';
    }
  }

  updateJob(id: number, lat: number, lon: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.DELIVERING) {
      throw new HttpException('Der Zusteller ist gerade nicht unterwegs.', 405); // 405 Method Not Allowed
    }

    const dTime = Math.ceil((Date.now() - zust.startDelivery) / (1000 * 60));
    zust.temperature = zust.temperature - dTime;
    zust.lat = lat;
    zust.lon = lon;
    zust.startDelivery = Date.now();
  }

  deleteJob(id: number) {
    const zust = AppService.zusteller[id];
    if (zust.status !== ZustellerState.DELIVERED) {
      throw new HttpException('Der Zusteller ist noch unterwegs.', 405); // 405 Method Not Allowed
    }

    zust.status = ZustellerState.READY;
    zust.startDelivery = 0;
    zust.stopDelivery = 0;
    zust.temperature = 0;
  }
}
